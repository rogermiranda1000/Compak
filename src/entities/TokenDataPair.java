package entities;

public class TokenDataPair {
    private final Token token;
    private final String data; // used for variables, functions, strings and digits
    private boolean promoted;

    public TokenDataPair(Token token, String data) {
        this.token = token;
        this.data = data;
        this.promoted = false;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted() {
        this.promoted = true;
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

    public boolean isMeaningLessToken(Token[] list) {
        for (int i = 0; i < list.length; i++) {
            if (this.token == list[i]) {
                return true;
            }
        }
        return false;
    }
}
