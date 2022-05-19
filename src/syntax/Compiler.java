package syntax;

import entities.DuplicateVariableException;
import entities.UnknownVariableException;
import entities.ThreeAddressLine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Interface Compiler.
 */
public interface Compiler {
    /**
     * Function that executes the "compile" of the language of this project.
     *
     * @param out file
     * @throws InvalidTreeException       the invalid tree exception
     * @throws DuplicateVariableException the duplicate variable exception
     * @throws UnknownVariableException   the unknown variable exception
     * @throws IOException                the io exception
     */
    public void compile(File out) throws InvalidTreeException, DuplicateVariableException, UnknownVariableException, IOException;

    /**
     * Gets all three address lines generated travelling the abstract syntax tree.
     *
     * @return array with three address lines
     */
    public ArrayList<ThreeAddressLine> getThreeAddressLines();
}
