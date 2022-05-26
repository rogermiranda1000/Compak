package syntax;

import entities.Token;

import java.util.Collection;
import java.util.List;

/**
 * InvalidTree Exception. Exception thrown when exists any error at parsing tree.
 */
public class InvalidTreeException extends RuntimeException {
    private final int line;

    /**
     * Constructor for InvalidTree exception.
     *
     * @param token    token found
     * @param line     line where the error is
     * @param column   column where the error is
     * @param expected token expected
     */
    public InvalidTreeException(Token token, int line, int column, Collection<Token> expected) {
        super("Found '" + token + "' in line " + line + ", column " + column + ". Expected: " + expected.toString());
        this.line = line;
    }

    /**
     * Constructor for InvalidTree exception.
     *
     * @param token    token found
     * @param line     line where the error is
     * @param column   column where the error is
     * @param expected token expected
     */
    public InvalidTreeException(Token token, int line, int column, Token expected) {
        this(token, line, column, List.of(expected));
    }

    /**
     * Gets line.
     *
     * @return the line
     */
    public int getLine() {
        return line;
    }
}
