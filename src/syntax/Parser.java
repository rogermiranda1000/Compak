package syntax;

import entities.SymbolTable;
import entities.Token;
import entities.TokenDataPair;
import lexic.TokenBuffer;
import lexic.TokenRequest;
import preprocesser.CodeProcessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Parser implements Compiler {
    private final TokenRequest tokenRequest;
    private final GrammarRequest grammarRequest;
    private final SymbolTable symbolTable;

    public Parser(TokenRequest tokenRequest, GrammarRequest grammarRequest) {
        this.tokenRequest = tokenRequest;
        this.grammarRequest = grammarRequest;

        this.symbolTable = new SymbolTable();
    }

    private void generateAbstractTree() throws InvalidTreeException {
        TokenDataPair token = this.tokenRequest.requestNextToken();
        Production candidate = this.grammarRequest.getEntryPoint();
        HashMap<Production, FirstFollowData> firstFollow = this.grammarRequest.getFirstFollowHash();
        Collection<Token> firsts = firstFollow.get(candidate).getFirst();
        if (!firsts.contains(token.getToken())) throw new InvalidTreeException(token.getToken(), this.tokenRequest.getCurrentLine(),
                this.tokenRequest.getCurrentColumn(), firsts);
    }

    public void compile(File out) {
        this.generateAbstractTree();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Parser p = new Parser(new TokenBuffer(new CodeProcessor("file.sus")), new GrammarAnalizer());
        p.compile(null);
        System.out.println();
    }
}
