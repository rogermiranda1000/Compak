package entities;

/**
 * Duplicate Variable Exception extending RuntimeException.
 */
public class DuplicateVariableException extends RuntimeException {
    /**
     * Instantiates a new Duplicate Variable exception.
     *
     * @param str the error string
     */
    public DuplicateVariableException(String str) {
        super(str);
    }
}
