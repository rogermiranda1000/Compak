package syntax;

import entities.SymbolTable;
import entities.SymbolTable;
import entities.TokenDataPair;
import entities.VariableTypes;
import entities.VariableTypes;

import java.util.ArrayList;
import java.util.List;

public class ParseTree {
    private final Production originalProduction;

    private SymbolTable table;

    /**
     * The productions inside this production evaluates to this type (UNKNOWN if it's not computed yet)
     */
    private VariableTypes evaluates;

    /**
     * Array of AbstractTreeNode or TokenDataPair
     */
    private final List<Object> treeExtend;

    public ParseTree(Production production) {
        this.originalProduction = production;
        this.treeExtend = new ArrayList<>();
    }

    public Production getOriginalProduction() {
        return originalProduction;
    }

    public List<Object> getTreeExtend() {
        return treeExtend;
    }

    public void addTree(Object o) {
        this.treeExtend.add(o);
    }

    public VariableTypes getEvaluates() {
        return this.evaluates;
    }

    public void setEvaluates(VariableTypes evaluates) {
        this.evaluates = evaluates;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Object o : this.treeExtend) {
            if (o instanceof ParseTree) sb.append(((ParseTree)o).toString());
            else sb.append(((TokenDataPair)o).getToken().name());
            sb.append(' ');
        }
        return sb.toString();
    }

    public void printTree() {
        System.out.println(this.printTree(new StringBuilder(), "").toString());
    }

    private StringBuilder printTree(StringBuilder buffer, String childrenPrefix) {
        if (this.treeExtend.size() == 0) {
            buffer.append("EPSILON\n");
            return buffer;
        }
        buffer.append("⬜\n");

        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof ParseTree) {
                if (i == this.treeExtend.size()-1) {
                    buffer.append(childrenPrefix).append("└── ");
                    ((ParseTree)o).printTree(buffer, childrenPrefix + "    ");
                } else {
                    buffer.append(childrenPrefix).append("├── ");
                    ((ParseTree)o).printTree(buffer, childrenPrefix + "│   ");
                }
            } else {
                if (i == this.treeExtend.size()-1) {
                    buffer.append(childrenPrefix).append("└── ").append(((TokenDataPair) o)).append("\n");
                } else {
                    buffer.append(childrenPrefix).append("├── ").append(((TokenDataPair) o)).append("\n");
                }
            }
        }
        return buffer;
    }
}
