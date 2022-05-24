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
import java.util.LinkedList;
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
    private LinkedList<ThreeAddressLine> data;
    private int idOp;

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
        this.data = new LinkedList<>(data);
        this.idOp = 0;
        generateIntermediateCodeFile(out);
    }

    private void generateIntermediateCodeFile(File out) throws IOException {
        Stack<Tag> stack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        while (!this.data.isEmpty()) {
            printLine(sb, data.pop(), stack);
        }

        FileWriter myWriter = new FileWriter(out);
        myWriter.write(sb.toString());
        myWriter.close();
    }

    private void printLine(StringBuilder r, ThreeAddressLine line, Stack<Tag> tags) {
        TokenDataPair op = line.getOp();
        Object arg1 = line.getArg1();
        Object arg2 = line.getArg2();
        String arg1String = null;
        String arg2String = null;
        int idOp = this.idOp++;

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
                r.append(arg2String).append('\n');
                return;
            } else if (arg2String == null) {
                r.append(arg1String).append('\n');
                return;
            } else {
                return;
            }
        }

        if (Objects.equals(op.getData(), "func") || Objects.equals(op.getData(), "af")) {
            return;
        }

        if (Objects.equals(op.getToken(), Token.MAIN)) {
            r.append("main:").append('\n');
            return;
        }

        if (Objects.equals(op.getToken(), Token.CALL_FUNC)) {
            if (arg2String.equals("NULL")) {
                r.append("t").append(idOp).append(" := ").append("Call ").append(arg1String).append('\n');
                return;
            } else {
                r.append("PushParam ").append(arg2String).append("\nt").append(idOp).append(" := ").append("Call ").append(arg1String).append('\n');
                return;
            }
        }

        if (Objects.equals(op.getData(), "name_func")) {
            // must be on top; generate the string and then append it to the top
            StringBuilder sb = new StringBuilder();
            sb.append(arg1String).append(":\nBeginFunc").append('\n');
            while (!this.data.isEmpty() && this.data.element().getOp() == null || !Objects.equals(this.data.element().getOp().getData(), "start_func")) printLine(sb, data.pop(), tags); // generate on the new string builder until the function ends
            if (!this.data.isEmpty()) printLine(sb, data.pop(), tags); // append the 'end function declaration'
            r.insert(0, sb);
            return;
        }

        if (Objects.equals(op.getData(), "params")) {
            if (arg1String == null) {
                return;
            } else {
                r.append("PopParam ").append(arg1String).append('\n');
                return;
            }
        }

        if (Objects.equals(op.getData(), "start_func")) {
            r.append("EndFunc").append('\n');
            return;
        }

        if (Objects.equals(op.getData(), "print")) {
            return "Print " + arg1String;
        }

        if (Objects.equals(op.getData(), "return")) {
            r.append("Return ").append(arg1String).append('\n');
            return;
        }

        if (Objects.equals(op.getData(), "end_for")) {
            Tag tag = tags.pop();
            r.append(tag.getVarIterate()).append(" := ").append(tag.getVarIterate()).append(" + 1").append("\ngoto ").append(tag.getName1()).append("\n").append(tag.getName2()).append(":").append('\n');
            return;
        }

        if (Objects.equals(op.getData(), "end_while")) {
            Tag tag = tags.pop();
            r.append("goto ").append(tag.getName1()).append("\n").append(tag.getName2()).append(":").append('\n');
            return;
        }

        if (Objects.equals(op.getData(), "end_if")) {
            Tag tag = tags.pop();
            Tag elseTag = new Tag("else");
            tags.push(elseTag);
            r.append("goto ").append(elseTag.getName2()).append("\n"); // if the 'if' is executed, skip the else
            r.append(tag.getName2()).append(":").append('\n');
            return;
        }

        if (Objects.equals(op.getData(), "end_else")) {
            Tag tag = tags.pop();
            r.append(tag.getName2()).append(":").append('\n');
            return;
        }

        if (Objects.equals(op.getData(), "if")) {
            // CASE if (bool)
            Tag tag = new Tag(arg1String);
            tags.push(tag);

            r.append(tag.getName1()).append(": if !").append(arg1String).append(" goto ").append(tag.getName2()).append('\n');
            return;
        }

        if (Objects.equals(op.getData(), "else")) {
            // end_if already creates the label
            return;
        }

        if (Objects.equals(op.getData(), "¡")) {
            // CASE ¡(bool)
            r.append("t").append(idOp).append(" := ").append("! ").append(arg1String).append('\n');
            return;
        }

        if (Objects.equals(op.getData(), "while")) {
            // CASE LOOP (bool)
            Tag tag = new Tag(arg1String);
            tags.push(tag);

            r.append(tag.getName1()).append(": if !").append(arg1String).append(" goto ").append(tag.getName2()).append('\n');
            return;
        }

        if (Objects.equals(op.getData(), "range")) {
            // CASE LOOP (FOR I IN RANGE(NUM))
            Tag tag = new Tag(arg1String);
            tags.push(tag);

            r.append(arg1String).append(" := ").append("0\n").append(tag.getName1()).append(": if ").append(arg1String).append(" >= ").append(arg2String).append(" goto ").append(tag.getName2()).append('\n');
            return;
        }

        if (Objects.equals(op.getData(), "=")) {
            r.append(arg1String).append(" := ").append(arg2String).append("\nt").append(idOp).append(" := ").append(arg1String).append('\n');
            return;
        }

        if (op.getData() == null) {
            return;
        }

        r.append("t").append(idOp).append(" := ").append(arg1String).append(" ").append(op.getData()).append(" ").append(arg2String).append('\n');
    }
}