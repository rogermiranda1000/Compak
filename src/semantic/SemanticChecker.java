package semantic;

import entities.*;
import syntax.AbstractSyntaxTree;

import java.util.List;

public class SemanticChecker {
    public static void recursive(Object pseudoRoot) throws SemanticException {
        if(pseudoRoot instanceof AbstractSyntaxTree){
            if (((AbstractSyntaxTree) pseudoRoot).getOperation() != null && ((AbstractSyntaxTree) pseudoRoot).getOperation().getToken() == Token.ASSIGN) {
                SemanticChecker.recursiveSemantic(pseudoRoot);
            }
            for(Object node : ((AbstractSyntaxTree) pseudoRoot).getTreeExtend()) recursive(node);
        }
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

        if (!left.equals(right)) {
            throw new SemanticException("ERROR sintaxis");
        }
        return left;
    }

    public static void check(AbstractSyntaxTree abstractSyntaxTree) throws SemanticException {
        recursive(abstractSyntaxTree);
    }
}
