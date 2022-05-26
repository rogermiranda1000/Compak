package entities;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class Symbol Table.
 */
public class SymbolTable {
    private final Set<SymbolTableEntry> entries = new TreeSet<>((SymbolTableEntry o1, SymbolTableEntry o2) -> {
        if (o1 == o2) return 0;
        if (!o1.getName().equals(o2.getName())) return o1.getName().compareTo(o2.getName());
        if (!o1.getScope().equals(o2.getScope())) return 1;
        return o1.getClass().equals(o2.getClass()) ? 0 : 1;
    });

    @Nullable
    private SymbolTable parent;

    private final List<SymbolTable> subtables;

    /**
     * Instantiates a new Symbol table.
     *
     * @param parent the parent
     */
    public SymbolTable(@Nullable SymbolTable parent) {
        this.parent = parent;
        this.subtables = new ArrayList<>();
    }

    /**
     * Instantiates a new Symbol table without parent.
     */
    public SymbolTable() {
        this(null);
    }

    /**
     * Add entry.
     *
     * @param entry the entry
     * @throws DuplicateVariableException the duplicate variable exception
     */
    public void addEntry(SymbolTableEntry entry) throws DuplicateVariableException {
        if (!this.entries.add(entry)) throw new DuplicateVariableException("The variable '" + entry.getName() + "' already exists in this scope!");
    }

    public List<SymbolTable> getSubtables() {
        return this.subtables;
    }

    /**
     * Search in the current scope (and above) and returns the node with the same name (if any)
     *
     * @param token Variable
     * @return Node representing that variable
     */
    @Nullable
    public SymbolTableEntry searchEntry(TokenDataPair token) {
        SymbolTable table = this;
        while (table != null) {
            for (SymbolTableEntry e : table.entries) {
                if (e.getName().equals(token.getData()) && ((token.getToken() == Token.ID && e instanceof SymbolTableVariableEntry) || (token.getToken() == Token.ID_FUNC && e instanceof SymbolTableFunctionEntry))) return e;
            }

            // TODO explorar subtaules

            table = table.parent; // not found, maybe upper?
        }
        return null; // not found
    }

    /**
     * Optimize the symbol table.
     *
     * @return the symbol table optimized
     */
    public SymbolTable optimize() {
        List<SymbolTable> subtableClone = new ArrayList<>(this.subtables);
        for (SymbolTable table : subtableClone) table.optimize();
        if (this.entries.size() == 0 && this.subtables.size() == 1) {
            // you can remove this table
            if (this.parent == null) {
                SymbolTable r = this.subtables.get(0);
                r.parent = null;
                return r;
            }

            this.parent.subtables.remove(this);
            this.parent.subtables.add(this.subtables.get(0));
            this.subtables.get(0).parent = this.parent;
        }
        return this;
    }

    /**
     * Add a subtable.
     *
     * @param symbolTable the symbol table
     */
    public void addSubtable(SymbolTable symbolTable) {
        if (symbolTable.isUsed()) this.subtables.add(symbolTable); // first-phase optimization
    }

    /**
     * Get parent.
     *
     * @return the parent
     */
    @Nullable
    public SymbolTable getParent() {
        return this.parent;
    }

    /**
     * Is used.
     *
     * @return the boolean
     */
    public boolean isUsed() {
        return this.entries.size() > 0 || this.subtables.size() > 0;
    }
}
