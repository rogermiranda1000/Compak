package entities;

public class TokenDataPair {
    private Token token;
    private final String data;                  // used for variables, functions, strings and digits
    private SymbolTableEntry variableNode;      // used for variables/functions
    private boolean promoted;

    public TokenDataPair(Token token, String data) {
        this.token = token;
        this.data = data;
        this.promoted = false;
    }

    public void setToken(Token t) {
        this.token = t;
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

    public SymbolTableEntry getVariableNode() {
        return this.variableNode;
    }

    public void setVariableNode(SymbolTableEntry entry) {
        this.variableNode = entry;
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
