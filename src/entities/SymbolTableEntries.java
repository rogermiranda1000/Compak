package entities;

public class SymbolTableEntries {
    private static int used_add = 0;

    private final String name;
    private final VariableTypes type;
    private final int size; // 1 for non-array
    private final int address;
    private final int scope;

    public SymbolTableEntries(VariableTypes type, String name, int scope) {
        this.name = name;
        this.type = type;
        this.size = type.getSize();
        this.scope = scope;

        this.address = SymbolTableEntries.used_add;
        SymbolTableEntries.used_add += this.size;
    }

    public String getName() {
        return name;
    }

    public VariableTypes getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public int getAddress() {
        return address;
    }

    public int getScope() {
        return scope;
    }
}
