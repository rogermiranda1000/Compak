package preprocesser;

import java.util.NoSuchElementException;

/**
 * The interface LineRequest.
 */
public interface LineRequest {
    /**
     * Gets next line from the file.
     *
     * @return next line string
     * @throws NoSuchElementException the no such element exception
     */
    public String getNextLine() throws NoSuchElementException;
}
