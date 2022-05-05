package syntax;

import entities.DuplicateVariableException;
import entities.UnknownVariableException;

import java.io.File;
import java.io.IOException;

public interface Compiler {
    public boolean compile(File out) throws InvalidTreeException, DuplicateVariableException, UnknownVariableException, IOException;
}
