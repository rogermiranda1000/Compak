package entities;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SymbolTable {
    private final Set<SymbolTableEntries> entries = new TreeSet<>((SymbolTableEntries o1, SymbolTableEntries o2) -> {
        if (o1 == o2) return 0;
        if (!o1.getName().equals(o2.getName())) return o1.getName().compareTo(o2.getName());
        if (!o1.getScope().equals(o2.getScope())) return 1;
        return o1.getClass().equals(o2.getClass()) ? 0 : 1;
    });

    @Nullable
    private final SymbolTable parent;

    private final List<SymbolTable> subtables;

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
        this.subtables = new ArrayList<>();
    }

    public SymbolTable() {
        this(null);
    }

    public void addEntry(SymbolTableEntries entry) throws DuplicateVariableException {
        if (!this.entries.add(entry)) throw new DuplicateVariableException("The variable '" + entry.getName() + "' already exists in this scope!");
    }

    public void addSubtable(SymbolTable symbolTable) {
        this.subtables.add(symbolTable);
    }

    public SymbolTable getParent() {
        return this.parent;
    }

    public boolean isUsed() {
        return this.entries.size() > 0 || this.subtables.size() > 0;
    }
}
