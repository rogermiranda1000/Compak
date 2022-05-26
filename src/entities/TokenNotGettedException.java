package entities;

/**
 * Token Not Getted Exception extending RuntimeException.
 */
public class TokenNotGettedException extends RuntimeException {
    /**
     * Instantiates a new Token Not Getted Exception.
     *
     * @param str the error string
     */
    public TokenNotGettedException(String str) {
        super(str);
    }
}
