package intermediateCode;

import entities.Tag;
import entities.Token;
import entities.TokenDataPair;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class IntermediateCodeData {

    /**
     * Example 3@Code Representation
     *      Op     Arg1     Arg2     Result
     * 0:   -      c                   t1
     * 1:   +      b          t1       t2
     * 2:   =      d          t2        a
     */

    private ArrayList<ThreeAddressLine> data;

    public IntermediateCodeData() {
        data = new ArrayList<>();
    }

    public int addLine(TokenDataPair op, Object arg1, Object arg2) {
        data.add(new ThreeAddressLine(op, arg1, arg2));
        return data.size()-1;
    }

    public int addLine(TokenDataPair op, Object arg1) {
        data.add(new ThreeAddressLine(op, arg1, null));
        return data.size()-1;
    }

    public void printData() {
        Stack<Tag> stack = new Stack<>();

        for (int i = 0; i < data.size(); i++) {
            String lineData = data.get(i).printLine(i, stack);
            if (lineData != null) {
                System.out.println(lineData);
            }
        }
    }

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
