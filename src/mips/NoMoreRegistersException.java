package mips;

/**
 * Exception that is thrown when there is no more register for compiler mars (mips).
 */
public class NoMoreRegistersException extends Exception {

    /**
     * Instantiates a new NoMoreRegistersException exception.
     *
     * @param error string error
     */
    public NoMoreRegistersException(String error) {
        super(error);
    }
}
