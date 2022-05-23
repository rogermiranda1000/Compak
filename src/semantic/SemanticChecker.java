package semantic;

import entities.*;
import syntax.AbstractSyntaxTree;

import java.util.List;

public class SemanticChecker {
    public static void recursive(Object pseudoRoot) throws SemanticException {
        if(pseudoRoot instanceof AbstractSyntaxTree) {
            if (((AbstractSyntaxTree) pseudoRoot).getOperation() != null) {
                Token operation = ((AbstractSyntaxTree) pseudoRoot).getOperation().getToken();
                if (operation == Token.ASSIGN) {
                    SemanticChecker.recursiveSemantic(pseudoRoot);
                    return;
                } else if (operation == Token.IF) {
                    AbstractSyntaxTree clone = new AbstractSyntaxTree((AbstractSyntaxTree)pseudoRoot);
                    clone.addElementToTreeExtend(new TokenDataPair(Token.BIT)); // ifs only have one son
                    SemanticChecker.recursiveSemantic(clone);
                    return;
                }
            }

            for(Object node : ((AbstractSyntaxTree) pseudoRoot).getTreeExtend()) recursive(node);
        }
    }

    /**
     * Operacions conmutatives:
     * int [+ / - / * / '/'] int -> int
     * int ^ int -> big
     * int [+ / - / * / '/' / ^] big -> big
     * big [+ / - / * / '/' / ^] big -> big
     * int [+ / - / * / '/' / ^] lf -> lf
     * big [+ / - / * / '/' / ^] lf -> lf
     * lf [+ / - / * / '/' / ^] lf -> lf
     * str [+ / *] str -> str
     * int [== / < / >] int -> bit
     * big [== / < / >] big -> bit
     * lf [== / < / >] lf -> bit
     * str == str -> bit
     * bit [| / & / == / < / > / ¡] bit -> bit
     *
     * Cast conmutatiu [TODO]:
     * int -> big
     * int -> lf
     * bit -> int
     *
     * Operacions no conmutatives:
     * int % int -> int
     * int % big -> int
     * big % int -> big
     * big % big -> big
     */
    private static VariableTypes evaluate(VariableTypes a, VariableTypes b, Token operation) throws SemanticException {
        if (operation == Token.MOD) {
            if (a == VariableTypes.INT) {
                if (b == VariableTypes.INT || b == VariableTypes.BIG) return VariableTypes.INT;
                throw new SemanticException("Expected int % [int/big], found int % " + b.name());
            }
            else if (a == VariableTypes.BIG) {
                if (b == VariableTypes.INT || b == VariableTypes.BIG) return VariableTypes.BIG;
                throw new SemanticException("Expected big % [int/big], found big % " + b.name());
            }
            throw new SemanticException("Factorial only works with 'int' and 'big' (found " + a.name() + ")");
        }

        if (operation == Token.COMP || operation == Token.LT || operation == Token.GT) {
            if (a == b && (a != VariableTypes.STR || operation == Token.COMP)) return VariableTypes.BIT;
            throw new SemanticException("Comparing " + a.name() + " with " + b.name());
        }

        if (operation == Token.NOT || operation == Token.OR || operation == Token.AND) {
            if (a == b && a == VariableTypes.BIT) return a;
            throw new SemanticException("Bit comparison between " + a.name() + " with " + b.name());
        }

        if ((a == VariableTypes.STR || b == VariableTypes.STR) && (operation == Token.SUM || operation == Token.SUBSTRACT)) {
            if (a != b) throw new SemanticException("Expected str " + operation.name() + " str, found " + a.name() + " " + operation.name() + " " + b.name());
            return a;
        }

        if (operation == Token.SUM || operation == Token.SUBSTRACT || operation == Token.MULT || operation == Token.DIVIDE) {
            if (a == VariableTypes.FLO || b == VariableTypes.FLO) return VariableTypes.FLO;
            if (a == VariableTypes.BIG || b == VariableTypes.BIG) return VariableTypes.BIG;
            return VariableTypes.INT;
        }

        if (operation == Token.ASSIGN || operation == Token.IF /* b is BIT for sure */) {
            if (a == b) return a;
            throw new SemanticException("Assigning " + a.name() + " variable with " + b.name() + " type");
        }

        if (operation == null && a == b) return a;
        throw new SemanticException("Unsupported operation: " + a.name() + " " + (operation != null ? operation.name() : "null") + " " + b.name());
    }

    /**
     * int! -> big
     * big! -> big
     */
    private static VariableTypes evaluate(VariableTypes a, Token operation) throws SemanticException {
        if (operation == Token.FACT) {
            if (a == VariableTypes.INT || a == VariableTypes.BIG) return VariableTypes.BIG;
            throw new SemanticException("Factorial only works with 'int' and 'big' types (found " + a.name() + ")");
        }
        return a;
    }

    public static VariableTypes recursiveSemantic(Object pseudoRoot) throws SemanticException {
        if(pseudoRoot instanceof TokenDataPair) {
            TokenDataPair data = (TokenDataPair) pseudoRoot;
            if (data.getToken() == Token.ID) return ((SymbolTableVariableEntry)data.getVariableNode()).getType();
            return data.getToken().getType();
        }

        List<Object> sons = ((AbstractSyntaxTree) pseudoRoot).getTreeExtend();
        VariableTypes left = recursiveSemantic(sons.get(0)),
                    right = recursiveSemantic(sons.get(1));

        return evaluate(left, right, ((AbstractSyntaxTree) pseudoRoot).getOperation() != null ? ((AbstractSyntaxTree) pseudoRoot).getOperation().getToken() : null);
    }

    public static void check(AbstractSyntaxTree abstractSyntaxTree) throws SemanticException {
        recursive(abstractSyntaxTree);
    }
}
