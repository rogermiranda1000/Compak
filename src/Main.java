import entities.Token;
import entities.TokenDataPair;
import lexic.TokenBuffer;
import lexic.TokenRequest;
import optimizer.OptimizerManager;
import preprocesser.CodeProcessor;
import preprocesser.LineRequest;
import syntax.GrammarAnalizer;
import syntax.Parser;

import java.io.FileNotFoundException;

import static mips.MipsGenerator.generateMipsFromFile;

public class Main {
    private static final String PATH_FILE = "file.sus";
    private static final String PATH_TAC = "tac.txt";
    private static final String PATH_MIPS = "mips.asm";

    public static void main(String[] args) throws FileNotFoundException {
        Parser p = new Parser(new TokenBuffer(new CodeProcessor(PATH_FILE)), new GrammarAnalizer());
        p.compile(null);
        //p.test(); TODO: Uncomment for final commit
        OptimizerManager om = new OptimizerManager();
        om.optimize();
        generateMipsFromFile(PATH_TAC, PATH_MIPS);
    }
}
