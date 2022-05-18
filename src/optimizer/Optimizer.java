package optimizer;

import java.io.File;
import java.io.IOException;

/**
 * The interface Optimizer.
 */
public interface Optimizer {
    /**
     * Optimize TAC code.
     *
     * @param file tac's file
     * @throws IOException the io exception
     */
    public void optimize(File file) throws IOException;
}
