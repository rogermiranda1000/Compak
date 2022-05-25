import entities.DuplicateVariableException;
import entities.UnknownVariableException;
import intermediateCode.IntermediateCodeGenerator;
import intermediateCode.TacConverter;
import lexic.TokenBuffer;
import mips.MipsConverter;
import mips.MipsGenerator;
import optimizer.Optimizer;
import optimizer.OptimizerManager;
import preprocesser.CodeProcessor;
import syntax.Compiler;
import syntax.GrammarAnalyzer;
import syntax.InvalidTreeException;
import syntax.Parser;
import testing.TestMaster;

import java.io.File;
import java.io.IOException;

/**
 * Class Main.
 */
public class Main {
    private static final String PATH_FILE = "file.sus";
    private static final String PATH_TAC = "tac.txt";
    private static final String PATH_MIPS = "mips.asm";

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InvalidTreeException       the invalid tree exception
     * @throws DuplicateVariableException the duplicate variable exception
     * @throws UnknownVariableException   the unknown variable exception
     * @throws IOException                the io exception
     */
    public static void main(String[] args) throws InvalidTreeException, DuplicateVariableException, UnknownVariableException, IOException {
        File tac = new File(PATH_TAC);

        // Parser phase
        Compiler compiler = new Parser(new TokenBuffer(new CodeProcessor(PATH_FILE)), new GrammarAnalyzer());
        compiler.compile(tac);

        // Test phase
        TestMaster.testAll();

        // AST to TAC code phase
        TacConverter tacConverter = new IntermediateCodeGenerator();
        tacConverter.process(compiler.getThreeAddressLines(), tac);

        // Optimizer phase
        Optimizer opt = new OptimizerManager();
        opt.optimize(tac);

        // TAC optimized to MIPs phase
        MipsConverter mipsConverter = new MipsGenerator();
        mipsConverter.generateMipsFromFile(PATH_TAC, PATH_MIPS);
    }
}
