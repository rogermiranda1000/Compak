package lexic;

import entities.Token;
import preprocesser.LineRequest;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenBuffer implements TokenRequest {
    private static final Pattern regex = Pattern.compile("^(\"(?:[^\"]|(?<=\\\\)\")*\"|\\S+)\\s*(.*)$");

    private final LineRequest lineRequest;
    private final ArrayList<Token> tokens;

    public TokenBuffer(LineRequest lineRequest) {
        this.lineRequest = lineRequest;
        this.tokens = new ArrayList<>();
    }

    private void readTokensFromNextLine() {
        try {
            Matcher m = regex.matcher(this.lineRequest.getNextLine());
            while (m.find()) {
                this.tokens.add(Token.getMatch(m.group(1)));
                m = regex.matcher(m.group(2));
            }
        } catch (NoSuchElementException ex) {
            this.tokens.add(Token.EOF);
        }
    }

    @Override
    public Token requestNextToken() {
        while (this.tokens.size() == 0) this.readTokensFromNextLine();
        return tokens.remove(0);
    }
}
