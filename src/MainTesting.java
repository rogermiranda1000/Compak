import entities.DuplicateVariableException;
import entities.UnknownVariableException;
import syntax.InvalidTreeException;
import testing.TestMaster;

import java.io.IOException;

public class MainTesting {
    /**
     * The entry point for testing.
     *
     * @param args the input arguments
     * @throws InvalidTreeException       the invalid tree exception
     * @throws DuplicateVariableException the duplicate variable exception
     * @throws UnknownVariableException   the unknown variable exception
     * @throws IOException                the io exception
     */
    public static void main(String[] args) throws InvalidTreeException, DuplicateVariableException, UnknownVariableException, IOException {
        TestMaster.testAll();
    }
}