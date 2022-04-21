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

    public void process(AbstractSyntaxTree abstractSyntaxTree) {
        abstractSyntaxTree.travelWithPriorityDepth(intermediateCodeData);
        intermediateCodeData.printData();
        intermediateCodeData.generateIntermediateCodeFile();
    }
}
