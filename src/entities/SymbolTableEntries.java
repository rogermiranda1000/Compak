package entities;

public abstract class SymbolTableEntries {
    private static int used_add = 0;

    private final String name;

    private final int address;

    private final int scope; // TODO fer taula de taules

    public SymbolTableEntries(String name, int size, int scope) {
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

    public int getScope() {
        return scope;
    }
}
