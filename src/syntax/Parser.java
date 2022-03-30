package syntax;

import lexic.TokenBuffer;
import lexic.TokenRequest;
import preprocesser.CodeProcessor;

import java.io.FileNotFoundException;
import java.util.List;

public class Parser {
    private final TokenRequest tokenRequest;
    private final GrammarRequest grammarRequest;

    public Parser(TokenRequest tokenRequest, GrammarRequest grammarRequest) {
        this.tokenRequest = tokenRequest;
        this.grammarRequest = grammarRequest;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Parser p = new Parser(new TokenBuffer(new CodeProcessor("file.sus")), new GrammarAnalizer());
        List<FirstFollowData> firstFollow = p.grammarRequest.getFirstFollow();
        System.out.println();
    }
}
