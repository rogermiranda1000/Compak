package entities;

/**
 * Class Symbol table entry.
 */
public abstract class SymbolTableEntry {
    private static int used_add = 0;

    private final String name;

    private final int address;

    private final SymbolTable scope;

    /**
     * Instantiates a new Symbol table entry.
     *
     * @param name  the name
     * @param size  the size
     * @param scope the scope
     */
    public SymbolTableEntry(String name, int size, SymbolTable scope) {
        this.name = name;
        this.scope = scope;

        this.address = SymbolTableEntry.used_add;
        SymbolTableEntry.used_add += size;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public int getAddress() {
        return address;
    }

    /**
     * Gets scope.
     *
     * @return the scope
     */
    public SymbolTable getScope() {
        return scope;
    }

    // TODO setScope on optimization
}
