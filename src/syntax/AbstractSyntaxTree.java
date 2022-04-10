package syntax;

import entities.Token;
import entities.TokenDataPair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AbstractSyntaxTree extends Tree {
    // Remove nodes for tokens that don't add meaning. Those are intermediate keywords (like "then"), separators (like comma) and brackets (like parenthesis).
    // Promote meaningful tokens (like "if") to be the parent of other tokens in the same rule.
    Token operation = null;

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

    public void removeMeaningLessTokens() {
        Token[] list = new Token[] {Token.EOL, Token.INT, Token.BIG,Token.FLO,Token.STR,Token.BIT,Token.MAIN,Token.RET_TYPE,Token.IN,
                Token.RANGE,Token.OPN_CONTEXT,Token.CLS_CONTEXT,Token.OPN_PARENTH,Token.CLS_PARENTH,Token.COMMA,Token.FOR};

        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractSyntaxTree) {
                ((AbstractSyntaxTree)o).removeMeaningLessTokens();
            } else {
                if (((TokenDataPair)o).isMeaningLessToken(list)) {
                    this.treeExtend.remove(o);
                    i--;
                }
            }
        }
    }

    public void promoteTokens() {
        Queue<TokenDataPair> queue = new LinkedList<TokenDataPair>();

        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractSyntaxTree) {
                if (((AbstractSyntaxTree)o).treeExtend.get(0) instanceof TokenDataPair) {
                    Token token = ((TokenDataPair) ((AbstractSyntaxTree)o).treeExtend.get(0)).getToken();
                    if (token == Token.ASSIGN) {

                    }
                }
                ((AbstractSyntaxTree)o).promoteTokens();
            } else {
            }
        }


    }
}
