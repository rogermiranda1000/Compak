package syntax;

import entities.TokenDataPair;

import java.util.ArrayList;
import java.util.List;

public class ParseTree extends Tree {
    public ParseTree() {
        super();
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
            if (o instanceof ParseTree) sb.append(((ParseTree)o).toString());
            else sb.append(((TokenDataPair)o).getToken().name());
            sb.append(' ');
        }
        return sb.toString();
    }
}
