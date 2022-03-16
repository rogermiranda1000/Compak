package lexic;

import entities.Token;
import preprocesser.LineRequest;

import java.util.ArrayList;

public class TokenBuffer implements TokenRequest {
    private final LineRequest lineRequest;
    private final ArrayList<Token> tokens;

    public TokenBuffer(LineRequest lineRequest) {
        this.lineRequest = lineRequest;
        this.tokens = new ArrayList<>();
    }

    @Override
    public Token requestNextToken() {
        Token next = Token.NONE;
        while (next == Token.NONE) {
            
        }
        return next;
    }
}
