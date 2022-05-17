package syntax;

import entities.SymbolTable;
import entities.TokenDataPair;
import entities.VariableTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Class ParseTree. This class consists in a node that forms the parse tree of the language.
 */
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

    /**
     * Constructor ParseTree.
     *
     * @param production original production
     */
    public ParseTree(Production production) {
        this.originalProduction = production;
        this.treeExtend = new ArrayList<>();
    }

    /**
     * Gets original production.
     *
     * @return the original production
     */
    public Production getOriginalProduction() {
        return originalProduction;
    }

    /**
     * Gets tree extend.
     *
     * @return the tree extend
     */
    public List<Object> getTreeExtend() {
        return treeExtend;
    }

    /**
     * Add tree.
     *
     * @param o the o
     */
    public void addTree(Object o) {
        this.treeExtend.add(o);
    }

    /**
     * Get the tokens that make up this part of the tree
     *
     * @return tokens that have built the tree.
     */
    public List<TokenDataPair> getTokens() {
        List<TokenDataPair> r = new ArrayList<>();

        for (Object o : this.treeExtend) {
            if (o instanceof ParseTree) r.addAll(((ParseTree)o).getTokens());
            else r.add((TokenDataPair) o); // TokenDataPair
        }

        return r;
    }

    /**
     * Gets evaluates.
     *
     * @return the evaluates
     */
    public VariableTypes getEvaluates() {
        return this.evaluates;
    }

    /**
     * Sets evaluates.
     *
     * @param evaluates the evaluates
     */
    public void setEvaluates(VariableTypes evaluates) {
        this.evaluates = evaluates;
    }

    /**
     * Gets table.
     *
     * @return the table
     */
    public SymbolTable getTable() {
        return table;
    }

    /**
     * Sets table.
     *
     * @param table the table
     */
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
}
