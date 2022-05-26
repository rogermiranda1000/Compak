package entities;

/**
 * Class Symbol table function entry.
 */
public class SymbolTableFunctionEntry extends SymbolTableEntry {
    private final VariableTypes returnType;
    private final VariableTypes[] arguments;

    /**
     * Instantiates a new Symbol table function entry.
     *
     * @param returnType the return type
     * @param name       the name
     * @param arguments  the arguments
     * @param scope      the scope
     */
    public SymbolTableFunctionEntry(VariableTypes returnType, String name, VariableTypes[] arguments, SymbolTable scope) {
        super(name, 0, scope);
        this.returnType = returnType;
        this.arguments = arguments;
    }

    /**
     * Gets return type.
     *
     * @return the return type
     */
    public VariableTypes getReturnType() {
        return this.returnType;
    }

    /**
     * Get arguments variable types [ ].
     *
     * @return the variable types [ ]
     */
    public VariableTypes[] getArguments() {
        return arguments;
    }
}
