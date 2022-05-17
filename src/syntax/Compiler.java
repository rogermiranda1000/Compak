package syntax;

import entities.DuplicateVariableException;
import entities.UnknownVariableException;

import java.io.File;
import java.io.IOException;

/**
 * Interface Compiler.
 */
public interface Compiler {
    /**
     * Function that executes the "compile" of the language of this project.
     *
     * @param out file
     * @return boolean that tells the success of the compile
     * @throws InvalidTreeException       the invalid tree exception
     * @throws DuplicateVariableException the duplicate variable exception
     * @throws UnknownVariableException   the unknown variable exception
     * @throws IOException                the io exception
     */
    public boolean compile(File out) throws InvalidTreeException, DuplicateVariableException, UnknownVariableException, IOException;
}
