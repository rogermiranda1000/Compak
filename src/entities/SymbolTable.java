package entities;

import org.jetbrains.annotations.Nullable;
import syntax.AbstractTreeNode;

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
    private SymbolTable parent;

    private final List<SymbolTable> subtables;

    private final List<AbstractTreeNode> nodes;

    public SymbolTable(AbstractTreeNode node, SymbolTable parent) {
        this.parent = parent;
        this.subtables = new ArrayList<>();
        this.nodes = new ArrayList<>();
        if (node != null) this.nodes.add(node);
    }

    public SymbolTable() {
        this(null, null);
    }

    public void addEntry(SymbolTableEntries entry) throws DuplicateVariableException {
        if (!this.entries.add(entry)) throw new DuplicateVariableException("The variable '" + entry.getName() + "' already exists in this scope!");
    }

    public void addSubtable(SymbolTable symbolTable) {
        if (symbolTable.isUsed()) this.subtables.add(symbolTable); // first-phase optimization
        else this.nodes.addAll(symbolTable.nodes);
    }

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
            this.parent.nodes.addAll(this.nodes);
        }
        return this;
    }

    public SymbolTable getParent() {
        return this.parent;
    }

    public boolean isUsed() {
        return this.entries.size() > 0 || this.subtables.size() > 0;
    }

    public void apply() {
        for (AbstractTreeNode node : this.nodes) node.setTable(this);
        for (SymbolTable table : this.subtables) table.apply();
    }
}
