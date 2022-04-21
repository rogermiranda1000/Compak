package syntax;

import entities.SymbolTable;
import entities.Token;
import entities.TokenDataPair;
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
    private AbstractTreeNode tree;

    public Parser(TokenRequest tokenRequest, GrammarRequest grammarRequest) {
        this.tokenRequest = tokenRequest;
        this.grammarRequest = grammarRequest;

        this.symbolTable = new SymbolTable();
    }

    @Nullable
    private AbstractTreeNode generateAbstractTree(HashMap<Production, FirstFollowData> firstFollowData, Production p) throws InvalidTreeException {
        for (Object[] productions : p.getProduccions()) {
            boolean match = true,
                first = true; // if it's the first token it can fail, but if not then it's an error
            AbstractTreeNode r = new AbstractTreeNode(p);
            for (Object tokenOrProduction : productions) {
                TokenDataPair token = this.tokenRequest.requestNextToken();

                if (tokenOrProduction instanceof Production) {
                    this.tokenRequest.returnTokens(token);
                    AbstractTreeNode node = this.generateAbstractTree(firstFollowData, (Production) tokenOrProduction);
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
            if (match) return r;
            else {
                // s'han de retornar tots els tokens utilitzats per construir el que portavem d'arbre
                this.tokenRequest.returnTokens(r.getTokens());
            }
        }
        return null;
    }

    private AbstractTreeNode generateAbstractTree() throws InvalidTreeException {
        return this.generateAbstractTree(this.grammarRequest.getFirstFollowHash(), this.grammarRequest.getEntryPoint());
    }

    public boolean compile(File out) throws InvalidTreeException {
        this.tree = this.generateAbstractTree();
        return tree != null;
    }
    public void test() {
        TestMaster.testAll();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Parser p = new Parser(new TokenBuffer(new CodeProcessor("file.sus")), new GrammarAnalizer());
        p.compile(null);
        p.test();
        System.out.println();
    }
}
