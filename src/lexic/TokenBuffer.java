package lexic;

import entities.Token;
import entities.TokenDataPair;
import entities.TokenNotGettedException;
import entities.TokenPosition;
import preprocesser.LineRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class TokenBuffer.
 */
public class TokenBuffer implements TokenRequest {
    private static final Pattern tokenSplitter = Pattern.compile("^(\"(?:[^\"]|(?<=\\\\)\")*\"|(?<=\\w|\\.|^)[\\w.]+|==|[;=+\\-*/%^¡!&|<>:{}(),])(\\s*)(.*)$");

    private final LineRequest lineRequest;
    private final ArrayList<TokenDataPair> tokens;

    private final ArrayList<TokenPosition> tokenPositions;

    /**
     * 1 means the first
     */
    private int currentLine;

    /**
     * Constructor TokenBuffer.
     *
     * @param lineRequest the line requested
     */
    public TokenBuffer(LineRequest lineRequest) {
        this.lineRequest = lineRequest;
        this.tokens = new ArrayList<>();
        this.tokenPositions = new ArrayList<>();

        this.currentLine = 0; // once the first line is readed the current line will be 1
    }

    private void readTokensFromNextLine() {
        try {
            String line = this.lineRequest.getNextLine();
            int column = line.length() + 1; // tmp value
            line = line.replaceAll("^\\s+", ""); // remove leading spaces
            column -= line.length(); // the difference between the old and the new string (+1) it's the column number
            Matcher m = tokenSplitter.matcher(line);
            Matcher next;
            this.currentLine++;
            while (m.find()) {
                TokenDataPair token = Token.getMatch(m.group(1));
                // is it a function name?
                next = tokenSplitter.matcher(m.group(3));
                if (token.getToken().equals(Token.ID) && next.find() && Token.getMatch(next.group(1)).getToken().equals(Token.OPN_PARENTH))
                    token.setToken(Token.ID_FUNC);

                this.tokens.add(token);
                this.tokenPositions.add(new TokenPosition(this.currentLine, column));
                column += m.group(1).length() + m.group(2).length();

                m = tokenSplitter.matcher(m.group(3));
            }
        } catch (NoSuchElementException ex) {
            this.tokens.add(new TokenDataPair(Token.EOF));
        }
    }

    @Override
    public TokenDataPair requestNextToken() {
        while (this.tokens.size() == 0) this.readTokensFromNextLine();
        return tokens.remove(0);
    }

    private TokenPosition getCurrentTokenInfo() throws TokenNotGettedException {
        try {
            return this.tokenPositions.get(this.tokenPositions.size() - this.tokens.size() - 1);
        } catch (IndexOutOfBoundsException ex) {
            throw new TokenNotGettedException("Trying to get info about a token when you didn't ask for any.");
        }
    }

    @Override
    public int getCurrentLine() {
        return this.getCurrentTokenInfo().getLine();
    }

    @Override
    public int getCurrentColumn() {
        return this.getCurrentTokenInfo().getColumn();
    }

    @Override
    public void returnTokens(List<TokenDataPair> tokens) {
        for (int n = tokens.size()-1; n >= 0; n--) returnTokens(tokens.get(n));
    }

    @Override
    public void returnTokens(TokenDataPair token) {
        this.tokens.add(0, token);
    }
}
