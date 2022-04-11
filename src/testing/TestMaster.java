package testing;

import lexic.TokenBuffer;
import preprocesser.CodeProcessor;
import syntax.GrammarAnalizer;
import syntax.InvalidTreeException;
import syntax.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class TestMaster {
    public static void testAll() {
        // Pass tests
        TestMaster.testFolder(true, "testFiles/pass");
        TestMaster.testFolder(false, "testFiles/fail");
    }

    private static void testFolder(boolean shouldPass, String folder) {
        File testsFolder = new File(folder);
        for (String testFile : Objects.requireNonNull(testsFolder.list())) {
            TestMaster.testFile(shouldPass, folder+"/"+testFile);
        }
    }

    private static void testFile(boolean shouldPass, String file) {
        try {
            Parser p = new Parser(new TokenBuffer(new CodeProcessor(file)), new GrammarAnalizer());
            boolean passed = true;
            try {
                p.compile(null);
            } catch (InvalidTreeException e) {
                passed = false;
            }
            if (shouldPass != passed) System.err.println("Test error: Test " + file + " should have " + (shouldPass?"passed":"failed") + " but " + (passed?"passed":"failed"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}