package syntax;

import entities.Token;
import entities.TokenDataPair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Object o : this.treeExtend) {
            if (o instanceof AbstractTreeNode){
                if (((AbstractTreeNode) o).treeExtend.size() == 0) {
                    sb.append("EPSILON ");
                } else {
                    sb.append("\t").append(((AbstractTreeNode)o).toString());
                }
            } else {
                sb.append(((TokenDataPair) o).getToken().name()).append(" ");
            }
        }
        return sb.toString();
    }

    public String toTreeString() {

        Queue<AbstractTreeNode> queue = new LinkedList<AbstractTreeNode>();

        queue.add(this);
        do {
            StringBuilder sb = new StringBuilder();
            AbstractTreeNode tree = queue.poll();
            for (Object o : tree.treeExtend) {
                if (o instanceof AbstractTreeNode) {
                    sb.append("PRODUCTION ");
                    queue.add((AbstractTreeNode)o);
                } else {
                    sb.append(((TokenDataPair)o).getToken().name()).append(' ');
                }
            }
            System.out.println(sb.toString());
            sb.append(" ");
        } while (!queue.isEmpty());

        return " ";
    }

    public void printTree(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        // print name ?
        if (this.treeExtend.size() == 0) {
            buffer.append("EPSILON");
        } else {
            buffer.append("PRODUCTION");
        }
        buffer.append('\n');

        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractTreeNode){
                if (i == this.treeExtend.size()-1) {
                    ((AbstractTreeNode)o).printTree(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
                } else {
                    ((AbstractTreeNode)o).printTree(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
                }
            } else {
                if (i == this.treeExtend.size()-1) {
                    buffer.append(childrenPrefix).append("└── ").append(((TokenDataPair) o).getToken().name()).append("\n");
                } else {
                    buffer.append(childrenPrefix).append("├── ").append(((TokenDataPair) o).getToken().name()).append("\n");
                }
            }
        }
    }

    public void removeEpsilons(AbstractTreeNode father) {
        if (this.treeExtend.size() == 0) {
            father.treeExtend.remove(this);
        }

        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractTreeNode) {
                ((AbstractTreeNode)o).removeEpsilons(this);
            }
        }
    }

    public void removeRedundantProductions() {
        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractTreeNode) {
                ((AbstractTreeNode)o).removeRedundantProductions();
                if (((AbstractTreeNode)o).treeExtend.size() == 1) {
                    this.treeExtend.set(i, ((AbstractTreeNode)o).treeExtend.get(0));
                }
            }
        }
    }
}
