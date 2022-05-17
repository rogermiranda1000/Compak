import entities.DuplicateVariableException;
import entities.UnknownVariableException;
import lexic.TokenBuffer;
import optimizer.OptimizerManager;
import preprocesser.CodeProcessor;
import syntax.GrammarAnalyzer;
import syntax.InvalidTreeException;
import syntax.Parser;

import java.io.File;
import java.io.IOException;

import static mips.MipsGenerator.generateMipsFromFile;

public class Main {
    private static final String PATH_FILE = "file.sus";
    private static final String PATH_TAC = "tac.txt";
    private static final String PATH_MIPS = "mips.asm";

    public static void main(String[] args) throws InvalidTreeException, DuplicateVariableException, UnknownVariableException, IOException {
        File tac = new File(PATH_TAC);
        Parser p = new Parser(new TokenBuffer(new CodeProcessor(PATH_FILE)), new GrammarAnalyzer());
        p.compile(tac);
        //TestMaster.testAll(); // TODO Uncomment for final commit
        OptimizerManager om = new OptimizerManager();
        om.optimize(tac);
        generateMipsFromFile(PATH_TAC, PATH_MIPS);
    }
}
