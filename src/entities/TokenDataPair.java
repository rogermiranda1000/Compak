package entities;

public class TokenDataPair {
    private final Token token;
    private final String data; // used for variables, functions, strings and digits

    public TokenDataPair(Token token, String data) {
        this.token = token;
        this.data = data;
    }

    public TokenDataPair(Token token) {
        this(token, null);
    }

    public Token getToken() {
        return this.token;
    }

    public String getData() {
        return this.data;
    }

    @Override
    public String toString() {
        String data = "";
        if (this.data != null) data = " (" + this.data + ")";
        return token.name() + data;
    }
}
