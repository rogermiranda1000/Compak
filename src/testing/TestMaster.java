package testing;

import lexic.TokenBuffer;
import preprocesser.CodeProcessor;
import syntax.GrammarAnalyzer;
import syntax.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Class TestMaster. This class manages the read of all test file to be tested with a specific function.
 */
public class TestMaster {

    /**
     * Main function to test with all test filnes (fail and pass folders).
     */
    public static void testAll() {
        // Pass tests
        TestMaster.testFolder(true, "testFiles/pass");
        TestMaster.testFolder(false, "testFiles/fail");
    }

    private static void testFolder(boolean shouldPass, String folder) {
        File testsFolder = new File(folder);
        ArrayList<String> results = new ArrayList<>();
        for (String testFile : Objects.requireNonNull(testsFolder.list())) {
            results.add(TestMaster.testFile(shouldPass, folder+"/"+testFile));
        }
        Collections.sort(results);
        for (String error : results) {
            if (!Objects.equals(error, "")) System.err.println(error);
        }
    }

    private static String testFile(boolean shouldPass, String file) {
        try {
            Parser p = new Parser(new TokenBuffer(new CodeProcessor(file)), new GrammarAnalyzer());
            boolean passed;
            try {
                p.compile(new File("tac.txt"));
                passed = true;
            } catch (Exception e) {
                passed = false;
            }
            if (shouldPass != passed) return ("Test error: Test " + file + " should have " + (shouldPass?"passed":"failed") + " but " + (passed?"passed":"failed"));
            return "";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}