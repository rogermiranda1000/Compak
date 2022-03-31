package lexic;

import entities.Token;
import entities.TokenDataPair;
import preprocesser.LineRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenBuffer implements TokenRequest {
    private static final Pattern tokenSplitter = Pattern.compile("^(\"(?:[^\"]|(?<=\\\\)\")*\"|\\S+)\\s*(.*)$");

    private final LineRequest lineRequest;
    private final ArrayList<TokenDataPair> tokens;

    private int currentLine;

    public TokenBuffer(LineRequest lineRequest) {
        this.lineRequest = lineRequest;
        this.tokens = new ArrayList<>();

        this.currentLine = -1; // once the first line is readed the current line will be 0
    }

    private void readTokensFromNextLine() {
        try {
            Matcher m = tokenSplitter.matcher(this.lineRequest.getNextLine());
            this.currentLine++;
            while (m.find()) {
                this.tokens.add(Token.getMatch(m.group(1)));
                m = tokenSplitter.matcher(m.group(2));
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

    @Override
    public int getCurrentLine() {
        return this.currentLine;
    }

    @Override
    public int getCurrentColumn() {
        return 0; // TODO
    }

    @Override
    public void returnTokens(List<TokenDataPair> tokens) {
        this.tokens.addAll(0, tokens);
    }
}
