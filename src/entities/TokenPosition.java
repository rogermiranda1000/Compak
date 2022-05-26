package entities;

/**
 * Class Token Position. It is defining the line and column where the token is.
 */
public class TokenPosition {
    private final int line, column;

    /**
     * Instantiates a new Token position.
     *
     * @param line   the line where is the token
     * @param column the column where is the token
     */
    public TokenPosition(int line, int column) {
        this.line = line;
        this.column = column;
    }

    /**
     * Gets line.
     *
     * @return the line
     */
    public int getLine() {
        return line;
    }

    /**
     * Gets column.
     *
     * @return the column
     */
    public int getColumn() {
        return column;
    }
}
