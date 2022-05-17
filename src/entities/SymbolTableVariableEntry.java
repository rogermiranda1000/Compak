package entities;

import java.security.InvalidParameterException;

/**
 * Class Symbol table variable entry.
 */
public class SymbolTableVariableEntry extends SymbolTableEntry {
    private final VariableTypes type;

    /**
     * Number of variables (1 for non-array)
     */
    private final int size;

    /**
     * Instantiates a new Symbol table variable entry.
     *
     * @param type  the type
     * @param name  the name
     * @param size  the size
     * @param scope the scope
     */
    public SymbolTableVariableEntry(VariableTypes type, String name, int size, SymbolTable scope) {
        super(name, type.getSize()*size, scope);
        this.type = type;
        this.size = size;

        if (type == VariableTypes.UNKNOWN || type == VariableTypes.VOID) throw new InvalidParameterException("Unexpected variable type");
    }

    /**
     * Instantiates a new Symbol table variable entry.
     *
     * @param type  the type
     * @param name  the name
     * @param scope the scope
     */
    public SymbolTableVariableEntry(VariableTypes type, String name, SymbolTable scope) {
        this(type, name, 1, scope);
    }


    /**
     * Gets type.
     *
     * @return the type
     */
    public VariableTypes getType() {
        return this.type;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize() {
        return this.size;
    }
}
