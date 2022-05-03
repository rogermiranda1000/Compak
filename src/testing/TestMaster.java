package testing;

import lexic.TokenBuffer;
import preprocesser.CodeProcessor;
import syntax.GrammarAnalizer;
import syntax.InvalidTreeException;
import syntax.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class TestMaster {

    public static void main(String[] args) {
        System.err.println((TestMaster.testFile(true, "testFiles/pass/t50")));
    }

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
            Parser p = new Parser(new TokenBuffer(new CodeProcessor(file)), new GrammarAnalizer());
            boolean passed;
            try {
                passed = p.compile(null);
            } catch (InvalidTreeException e) {
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