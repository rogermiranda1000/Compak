package syntax;

import entities.TokenDataPair;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    protected final List<Object> treeExtend;

    public Tree() {
        this.treeExtend = new ArrayList<>();
    }

    public void printTree() {
        StringBuilder sb = new StringBuilder();
        printTree(sb, "", "");
        System.out.println(sb);
    }

    protected void printTree(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        if (this.treeExtend.size() == 0) {
            buffer.append("EPSILON");
        } else {
            buffer.append("PRODUCTION");
        }
        buffer.append('\n');

        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractSyntaxTree){
                if (i == this.treeExtend.size()-1) {
                    ((AbstractSyntaxTree)o).printTree(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
                } else {
                    ((AbstractSyntaxTree)o).printTree(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
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
}
