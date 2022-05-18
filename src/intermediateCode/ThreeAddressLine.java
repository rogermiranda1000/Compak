package intermediateCode;

import entities.Tag;
import entities.Token;
import entities.TokenDataPair;
import syntax.AbstractSyntaxTree;

import java.util.Objects;
import java.util.Stack;

/**
 * Class Three Address Line. In this class will save a line of TAC code and be the one who process string tags,
 * arguments and operation to string.
 */
public class ThreeAddressLine {
    private final TokenDataPair op;
    private final Object arg1;
    private final Object arg2;
    private static int serialCounter = 0;
    private int result;

    /**
     * Instantiates a new Three Address Line.
     *
     * @param op   the op
     * @param arg1 the arg 1
     * @param arg2 the arg 2
     */
    public ThreeAddressLine(TokenDataPair op, Object arg1, Object arg2) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = serialCounter;
        serialCounter++;
    }

    /**
     * Gets string line.
     *
     * @return line string
     */
    public String getLine() {
        return result + "=" + arg1 + op + arg2;
    }

    /**
     * Algorithm that depends on operation, id operation, tag's stack and arguments prints the corresponding tac line.
     *
     * @param idOp the id of operation to classify the temporal register
     * @param tags the tags in stack (for loops and conditionals)
     * @return the string line
     */
    public String printLine(int idOp, Stack<Tag> tags) {
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
            return "t" + idOp + " := " + "!" + arg1String;
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