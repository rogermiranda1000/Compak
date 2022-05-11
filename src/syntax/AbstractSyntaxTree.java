package syntax;

import entities.Token;
import entities.TokenDataPair;
import intermediateCode.IntermediateCodeData;
import intermediateCode.IntermediateCodeGenerator;
import intermediateCode.ThreeAddressLine;

import java.util.*;

public class AbstractSyntaxTree {
    private List<Object> treeExtend;
    private AbstractSyntaxTree father;
    private static int t;
    private int level;
    private int height;
    private TokenDataPair operation;
    private int id;

    public AbstractSyntaxTree() {
        this.treeExtend = new ArrayList<>();
    }

    public AbstractSyntaxTree(ParseTree parseTree) {
        this.treeExtend = new ArrayList<>();
        cloneTree(parseTree);
    }

    private void cloneTree(ParseTree parseTree) {
        for (int i = 0; i < parseTree.getTreeExtend().size(); i++) {
            Object o = parseTree.getTreeExtend().get(i);

            if (o instanceof ParseTree) {
                Object o2 = new AbstractSyntaxTree((ParseTree) o);
                ((AbstractSyntaxTree)o2).father = this;
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
                if (((AbstractSyntaxTree)o).treeExtend.size() == 1 && ((AbstractSyntaxTree)o).operation == null) {
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

    public void promoteOneLevelTokens() {
        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractSyntaxTree) {
                //((AbstractSyntaxTree)o).promoteOneLevelTokens();
            } else {
                // Promote un nivell
                Token tk = ((TokenDataPair) o).getToken();

                if (tk == Token.IF) {
                    ((TokenDataPair) o).setPromoted();
                    this.operation = new TokenDataPair(Token.END_IF, "end_if");

                    //new TokenDataPair(Token.WHILE, "while")
                    Object obj = this.treeExtend.get(1);
                    AbstractSyntaxTree newObject =  new AbstractSyntaxTree();
                    newObject.treeExtend.add(obj);
                    this.treeExtend.add(0, newObject);

                    this.treeExtend.remove(obj);
                    this.treeExtend.remove(o);

                    ((AbstractSyntaxTree) this.treeExtend.get(0)).operation = new TokenDataPair(Token.IF, "if");
                } else if (tk == Token.BUCLE) {
                    if (this.father != null && !((TokenDataPair) o).isPromoted()) {
                        // Case loop(for i in range (5)) - for
                        if (this.treeExtend.get(1) instanceof AbstractSyntaxTree && ((AbstractSyntaxTree) this.treeExtend.get(1)).operation == null) {
                            ((TokenDataPair) o).setPromoted();
                            this.operation = new TokenDataPair(Token.END_LOOP, "end_for");
                            ((AbstractSyntaxTree)this.treeExtend.get(1)).operation = new TokenDataPair(Token.FOR_IN, "range");
                            this.treeExtend.remove(o);
                        } else if (false) {
                            // Case loop(number)
                        } else {
                            // Case loop(boolean) - while
                            ((TokenDataPair) o).setPromoted();
                            this.operation = new TokenDataPair(Token.END_LOOP, "end_while");

                            //new TokenDataPair(Token.WHILE, "while")
                            Object obj = this.treeExtend.get(1);
                            AbstractSyntaxTree newObject =  new AbstractSyntaxTree();
                            newObject.treeExtend.add(obj);
                            this.treeExtend.add(0, newObject);

                            this.treeExtend.remove(obj);
                            this.treeExtend.remove(o);

                            ((AbstractSyntaxTree) this.treeExtend.get(0)).operation = new TokenDataPair(Token.WHILE, "while");
                        }
                    }
                } else if (i == 1 && tk == Token.ASSIGN) {
                    if (this.father != null && !((TokenDataPair) o).isPromoted()) {
                        ((TokenDataPair) o).setPromoted();
                        this.operation = ((TokenDataPair) o);
                        this.treeExtend.remove(o);
                    }
                } else if (tk == Token.SUM || tk == Token.SUBSTRACT || tk == Token.AND || tk == Token.OR || tk == Token.MULT || tk == Token.DIVIDE || tk == Token.ASSIGN
                        || tk == Token.GT || tk == Token.LT || tk == Token.COMP) {
                    if (this.father != null && !((TokenDataPair) o).isPromoted()) {
                        ((TokenDataPair) o).setPromoted();
                        this.father.operation = ((TokenDataPair) o);
                        this.treeExtend.remove(o);
                    }
                }
            }
        }
    }

    public void promoteTokens() {
        PriorityQueue<AbstractSyntaxTree> pq = new PriorityQueue<AbstractSyntaxTree>((a,b) -> b.height - a.height);

        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractSyntaxTree) {
                pq.add(((AbstractSyntaxTree)o));
            }
        }

        while (!pq.isEmpty()) {
            AbstractSyntaxTree tree = pq.remove();
            tree.promoteTokens();
            tree.promoteOneLevelTokens();
        }
    }

