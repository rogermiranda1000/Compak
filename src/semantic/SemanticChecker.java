package semantic;

import entities.Token;
import entities.TokenDataPair;
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
    public static Token recursiveSemantic(Object pseudoRoot) throws SemanticException {

        if(pseudoRoot instanceof TokenDataPair) {
            return (((TokenDataPair) pseudoRoot).getToken());
        }

        Token left = recursiveSemantic(((AbstractSyntaxTree) pseudoRoot).getTreeExtend().get(0));
        Token right = recursiveSemantic(((AbstractSyntaxTree) pseudoRoot).getTreeExtend().get(1));

        //TODO: Delete prints for debugging
        System.out.println("DEBUG: LEFT(0)" + left);
        System.out.println("DEBUG: RIGHT(0)" + right);

        if(left.equals(right)) return left;

        throw new SemanticException("ERROR sintaxis");


    }
    public static void check(AbstractSyntaxTree abstractSyntaxTree) throws SemanticException {
        recursive(abstractSyntaxTree);

    }
}
