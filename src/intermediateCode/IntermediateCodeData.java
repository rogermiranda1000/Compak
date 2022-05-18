package intermediateCode;

import entities.Tag;
import entities.TokenDataPair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Class Intermediate Code Data.
 * Example 3@Code Representation
 *      Op     Arg1     Arg2     Result
 * 0:   -      c                   t1
 * 1:   +      b          t1       t2
 * 2:   =      d          t2        a
 */
public class IntermediateCodeData {

    private ArrayList<ThreeAddressLine> data;

    /**
     * Instantiates a new Intermediate code data.
     */
    public IntermediateCodeData() {
        data = new ArrayList<>();
    }

    /**
     * Add new TAC line.
     *
     * @param op   operation
     * @param arg1 arg 1
     * @param arg2 arg 2
     * @return data size
     */
    public int addLine(TokenDataPair op, Object arg1, Object arg2) {
        data.add(new ThreeAddressLine(op, arg1, arg2));
        return data.size()-1;
    }

    /**
     * Add new TAC line.
     *
     * @param op   operation
     * @param arg1 arg 1
     * @return data size
     */
    public int addLine(TokenDataPair op, Object arg1) {
        data.add(new ThreeAddressLine(op, arg1, null));
        return data.size()-1;
    }

    /**
     * Generate intermediate code file.
     *
     * @param out file's out
     * @throws IOException the io exception
     */
    public void generateIntermediateCodeFile(File out) throws IOException {
        FileWriter myWriter = new FileWriter(out);
        Stack<Tag> stack = new Stack<>();
        for (int i = 0; i < data.size(); i++) {
            String lineData = data.get(i).printLine(i, stack);
            if (lineData != null) {
                myWriter.write(lineData);
                myWriter.write("\n");
            }
        }
        myWriter.close();
    }
}
