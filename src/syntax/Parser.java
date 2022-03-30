package syntax;

import entities.TokenDataPair;
import lexic.TokenBuffer;
import lexic.TokenRequest;
import preprocesser.CodeProcessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class Parser implements Compiler {
    private final TokenRequest tokenRequest;
    private final GrammarRequest grammarRequest;

    public Parser(TokenRequest tokenRequest, GrammarRequest grammarRequest) {
        this.tokenRequest = tokenRequest;
        this.grammarRequest = grammarRequest;
    }

    private void generateAbstractTree() {
        TokenDataPair token = this.tokenRequest.requestNextToken();
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
