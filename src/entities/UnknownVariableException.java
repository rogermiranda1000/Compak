package entities;

/**
 * Unknown Variable Exception extending RuntimeException.
 */
public class UnknownVariableException extends RuntimeException {
    /**
     * Instantiates a new Unknown Variable Exception.
     *
     * @param str the error string
     */
    public UnknownVariableException(String str) {
        super(str);
    }
}
