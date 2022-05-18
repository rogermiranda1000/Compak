package intermediateCode;

import syntax.AbstractSyntaxTree;

import java.io.File;
import java.io.IOException;

/**
 * Class Intermediate Code Generator. It generates all TAC lines to be printed on tac.txt file.
 */
public class IntermediateCodeGenerator {

    private IntermediateCodeData intermediateCodeData;

    /**
     * Instantiates a new Intermediate code generator.
     */
    public IntermediateCodeGenerator() {
        this.intermediateCodeData = new IntermediateCodeData();
    }

    /**
     * Function that generates all TAC lines travelling on AST and printing each line.
     *
     * @param abstractSyntaxTree abstract syntax tree
     * @param out                file's out
     * @throws IOException       the io exception
     */
    public void process(AbstractSyntaxTree abstractSyntaxTree, File out) throws IOException {
        abstractSyntaxTree.travelWithPriorityDepth(intermediateCodeData);
        intermediateCodeData.generateIntermediateCodeFile(out);
    }
}
