package semantic;

import entities.*;
import syntax.AbstractSyntaxTree;

public class SemanticChecker {
    public static void recursive(Object pseudoRoot) throws SemanticException {
        //System.out.println("DEBUG: " + ((AbstractSyntaxTree) pseudoRoot).getOperation().getToken());
        if(pseudoRoot instanceof AbstractSyntaxTree){
            if (((AbstractSyntaxTree) pseudoRoot).getOperation() != null && ((AbstractSyntaxTree) pseudoRoot).getOperation().getToken() == Token.ASSIGN) {
                if(recursiveSemantic(pseudoRoot).equals("ERROR")) System.out.println("ERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRROR");
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

        VariableTypes left = recursiveSemantic(((AbstractSyntaxTree) pseudoRoot).getTreeExtend().get(0)),
                    right = recursiveSemantic(((AbstractSyntaxTree) pseudoRoot).getTreeExtend().get(1));

        if (!left.equals(right)) {
            throw new SemanticException("ERROR sintaxis");
        }
        return left;
    }

    public static void check(AbstractSyntaxTree abstractSyntaxTree) throws SemanticException {
        recursive(abstractSyntaxTree);
    }
}
