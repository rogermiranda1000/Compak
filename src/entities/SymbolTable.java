package entities;

import java.util.Set;
import java.util.TreeSet;

public class SymbolTable {
    private final Set<SymbolTableEntries> entries = new TreeSet<>((SymbolTableEntries o1, SymbolTableEntries o2) -> {
        if (o1 == o2) return 0;
        if (!o1.getName().equals(o2.getName())) return o1.getName().compareTo(o2.getName());
        return o2.getScope() - o1.getScope();
    });

    public SymbolTable() {}

    public void addEntry(SymbolTableEntries entry) throws DuplicateVariableException {
        if (!this.entries.add(entry)) throw new DuplicateVariableException("The variable '" + entry.getName() + "' already exists in this scope!");
    }
}
