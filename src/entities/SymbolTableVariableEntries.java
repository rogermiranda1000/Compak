package entities;

public class SymbolTableVariableEntries extends SymbolTableEntries {
    private final VariableTypes type;

    /**
     * Number of variables (1 for non-array)
     */
    private final int size;

    public SymbolTableVariableEntries(VariableTypes type, String name, int size, int scope) {
        super(name, type.getSize()*size, scope);
        this.type = type;
        this.size = size;
    }

    public SymbolTableVariableEntries(VariableTypes type, String name, int scope) {
        this(type, name, 1, scope);
    }


    public VariableTypes getType() {
        return this.type;
    }

    public int getSize() {
        return this.size;
    }
}
