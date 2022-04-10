package syntax;

import entities.TokenDataPair;

import java.util.ArrayList;
import java.util.List;

public class AbstractSyntaxTree extends Tree {
    public AbstractSyntaxTree(ParseTree parseTree) {
        super();
        cloneTree(parseTree);
    }

    private void cloneTree(ParseTree parseTree) {
        for (int i = 0; i < parseTree.getTreeExtend().size(); i++) {
            Object o = parseTree.getTreeExtend().get(i);

            if (o instanceof ParseTree) {
                Object o2 = new AbstractSyntaxTree((ParseTree) o);
                treeExtend.add(o2);
            } else {
                treeExtend.add(o);
            }
        }
    }

    public void removeEpsilons(AbstractSyntaxTree father) {
        if (this.treeExtend.size() == 0) {
            father.treeExtend.remove(this);
        }

        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractSyntaxTree) {
                ((AbstractSyntaxTree)o).removeEpsilons(this);
            }
        }
    }

    public void removeEpsilons() {
        this.removeEpsilons(null);
    }

    public void removeRedundantProductions() {
        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractSyntaxTree) {
                ((AbstractSyntaxTree)o).removeRedundantProductions();
                if (((AbstractSyntaxTree)o).treeExtend.size() == 1) {
                    this.treeExtend.set(i, ((AbstractSyntaxTree)o).treeExtend.get(0));
                }
            }
        }
    }
}
