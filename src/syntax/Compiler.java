package syntax;

import entities.DuplicateVariableException;

import java.io.File;

public interface Compiler {
    public boolean compile(File out) throws InvalidTreeException, DuplicateVariableException;
}
