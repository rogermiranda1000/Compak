package entities;

/**
 * Class Token Data Pair. It is defined by a token and more specific data from the token.
 * Example: ID (a).
 */
public class TokenDataPair {
    private final Token token;
    private final String data;                  // used for variables, functions, strings and digits
    private SymbolTableEntry variableNode;      // used for variables/functions
    private boolean promoted;

    /**
     * Instantiates a new Token Data Pair.
     *
     * @param token the token
     * @param data  data from the token
     */
    public TokenDataPair(Token token, String data) {
        this.token = token;
        this.data = data;
        this.promoted = false;
    }

    /**
     * Instantiates a new Token Data Pair without data.
     *
     * @param token the token
     */
    public TokenDataPair(Token token) {
        this(token, null);
    }

    /**
     * Is promoted boolean.
     *
     * @return the boolean
     */
    public boolean isPromoted() {
        return promoted;
    }

    /**
     * Sets promoted.
     */
    public void setPromoted() {
        this.promoted = true;
    }

    /**
     * Gets token.
     *
     * @return the token
     */
    public Token getToken() {
        return this.token;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public String getData() {
        return this.data;
    }

    /**
     * Gets variable node.
     *
     * @return the variable node
     */
    public SymbolTableEntry getVariableNode() {
        return this.variableNode;
    }

    /**
     * Sets variable node.
     *
     * @param entry the entry
     */
    public void setVariableNode(SymbolTableEntry entry) {
        this.variableNode = entry;
    }

    @Override
    public String toString() {
        String data = "";
        if (this.data != null) data = " (" + this.data + ")";
        return token.name() + data;
    }

    /**
     * Function that check is the token is a meaning less token.
     *
     * @param list the list of all meaning less tokens
     * @return true if it's meaning less, otherwise false
     */
    public boolean isMeaningLessToken(Token[] list) {
        for (int i = 0; i < list.length; i++) {
            if (this.token == list[i]) {
                return true;
            }
        }
        return false;
    }
}
