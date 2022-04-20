package intermediateCode;

import entities.Token;
import entities.TokenDataPair;
import syntax.AbstractSyntaxTree;

import java.util.List;

public class IntermediateCodeGenerator {

    private IntermediateCodeData intermediateCodeData;

    public IntermediateCodeGenerator() {
        this.intermediateCodeData = new IntermediateCodeData();
    }

    private TokenDataPair assignPointer(List<Object> treeExtend) {
        System.out.println("Hola carles");
        return null;
    }


    /**ThreeAddressLine tac = new ThreeAddressLine(
            tree.getOperation(),
            (tree.getTreeExtend().get(0) instanceof TokenDataPair)? (TokenDataPair) tree.getTreeExtend().get(0) : assignPointer(tree.getTreeExtend()),
            (tree.getTreeExtend().get(1) instanceof TokenDataPair)? (TokenDataPair) tree.getTreeExtend().get(1) : assignPointer(tree.getTreeExtend())
    );**/

    public void process(AbstractSyntaxTree abstractSyntaxTree) {
        System.out.println("soc un pro");
        abstractSyntaxTree.travelWithPriorityDepth(intermediateCodeData);
        System.out.println(intermediateCodeData.toString());
        //recursive(abstractSyntaxTree);


    }


    public void recursive(AbstractSyntaxTree abstractSyntaxTree) {
        //recursive(abstractSyntaxTree.);
    }
}
