package syntax;

import entities.*;
import intermediateCode.IntermediateCodeGenerator;
import lexic.TokenBuffer;
import lexic.TokenRequest;
import org.jetbrains.annotations.Nullable;
import preprocesser.CodeProcessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Parser implements Compiler {
    private final TokenRequest tokenRequest;
    private final GrammarRequest grammarRequest;

    public Parser(TokenRequest tokenRequest, GrammarRequest grammarRequest) {
        this.tokenRequest = tokenRequest;
        this.grammarRequest = grammarRequest;
    }

    @Nullable
    private ParseTree generateParseTree(HashMap<Production, FirstFollowData> firstFollowData, Production p) throws InvalidTreeException {
        for (Object[] productions : p.getProduccions()) {
            boolean match = true,
                first = true; // if it's the first token it can fail, but if not then it's an error
            ParseTree r = new ParseTree(p);
            TokenDataPair token;
            for (Object tokenOrProduction : productions) {
                token = this.tokenRequest.requestNextToken();

                if (tokenOrProduction instanceof Production) {
                    this.tokenRequest.returnTokens(token);
                    ParseTree node = this.generateParseTree(firstFollowData, (Production) tokenOrProduction);
                    if (node == null) {
                        // error; return the tokens and start with other production
                        if (!first) {
                            Set<Token> firstFollow = firstFollowData.get((Production) tokenOrProduction).getFirst();
                            if (firstFollow.remove(Token.EPSILON)) firstFollow.addAll(firstFollowData.get((Production) tokenOrProduction).getFollow()); // if epsilon -> also follow
                            //throw new InvalidTreeException(token.getToken(), this.tokenRequest.getCurrentLine(), this.tokenRequest.getCurrentColumn(), firstFollow);
                        }

                        match = false;
                        break;
                    }
                    r.addTree(node);
                }
                else {
                    r.addTree(token);

                    if (!token.getToken().equals((Token)tokenOrProduction)) {
                        // error; return the tokens and start with other production
                        /*if (!first) throw new InvalidTreeException(token.getToken(), this.tokenRequest.getCurrentLine(),
                                this.tokenRequest.getCurrentColumn(), (Token)tokenOrProduction);*/

                        match = false;
                        break;
                    }
                }
                first = false;
            }

            if (match) {
                if (p != this.grammarRequest.getEntryPoint()) return r;

                // si és el punt d'entrada ha d'acabar amb EOF
                token = this.tokenRequest.requestNextToken();
                if (token.getToken() == Token.EOF) return r;
                this.tokenRequest.returnTokens(token);
                // !match
            }

            // !match => s'han de retornar tots els tokens utilitzats per construir el que portavem d'arbre
            this.tokenRequest.returnTokens(r.getTokens());
        }
        return null;
    }

    private ParseTree generateParseTree() throws InvalidTreeException {
        return this.generateParseTree(this.grammarRequest.getFirstFollowHash(), this.grammarRequest.getEntryPoint());
    }

    private AbstractSyntaxTree generateAbstractSyntaxTree(ParseTree parseTree, ArrayList<AbstractSyntaxTree> codes) {
        AbstractSyntaxTree tree = new AbstractSyntaxTree(parseTree);
        tree.removeEpsilons();
        tree.removeRedundantProductions();
        tree.removeMeaningLessTokens();
        tree.removeRedundantProductions();
        tree.calculateLevels();
        tree.calculateHeight();

        tree.recalculateFathers();
        tree.promoteTokens();

        tree.removeRedundantProductions();
        tree.recalculateFathers();

        // First approach to 3@Code
        // tree.travelWithPriorityDepth();

        return tree;
    }

    private void generateSymbolTable(ParseTree parseTree, SymbolTable scope) throws DuplicateVariableException, UnknownVariableException {
        List<Object> nodes = parseTree.getTreeExtend();

        Stack<SymbolTable> scopes = new Stack<>();
        scopes.add(scope);

        if (parseTree.getOriginalProduction() == GrammarAnalizer.declaracioVariable) {
            // variable declaration
            ParseTree tipusNode = (ParseTree) nodes.get(0);
            Token tipus = ((TokenDataPair) tipusNode.getTreeExtend().get(0)).getToken();
            scope.addEntry(new SymbolTableVariableEntry(VariableTypes.tokenToVariableType(tipus), ((TokenDataPair) nodes.get(1)).getData(), scope)); // <tipus> <nom_variable>
        }
        else if (parseTree.getOriginalProduction() == GrammarAnalizer.start) {
            // main declaration
            scope.addEntry(new SymbolTableFunctionEntry(VariableTypes.VOID, (String) Token.MAIN.getMatch(), new VariableTypes[]{}, scope));
        }
        else if (parseTree.getOriginalProduction() == GrammarAnalizer.declaracioFuncio) {
            // function declaration: "func " <nom_funcio> "(" <arguments> ")" <declaracio_funcio_sub> "{" <sentencies> "}"
            String funcName = ((TokenDataPair) nodes.get(1)).getData();

            ParseTree funcRet = (ParseTree) nodes.get(5);
            VariableTypes returnType = VariableTypes.VOID;
            if (funcRet.getTreeExtend().size() > 0) { // it may be epsilon
                //  ":" <tipus>
                ParseTree returnTypeNode = (ParseTree) funcRet.getTreeExtend().get(1);
                Token returnTypeToken = ((TokenDataPair) returnTypeNode.getTreeExtend().get(0)).getToken();
                returnType = VariableTypes.tokenToVariableType(returnTypeToken);
            }

            SymbolTable functionScope = new SymbolTable(scope);
            ParseTree funcAttr = (ParseTree) nodes.get(3);
            List<VariableTypes> arguments = new ArrayList<>();
            while (funcAttr.getTreeExtend().size() > 0) { // it may be epsilon
                // <tipus> <nom_variable> <arguments_sub>
                ParseTree argumentTypeNode = (ParseTree) funcAttr.getTreeExtend().get(0);
                Token argumentTypeToken = ((TokenDataPair) argumentTypeNode.getTreeExtend().get(0)).getToken();
                VariableTypes argumentType = VariableTypes.tokenToVariableType(argumentTypeToken);

                arguments.add(argumentType);
                functionScope.addEntry(new SymbolTableVariableEntry(argumentType, ((TokenDataPair) funcAttr.getTreeExtend().get(1)).getData(), functionScope));

                funcAttr = (ParseTree) funcAttr.getTreeExtend().get(2);
                if (funcAttr.getTreeExtend().size() > 0) funcAttr = (ParseTree) funcAttr.getTreeExtend().get(1);
                // add the next argument in the next iteration
            }

            scopes.empty(); scopes.add(functionScope); // for the ID substitution from below
            scope.addEntry(new SymbolTableFunctionEntry(returnType, funcName, arguments.toArray(new VariableTypes[0]), scope));
            scope.addSubtable(functionScope);
        }

        for (Object node : nodes) {
            if (node instanceof ParseTree) {
                this.generateSymbolTable((ParseTree) node, scopes.peek());
            }
            else {
                // TokenDataPair
                TokenDataPair dataPair = (TokenDataPair)node;
                if (dataPair.getToken() == Token.ID) {
                    // add relation between variable and scope table (created above)
                    String varName = dataPair.getData();
                    SymbolTableEntry idNode = scopes.peek().searchEntry(varName);
                    if (idNode == null) throw new UnknownVariableException("Unknown variable (" + varName + ")"); // TODO error line?
                    dataPair.setVariableNode(idNode);
                }
                else if (dataPair.getToken() == Token.OPN_CONTEXT) {
                    // more specific scope
                    scopes.add(new SymbolTable(scopes.peek()));
                }
                else if (dataPair.getToken() == Token.CLS_CONTEXT) {
                    // leave scope
                    SymbolTable removed = scopes.pop();
                    if (removed.isUsed()) scopes.peek().addSubtable(removed);
                }
            }
        }
    }

    private SymbolTable generateSymbolTable(ParseTree parseTree) throws DuplicateVariableException, UnknownVariableException {
        SymbolTable r = new SymbolTable();
        this.generateSymbolTable(parseTree, r);
        return r.optimize();
    }

    public boolean compile(File out) throws InvalidTreeException, DuplicateVariableException, UnknownVariableException {
        ParseTree parseTree = generateParseTree();
        if (parseTree == null) return false;
        //parseTree.printTree();

        SymbolTable symbolTable = this.generateSymbolTable(parseTree);
        ArrayList<AbstractSyntaxTree> codes = new ArrayList<>();

        AbstractSyntaxTree abstractSyntaxTree = this.generateAbstractSyntaxTree(parseTree, codes); // TODO send symbolTable
        abstractSyntaxTree.printTree();

        IntermediateCodeGenerator intermediateCodeGenerator = new IntermediateCodeGenerator();
        intermediateCodeGenerator.process(abstractSyntaxTree);

        return true;
    }

    public void test() {
        //TestMaster.testAll();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Parser p = new Parser(new TokenBuffer(new CodeProcessor("file.sus")), new GrammarAnalizer());
        p.compile(null);
        //TODO: Uncomment for final commit
        //p.test();
    }
}