    public void calculateLevels() {
        this.calculateLevels(0);
    }

    private void calculateLevels(int level) {
        this.level = level;
        for (int i = 0; i < treeExtend.size(); i++) {
            Object o = treeExtend.get(i);
            if (o instanceof AbstractSyntaxTree) {
                ((AbstractSyntaxTree)o).calculateLevels(level+1);
            }
        }
    }

    public int calculateHeight() {
        int max = Integer.MIN_VALUE;
        int value;

        for (int i = 0; i < treeExtend.size(); i++) {
            Object o = treeExtend.get(i);
            if (o instanceof AbstractSyntaxTree) {
                value = ((AbstractSyntaxTree)o).calculateHeight();
                ((AbstractSyntaxTree)o).height = value;

                if (value > max) {
                    max = value;
                }
            }
        }

        if (max == Integer.MIN_VALUE) {
            return 0;
        }

        return max+1;
    }

    public void travelWithPriorityDepth(IntermediateCodeData intermediateCodeData) {
        Comparator<AbstractSyntaxTree> comparator = new Comparator<AbstractSyntaxTree>() {
            @Override
            public int compare(AbstractSyntaxTree a, AbstractSyntaxTree b) {
                if (a.operation == null) {
                    return 1;
                }

                if (b.operation == null) {
                    return 1;
                }

                if (a.operation.getToken() == Token.END_LOOP) {
                    return 100;
                }

                if (b.operation.getToken() == Token.END_LOOP) {
                    return 100;
                }

                if (a.operation.getToken() == Token.END_IF) {
                    return 100;
                }

                if (b.operation.getToken() == Token.WHILE) {
                    return 100;
                }

                if (b.operation.getToken() == Token.IF) {
                    return 100;
                }

                if (a.operation.getToken() == Token.BUCLE) {
                    return 1;
                }

                if (a.operation.getToken() == Token.IF) {
                    return 1;
                }

                if (a.operation.getToken() == Token.ASSIGN && b.operation.getToken() == Token.ASSIGN) {
                    return 0;
                }
                return b.height - a.height;
            }
        };

        PriorityQueue<AbstractSyntaxTree> pq = new PriorityQueue<AbstractSyntaxTree>(comparator);

        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractSyntaxTree) {
                pq.add(((AbstractSyntaxTree)o));
            }
        }

        while (!pq.isEmpty()) {
            AbstractSyntaxTree tree = pq.remove();
            tree.travelWithPriorityDepth(intermediateCodeData);

            if (tree.treeExtend.size() == 2) {
                tree.id = intermediateCodeData.addLine(tree.operation, tree.treeExtend.get(0), tree.treeExtend.get(1));
            } else {
                tree.id = intermediateCodeData.addLine(tree.operation, tree.treeExtend.get(0));
            }


            // debug line for 3@Code
            // System.out.println(tree + "->" + tree.treeExtend + "   op: " + tree.operation);
        }
    }

    public int getId() {
        return id;
    }

    public void recalculateFathers() {
        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractSyntaxTree) {
                ((AbstractSyntaxTree)o).father = this;
                ((AbstractSyntaxTree)o).recalculateFathers();
            }
        }
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
            if (this.operation == null) {
                buffer.append("⬜");
            } else {
                buffer.append(this.operation.toString());
            }

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
                    buffer.append(childrenPrefix).append("└── ").append(((TokenDataPair) o)).append("\n");
                } else {
                    buffer.append(childrenPrefix).append("├── ").append(((TokenDataPair) o)).append("\n");
                }
            }
        }
    }
}
