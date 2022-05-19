package intermediateCode;

import entities.Tag;
import entities.ThreeAddressLine;
import entities.Token;
import entities.TokenDataPair;
import syntax.AbstractSyntaxTree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

/**
 * Class Intermediate Code Generator. It generates all TAC lines to be printed on tac.txt file.
 * Example 3@Code Representation
 *      Op     Arg1     Arg2
 * 0:   -      c
 * 1:   +      b          t1
 * 2:   =      d          t2
 */
public class IntermediateCodeGenerator implements TacConverter {
    private ArrayList<ThreeAddressLine> data;

    /**
     * Instantiates a new Intermediate code generator.
     */
    public IntermediateCodeGenerator() {

    }

    /**
     * Function that generates all TAC lines travelling on AST and printing each line.
     *
     * @param data               array with all TAC lines
     * @param out                file's out
     * @throws IOException       the io exception
     */
    @Override
    public void process(ArrayList<ThreeAddressLine> data, File out) throws IOException {
        this.data = data;
        generateIntermediateCodeFile(out);
    }

    private void generateIntermediateCodeFile(File out) throws IOException {
        FileWriter myWriter = new FileWriter(out);
        Stack<Tag> stack = new Stack<>();
        for (int i = 0; i < data.size(); i++) {
            String lineData = printLine(data.get(i), i, stack);
            if (lineData != null) {
                myWriter.write(lineData);
                myWriter.write("\n");
            }
        }
        myWriter.close();
    }

    private String printLine(ThreeAddressLine line, int idOp, Stack<Tag> tags) {
        TokenDataPair op = line.getOp();
        Object arg1 = line.getArg1();
        Object arg2 = line.getArg2();
        String arg1String = null;
        String arg2String = null;

        if (arg1 instanceof TokenDataPair) {
            arg1String = ((TokenDataPair) arg1).getData();
            if (arg1String != null) {
                if (arg1String.equals("true")) {
                    arg1String = "1";
                }

                if (arg1String.equals("false")) {
                    arg1String = "0";
                }
            }
        } else {
            arg1String = "t" + String.valueOf(((AbstractSyntaxTree) arg1).getId());
        }

        if (arg2 != null) {
            if (arg2 instanceof TokenDataPair) {
                arg2String = ((TokenDataPair) arg2).getData();
                if (arg2String != null) {
                    if (arg2String.equals("true")) {
                        arg2String = "1";
                    }

                    if (arg2String.equals("false")) {
                        arg2String = "0";
                    }
                }
            } else {
                arg2String = "t" + String.valueOf(((AbstractSyntaxTree) arg2).getId());
            }
        } else {
            arg2String = "NULL";
        }

        if (op == null) {
            if (arg1String == null) {
                return arg2String;
            } else if (arg2String == null) {
                return arg1String;
            } else {
                return null;
            }
        }

        if (Objects.equals(op.getData(), "func")) {
            return "";
        }

        if (Objects.equals(op.getToken(), Token.MAIN)) {
            return "main:";
        }

        if (Objects.equals(op.getToken(), Token.CALL_FUNC)) {
            if (arg2String.equals("NULL")) {
                return "Call " + arg1String;
            } else {
                return "PushParam " + arg2String + "\nCall " + arg1String;
            }
        }

        if (Objects.equals(op.getData(), "name_func")) {
            return arg1String + ":\nBeginFunc";
        }

        if (Objects.equals(op.getData(), "params")) {
            if (arg1String == null) {
                return "";
            } else {
                return "PopParam " + arg1String;
            }
        }

        if (Objects.equals(op.getData(), "start_func")) {
            return "EndFunc";
        }

        if (Objects.equals(op.getData(), "end_for")) {
            Tag tag = tags.pop();
            return tag.getVarIterate() + " := " + tag.getVarIterate() + " + 1" + "\ngoto " + tag.getName1() + "\n" + tag.getName2() + ":";
        }

        if (Objects.equals(op.getData(), "end_while")) {
            Tag tag = tags.pop();
            return "goto " + tag.getName1() + "\n" + tag.getName2() + ":";
        }

        if (Objects.equals(op.getData(), "end_if")) {
            Tag tag = tags.pop();
            return tag.getName2() + ":";
        }

        if (Objects.equals(op.getData(), "if")) {
            // CASE if (bool)
            Tag tag = new Tag(arg1String);
            tags.push(tag);

            return tag.getName1() + ": if !" + arg1String + " goto " + tag.getName2();
        }

        if (Objects.equals(op.getData(), "¡")) {
            // CASE ¡(bool)
            return "t" + idOp + " := " + "! " + arg1String;
        }

        if (Objects.equals(op.getData(), "while")) {
            // CASE LOOP (bool)
            Tag tag = new Tag(arg1String);
            tags.push(tag);

            return tag.getName1() + ": if !" + arg1String + " goto " + tag.getName2();
        }

        if (Objects.equals(op.getData(), "range")) {
            // CASE LOOP (FOR I IN RANGE(NUM))
            Tag tag = new Tag(arg1String);
            tags.push(tag);

            return arg1String + " := " + "0\n" + tag.getName1() + ": if " + arg1String + " >= " + arg2String + " goto " + tag.getName2();
        }

        if (Objects.equals(op.getData(), "=")) {
            return (arg1String + " := " + arg2String  + "\nt" + idOp + " := " + arg1String);
        }

        if (op.getData() == null) {
            return "";
        }

        return ("t" + idOp + " := " + arg1String + " " + op.getData() + " " + arg2String);
    }
}