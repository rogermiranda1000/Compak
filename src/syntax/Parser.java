package syntax;

import entities.*;
import lexic.TokenBuffer;
import lexic.TokenRequest;
import org.jetbrains.annotations.Nullable;
import preprocesser.CodeProcessor;
import testing.TestMaster;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Parser implements Compiler {
    private final TokenRequest tokenRequest;
    private final GrammarRequest grammarRequest;
    private final SymbolTable symbolTable;
    private AbstractSyntaxTree tree;

    public Parser(TokenRequest tokenRequest, GrammarRequest grammarRequest) {
        this.tokenRequest = tokenRequest;
        this.grammarRequest = grammarRequest;

        this.symbolTable = new SymbolTable();
    }

    @Nullable
    private ParseTree generateParseTree(HashMap<Production, FirstFollowData> firstFollowData, Production p) throws InvalidTreeException {
        for (Object[] productions : p.getProduccions()) {
            List<TokenDataPair> requestedTokens = new ArrayList<>();
            boolean match = true,
                first = true; // if it's the first token it can fail, but if not then it's an error
            ParseTree r = new ParseTree();
            for (Object tokenOrProduction : productions) {
                TokenDataPair token = this.tokenRequest.requestNextToken();
                requestedTokens.add(token);

                if (tokenOrProduction instanceof Production) {
                    this.tokenRequest.returnTokens(requestedTokens.remove(requestedTokens.size()-1));
                    ParseTree node = this.generateParseTree(firstFollowData, (Production) tokenOrProduction);
                    if (node == null) {
                        // error; return the tokens and start with other production
                        if (!first) {
                            Set<Token> firstFollow = firstFollowData.get((Production) tokenOrProduction).getFirst();
                            if (firstFollow.remove(Token.EPSILON)) firstFollow.addAll(firstFollowData.get((Production) tokenOrProduction).getFollow()); // if epsilon -> also follow
                            //throw new InvalidTreeException(token.getToken(), this.tokenRequest.getCurrentLine(), this.tokenRequest.getCurrentColumn(), firstFollow);
                        }
                        this.tokenRequest.returnTokens(requestedTokens);
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
                        this.tokenRequest.returnTokens(requestedTokens);
                        match = false;
                        break;
                    }
                }
                first = false;
            }
            if (match) return r;
        }
        return null;
    }

    private ParseTree generateParseTree() throws InvalidTreeException {
        return this.generateParseTree(this.grammarRequest.getFirstFollowHash(), this.grammarRequest.getEntryPoint());
    }

    private AbstractSyntaxTree generateAbstractSyntaxTree(ParseTree parseTree) {
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

    private void generateSymbolTable(AbstractSyntaxTree abstractTree, SymbolTable parent, SymbolTable top) throws DuplicateVariableException {
        boolean topTable = false;
        SymbolTable symbolTable = new SymbolTable(abstractTree, parent);
        List<Object> nodes = abstractTree.getTreeExtend();

        if (abstractTree.getOriginalProduction() == GrammarAnalizer.declaracioVariable) {
            // variable declaration
            AbstractSyntaxTree tipusNode = (AbstractSyntaxTree) nodes.get(0);
            Token tipus = ((TokenDataPair) tipusNode.getTreeExtend().get(0)).getToken();
            symbolTable.addEntry(new SymbolTableVariableEntries(VariableTypes.tokenToVariableType(tipus), ((TokenDataPair) nodes.get(1)).getData(), symbolTable)); // <tipus> <nom_variable>
        }
        else if (abstractTree.getOriginalProduction() == GrammarAnalizer.start) {
            // function already on top
            // main declaration
            symbolTable.addEntry(new SymbolTableFunctionEntries(VariableTypes.VOID, (String) Token.MAIN.getMatch(), new VariableTypes[]{}, symbolTable));
        }
        else if (abstractTree.getOriginalProduction() == GrammarAnalizer.declaracioFuncio) {
            // functions must be on top
            symbolTable = new SymbolTable(abstractTree, top);
            topTable = true;

            // function declaration: "func " <nom_funcio> "(" <arguments> ")" <declaracio_funcio_sub> "{" <sentencies> "}"
            String funcName = ((TokenDataPair) nodes.get(1)).getData();

            AbstractSyntaxTree funcRet = (AbstractSyntaxTree) nodes.get(5);
            VariableTypes returnType = VariableTypes.VOID;
            if (funcRet.getTreeExtend().size() > 0) { // it may be epsilon
                //  ":" <tipus>
                AbstractSyntaxTree returnTypeNode = (AbstractSyntaxTree) funcRet.getTreeExtend().get(1);
                Token returnTypeToken = ((TokenDataPair) returnTypeNode.getTreeExtend().get(0)).getToken();
                returnType = VariableTypes.tokenToVariableType(returnTypeToken);
            }

            AbstractSyntaxTree funcAttr = (AbstractSyntaxTree) nodes.get(3);
            List<VariableTypes> arguments = new ArrayList<>();
            while (funcAttr.getTreeExtend().size() > 0) { // it may be epsilon
                // <tipus> <nom_variable> <arguments_sub>
                AbstractSyntaxTree argumentTypeNode = (AbstractSyntaxTree) funcAttr.getTreeExtend().get(0);
                Token argumentTypeToken = ((TokenDataPair) argumentTypeNode.getTreeExtend().get(0)).getToken();
                VariableTypes argumentType = VariableTypes.tokenToVariableType(argumentTypeToken);

                arguments.add(argumentType);
                symbolTable.addEntry(new SymbolTableVariableEntries(argumentType, ((TokenDataPair) funcAttr.getTreeExtend().get(1)).getData(), symbolTable));

                funcAttr = (AbstractSyntaxTree) funcAttr.getTreeExtend().get(2);
                if (funcAttr.getTreeExtend().size() > 0) funcAttr = (AbstractSyntaxTree) funcAttr.getTreeExtend().get(1);
                // add the next argument in the next iteration
            }

            symbolTable.addEntry(new SymbolTableFunctionEntries(returnType, funcName, arguments.toArray(new VariableTypes[0]), symbolTable));
        }

        for (Object node : nodes) {
            if (node instanceof AbstractSyntaxTree) this.generateSymbolTable((AbstractSyntaxTree) node, symbolTable, top);
            // else TokenDataPair; already done in the first part with Production
        }

        (topTable ? top : parent).addSubtable(symbolTable);
    }

    private SymbolTable generateSymbolTable(AbstractSyntaxTree abstractTree) throws DuplicateVariableException {
        SymbolTable r = new SymbolTable();
        this.generateSymbolTable(abstractTree, r, r);
        return r.optimize();
    }




    public boolean compile(File out) throws InvalidTreeException {
        ParseTree parseTree = this.generateParseTree();
        this.tree = this.generateAbstractSyntaxTree(parseTree);
        if (tree == null) return false;
        SymbolTable symbolTable = this.generateSymbolTable(tree);
        symbolTable.apply();
        this.tree.printTree();
        return true;
    }

    public void test() {
        TestMaster.testAll();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Parser p = new Parser(new TokenBuffer(new CodeProcessor("file.sus")), new GrammarAnalizer());
        p.compile(null);
        p.test();
    }
}
