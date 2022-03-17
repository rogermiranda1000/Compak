package preprocesser;

import java.util.NoSuchElementException;

public interface LineRequest {
    public String getNextLine() throws NoSuchElementException;
}
