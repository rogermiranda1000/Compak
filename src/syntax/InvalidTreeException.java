package syntax;

import entities.Token;

import java.util.Collection;
import java.util.List;

public class InvalidTreeException extends RuntimeException {
    private final int line, column;
    private final Collection<Token> expected;
    public InvalidTreeException(Token token, int line, int column, Collection<Token> expected) {
        super("Found '" + token + "' in " + line + ", " + column + ". Expected: " + expected.toString());
        this.line = line;
        this.column = column;
        this.expected = expected;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public Collection<Token> getExpected() {
        return expected;
    }
}
