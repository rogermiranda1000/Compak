package syntax;

import entities.TokenDataPair;

import java.util.ArrayList;
import java.util.List;

public class AbstractTreeNode {
    private final Production originalProduction;

    /**
     * Array of AbstractTreeNode or TokenDataPair
     */
    private final List<Object> treeExtend;

    public AbstractTreeNode(Production production) {
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

    /**
     * Obtè els tokens que formen aquesta part de l'arbre
     * @return Tokens que han construit l'arbre
     */
    public List<TokenDataPair> getTokens() {
        List<TokenDataPair> r = new ArrayList<>();

        for (Object o : this.treeExtend) {
            if (o instanceof AbstractTreeNode) r.addAll(((AbstractTreeNode)o).getTokens());
            else r.add((TokenDataPair) o); // TokenDataPair
        }

        return r;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Object o : this.treeExtend) {
            if (o instanceof AbstractTreeNode) sb.append(((AbstractTreeNode)o).toString());
            else sb.append(((TokenDataPair)o).getToken().name());
            sb.append(' ');
        }
        return sb.toString();
    }
}
