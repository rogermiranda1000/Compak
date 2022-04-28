package entities;

public abstract class SymbolTableEntry {
    private static int used_add = 0;

    private final String name;

    private final int address;

    private final SymbolTable scope;

    public SymbolTableEntry(String name, int size, SymbolTable scope) {
        this.name = name;
        this.scope = scope;

        this.address = SymbolTableEntry.used_add;
        SymbolTableEntry.used_add += size;
    }

    public String getName() {
        return name;
    }

    public int getAddress() {
        return address;
    }

    public SymbolTable getScope() {
        return scope;
    }
}
