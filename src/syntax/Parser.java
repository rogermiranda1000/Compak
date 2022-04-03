package syntax;

import entities.*;
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
    private AbstractTreeNode generateAbstractTree(HashMap<Production, FirstFollowData> firstFollowData, Production p) throws InvalidTreeException {
        for (Object[] productions : p.getProduccions()) {
            List<TokenDataPair> requestedTokens = new ArrayList<>();
            boolean match = true,
                first = true; // if it's the first token it can fail, but if not then it's an error
            AbstractTreeNode r = new AbstractTreeNode(p);
            for (Object tokenOrProduction : productions) {
                TokenDataPair token = this.tokenRequest.requestNextToken();
                requestedTokens.add(token);

                if (tokenOrProduction instanceof Production) {
                    this.tokenRequest.returnTokens(requestedTokens.remove(requestedTokens.size()-1));
                    AbstractTreeNode node = this.generateAbstractTree(firstFollowData, (Production) tokenOrProduction);
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

    private AbstractTreeNode generateAbstractTree() throws InvalidTreeException {
        return this.generateAbstractTree(this.grammarRequest.getFirstFollowHash(), this.grammarRequest.getEntryPoint());
    }

    private SymbolTable generateSymbolTable(AbstractTreeNode abstractTree, SymbolTable parent) throws DuplicateVariableException {
        SymbolTable symbolTable = new SymbolTable(parent);
        List<Object> nodes = abstractTree.getTreeExtend();

        if (abstractTree.getOriginalProduction() == GrammarAnalizer.declaracioVariable) {
            // variable declaration
            AbstractTreeNode tipusNode = (AbstractTreeNode) nodes.get(0);
            Token tipus = ((TokenDataPair) tipusNode.getTreeExtend().get(0)).getToken();
            symbolTable.addEntry(new SymbolTableVariableEntries(VariableTypes.tokenToVariableType(tipus), ((TokenDataPair) nodes.get(1)).getData(), symbolTable)); // <tipus> <nom_variable>
        }
        else if (abstractTree.getOriginalProduction() == GrammarAnalizer.start) {
            // main declaration
            symbolTable.addEntry(new SymbolTableFunctionEntries(VariableTypes.VOID, (String) Token.MAIN.getMatch(), new VariableTypes[]{}, symbolTable));
        }
        else if (abstractTree.getOriginalProduction() == GrammarAnalizer.declaracioFuncio) {
            // function declaration: "func " <nom_funcio> "(" <arguments> ")" <declaracio_funcio_sub> "{" <sentencies> "}"
            String funcName = ((TokenDataPair) nodes.get(1)).getData();

            AbstractTreeNode funcAttr = (AbstractTreeNode) nodes.get(5);
            VariableTypes returnType = VariableTypes.VOID;
            if (funcAttr.getTreeExtend().size() > 0) { // it may be epsilon
                //  ":" <tipus>
                AbstractTreeNode returnTypeNode = (AbstractTreeNode) funcAttr.getTreeExtend().get(1);
                Token returnTypeToken = ((TokenDataPair) returnTypeNode.getTreeExtend().get(0)).getToken();
                returnType = VariableTypes.tokenToVariableType(returnTypeToken);
            }

            symbolTable.addEntry(new SymbolTableFunctionEntries(returnType, funcName, new VariableTypes[]{ /* TODO */ }, symbolTable));
        }
        // TODO afegir variables enviades a les funcions

        for (Object node : nodes) {
            if (node instanceof AbstractTreeNode) symbolTable.addSubtable(this.generateSymbolTable((AbstractTreeNode) node, symbolTable));
            // else TokenDataPair; already done in the first part
        }

        return symbolTable;
    }

    private SymbolTable generateSymbolTable(AbstractTreeNode abstractTree) throws DuplicateVariableException {
        return this.generateSymbolTable(abstractTree, null)
                .optimize();
    }

    public void compile(File out) throws InvalidTreeException, DuplicateVariableException {
        AbstractTreeNode tree = this.generateAbstractTree();
        if (tree == null) return;
        SymbolTable symbolTable = this.generateSymbolTable(tree);
        System.out.println();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Parser p = new Parser(new TokenBuffer(new CodeProcessor("file.sus")), new GrammarAnalizer());
        p.compile(null);
        System.out.println();
    }
}
