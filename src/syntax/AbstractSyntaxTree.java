package syntax;

import entities.Token;
import entities.TokenDataPair;
import entities.ThreeAddressLine;

import java.util.*;

/**
 * Class AbstractSyntaxTree.
 */
public class AbstractSyntaxTree {
    private final List<Object> treeExtend;
    private AbstractSyntaxTree father;
    private int height;
    private TokenDataPair operation;
    private int id;

    public TokenDataPair getOperation() {
        return operation;
    }

    public void setOperation(TokenDataPair operation) {
        this.operation = operation;
    }

    private static ArrayList<ThreeAddressLine> treeInLines;
    private static int globalId;

    /**
     * Class constructor to instantiates new AST empty.
     */
    public AbstractSyntaxTree() {
        this.treeExtend = new ArrayList<>();
    }

    public AbstractSyntaxTree(AbstractSyntaxTree that) {
        this.treeExtend = new ArrayList<>(that.treeExtend);
        this.father = that.father;
        this.height = that.height;
        this.operation = that.operation;
        this.id = that.id;
    }

    /**
     * Getter of static arraylist treeInLines (TAC lines)
     * @return arraylist treeInLines
     */
    public static ArrayList<ThreeAddressLine> getTreeInLines() {
        return treeInLines;
    }

    /**
     * Class constructor to instantiates new AST cloning the parse tree.
     *
     * @param parseTree the parse tree to clone
     */
    public AbstractSyntaxTree(ParseTree parseTree) {
        this.treeExtend = new ArrayList<>();

        if (treeInLines == null) {
            treeInLines = new ArrayList<>();
            globalId = 0;
        }

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

    /**
     * Function that removes epsilons from productions.
     */
    public void removeEpsilons() {
        this.removeEpsilons(null);
    }

    private void removeEpsilons(AbstractSyntaxTree father) {
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

    /**
     * Function that removes redundant productions from AST.
     */
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

    /**
     * Function that removes meaningless tokens from AST. In parse tree we consider all tokens, but not AST.
     */
    public void removeMeaningLessTokens() {
        Token[] list = new Token[] {Token.EOL, Token.RET_TYPE,Token.IN, Token.INT, Token.BIG, Token.BIT, Token.FLO,
                Token.RANGE,Token.OPN_CONTEXT,Token.CLS_CONTEXT,Token.OPN_PARENTH,Token.CLS_PARENTH,Token.COMMA,Token.FOR,Token.ARROW};

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

    /**
     * Function that promotes tokens to operations node. In parse tree we have at same level operations and arguments.
     * In AST we have operations one level higher than arguments.
     */
    public void promoteTokens() {
        PriorityQueue<AbstractSyntaxTree> pq = new PriorityQueue<AbstractSyntaxTree>((a,b) -> b.height - a.height);

        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractSyntaxTree) {
                pq.add(((AbstractSyntaxTree)o));
            } else {
                if (!((TokenDataPair)o).isPromoted() && ((TokenDataPair)o).getToken().equals(Token.MAIN)) {
                    AbstractSyntaxTree newEntry = new AbstractSyntaxTree();
                    TokenDataPair tokn = new TokenDataPair(Token.MAIN);
                    tokn.setPromoted();
                    newEntry.operation = new TokenDataPair(Token.MAIN);
                    newEntry.treeExtend.add(tokn);
                    this.treeExtend.add(i, newEntry);
                    this.treeExtend.remove(o);
                    pq.add(newEntry);
                }
            }
        }

        while (!pq.isEmpty()) {
            AbstractSyntaxTree tree = pq.remove();
            tree.promoteTokens();
            tree.promoteTokensToOperation();
        }
    }

    private boolean containsSon(Token token) {
        for (Object o : this.treeExtend) {
            if (o instanceof TokenDataPair && ((TokenDataPair)o).getToken().equals(token)) return true;
        }
        return false;
    }

    private void promoteTokensToOperation() {
        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractSyntaxTree) {
                //((AbstractSyntaxTree)o).promoteOneLevelTokens();
            } else {
                // Promote un nivell
                Token tk = ((TokenDataPair) o).getToken();

                if (tk == Token.IF) {
                    ((TokenDataPair) o).setPromoted();

                    AbstractSyntaxTree condition = new AbstractSyntaxTree();
                    condition.operation = (TokenDataPair) this.treeExtend.get(0);
                    condition.treeExtend.add(this.treeExtend.remove(1));
                    this.treeExtend.set(0, condition);

                    this.operation = new TokenDataPair(Token.END_IF, "end_if");

                    if (!this.father.containsSon(Token.ELSE)) {
                        // create the else node
                        AbstractSyntaxTree ifNode = new AbstractSyntaxTree();
                        ifNode.operation = this.operation;
                        ifNode.treeExtend.addAll(this.treeExtend);

                        this.treeExtend.clear();
                        this.treeExtend.add(ifNode);
                        this.operation = new TokenDataPair(Token.END_ELSE, "end_else");
                    }
                }
                else if (tk == Token.ELSE) {
                    ((TokenDataPair) o).setPromoted();

                    AbstractSyntaxTree elseNode = new AbstractSyntaxTree();
                    elseNode.operation = (TokenDataPair) o;
                    elseNode.treeExtend.add(o);
                    this.treeExtend.set(i, elseNode); // treeExtend[i] is ELSE
                    this.operation = new TokenDataPair(Token.END_ELSE, "end_else");
                }
                else if (tk == Token.BUCLE) {
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
                } else if (tk == Token.NOT) {
                    this.operation = ((TokenDataPair) o);
                    this.treeExtend.remove(o);
                } else if (tk == Token.FUNC || tk == Token.AF) {
                    ((TokenDataPair) o).setPromoted();
                    this.operation = ((TokenDataPair) o);
                    this.treeExtend.remove(o);

                    // New production to save name
                    AbstractSyntaxTree newObject =  new AbstractSyntaxTree();
                    newObject.operation = new TokenDataPair(Token.NAME_FUNC, "name_func");
                    Object obj = treeExtend.get(0);
                    newObject.treeExtend.add(obj);
                    treeExtend.remove(obj);
                    this.treeExtend.add(0, newObject);

                    // New production to save vars
                    AbstractSyntaxTree newObject2 =  new AbstractSyntaxTree();
                    newObject2.operation = new TokenDataPair(Token.PARAMS, "params");

                    //newObject2.treeExtend.add(new TokenDataPair(Token.EPSILON));
                    int index_start_func = 1;
                    if (treeExtend.size() > 1 && treeExtend.get(1) instanceof TokenDataPair) {
                        // add to params
                        index_start_func++;
                        newObject2.treeExtend.add((treeExtend.get(1)));
                        treeExtend.remove(treeExtend.get(1));
                        this.treeExtend.add(1, newObject2);
                    }

                    // New production to save vars
                    AbstractSyntaxTree newObject3 =  new AbstractSyntaxTree();
                    newObject3.operation = new TokenDataPair(Token.START_FUNC, "start_func");

                    boolean flagEmpty = true;
                    for (int j = index_start_func; j < treeExtend.size(); j++) {
                        newObject3.treeExtend.add(treeExtend.get(j));
                        treeExtend.remove(treeExtend.get(j));
                        flagEmpty = false;
                        j--;
                    }

                    if (flagEmpty) {
                        newObject3.treeExtend.add(new TokenDataPair(Token.INT));
                    }
                    this.treeExtend.add(newObject3);
                } else if (tk == Token.ID_FUNC) {
                    // case call 1 parameter
                    if (this.treeExtend.size() == 2 && this.treeExtend.get(0) instanceof TokenDataPair &&
                            ((TokenDataPair) this.treeExtend.get(0)).getToken().equals(Token.ID_FUNC) &&
                            !(this.treeExtend.get(1) instanceof AbstractSyntaxTree)) {
                        this.operation = new TokenDataPair(Token.CALL_FUNC);
                    } else {
                        // case call 0 parameters
                        if (this.father != null && !((TokenDataPair) o).isPromoted()) {
                            ((TokenDataPair) o).setPromoted();
                            AbstractSyntaxTree newObj = new AbstractSyntaxTree();
                            newObj.treeExtend.add(o);
                            newObj.operation = new TokenDataPair(Token.CALL_FUNC);
                            this.treeExtend.add(i, newObj);
                            this.treeExtend.remove(o);
                        }
                    }
                } else if (tk == Token.RETURN) {
                    ((TokenDataPair) o).setPromoted();
                    this.operation = ((TokenDataPair) o);
                    this.treeExtend.remove(o);
                }
            }
        }
    }

    /**
     * Function that calculate levels from tree to use it to travel with priority.
     */
    public void calculateLevels() {
        this.calculateLevels(0);
    }

    private void calculateLevels(int level) {
        for (int i = 0; i < treeExtend.size(); i++) {
            Object o = treeExtend.get(i);
            if (o instanceof AbstractSyntaxTree) {
                ((AbstractSyntaxTree)o).calculateLevels(level+1);
            }
        }
    }

    /**
     * If-else structure is different.
     *
     * From:
     * ⬜
     * ├── IF (if)
     * ├── TRUE (true)
     * ├── ⬜
     * └── ⬜
     *     ├── ELSE (else)
     *     └── ⬜
     *
     *  To:
     *  ⬜
     *  └── ⬜
     *  |   ├── IF (if)
     *  |   ├── TRUE (true)
     *  |   └── ⬜
     *  ├── ELSE
     *  └── ⬜
     *
     */
    public void prepareIf() {
        // checks if it's an if condition and if it has an else
        boolean haveElse = (this.treeExtend.size() > 3 && this.treeExtend.get(0) instanceof TokenDataPair
                                && ((TokenDataPair)this.treeExtend.get(0)).getToken() == Token.IF
                                && this.treeExtend.get(3) instanceof AbstractSyntaxTree
                                && ((AbstractSyntaxTree)this.treeExtend.get(3)).treeExtend.size() > 1
                                && ((AbstractSyntaxTree)this.treeExtend.get(3)).treeExtend.get(0) instanceof TokenDataPair
                                && ((AbstractSyntaxTree)this.treeExtend.get(3)).treeExtend.get(1) instanceof AbstractSyntaxTree
                                && ((TokenDataPair)((AbstractSyntaxTree)this.treeExtend.get(3)).treeExtend.get(0)).getToken() == Token.ELSE);

        if (haveElse) {
            AbstractSyntaxTree ifNode = new AbstractSyntaxTree();
            List<Object> ifCondition = new ArrayList<>(this.treeExtend);
            AbstractSyntaxTree elseNode = (AbstractSyntaxTree)ifCondition.remove(3);
            ifNode.treeExtend.addAll(ifCondition);

            this.treeExtend.clear();
            this.treeExtend.add(ifNode);
            this.treeExtend.addAll(elseNode.treeExtend);
        }

        for (Object son : this.treeExtend) {
            if (son instanceof AbstractSyntaxTree) ((AbstractSyntaxTree)son).prepareIf();
        }
    }

    /**
     * Function that calculate height from tree to use it to travel with priority.
     *
     * @return height
     */
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

    /**
     * Function that travel all AST with a priority defined by our language and add to a static array each line
     * of 3 address code.
     */
    public void travelWithPriorityDepth() {
        Comparator<AbstractSyntaxTree> comparator = new Comparator<AbstractSyntaxTree>() {
            @Override
            public int compare(AbstractSyntaxTree a, AbstractSyntaxTree b) {
                if (a.operation == null) {
                    return 1;
                }

                if (b.operation == null) {
                    return 1;
                }

                if (b.operation.getToken() == Token.MAIN) {
                    return 100;
                }

                if (b.operation.getToken() == Token.NAME_FUNC) {
                    return 100;
                }

                if (a.operation.getToken() == Token.START_FUNC) {
                    return 100;
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

                if (a.operation.getToken() == Token.END_ELSE) {
                    return 100;
                }

                if (b.operation.getToken() == Token.WHILE) {
                    return 100;
                }

                if (b.operation.getToken() == Token.IF) {
                    return 100;
                }

                if (b.operation.getToken() == Token.ELSE) {
                    return 100;
                }

                if (a.operation.getToken() == Token.BUCLE) {
                    return 1;
                }

                if (a.operation.getToken() == Token.IF) {
                    return 1;
                }

                if (a.operation.getToken() == Token.ELSE) {
                    return 1;
                }

                if (a.operation.getToken() == Token.ASSIGN && b.operation.getToken() == Token.ASSIGN) {
                    return 0;
                }

                // Bug dos trees diferent altura
                if (a.father.operation == null) {
                    return 100;
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
            tree.travelWithPriorityDepth();

            if (tree.treeExtend.size() == 2) {
                addLine(tree.operation, tree.treeExtend.get(0), tree.treeExtend.get(1));
            } else {
                addLine(tree.operation, tree.treeExtend.get(0)); // tree.treeExtend.size() == 1
            }

            tree.id = globalId;
            globalId++;


            // debug line for 3@Code
            // System.out.println(tree + "->" + tree.treeExtend + "   op: " + tree.operation);
        }
    }

    private void addLine(TokenDataPair op, Object arg1, Object arg2) {
        treeInLines.add(new ThreeAddressLine(op, arg1, arg2));
    }

    private void addLine(TokenDataPair op, Object arg1) {
        treeInLines.add(new ThreeAddressLine(op, arg1, null));
    }

    /**
     * Function that recalculates father's id at modify AST nodes.
     */
    public void recalculateFathers() {
        for (int i = 0; i < this.treeExtend.size(); i++) {
            Object o = this.treeExtend.get(i);

            if (o instanceof AbstractSyntaxTree) {
                ((AbstractSyntaxTree)o).father = this;
                ((AbstractSyntaxTree)o).recalculateFathers();
            }
        }
    }

    /**
     * Function that prints AST.
     */
    public void printTree() {
        StringBuilder sb = new StringBuilder();
        printTree(sb, "", "");
        System.out.println(sb);
    }

    private void printTree(StringBuilder buffer, String prefix, String childrenPrefix) {
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

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    public List<Object> getTreeExtend() {
        return this.treeExtend;
    }

    public void addElementToTreeExtend(Object o) {
        this.treeExtend.add(o);
    }
}
