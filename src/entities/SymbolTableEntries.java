package entities;

public abstract class SymbolTableEntries {
    private static int used_add = 0;

    private final String name;

    private final int address;

    private final SymbolTable scope;

    public SymbolTableEntries(String name, int size, SymbolTable scope) {
        this.name = name;
        this.scope = scope;

        this.address = SymbolTableEntries.used_add;
        SymbolTableEntries.used_add += size;
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
