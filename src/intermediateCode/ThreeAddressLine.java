package intermediateCode;

import entities.Tag;
import entities.TokenDataPair;
import syntax.AbstractSyntaxTree;

import java.util.Objects;
import java.util.Stack;

public class ThreeAddressLine {
    private final TokenDataPair op;
    private final Object arg1;
    private final Object arg2;
    private static int serialCounter = 0;
    private int result;

    public ThreeAddressLine(TokenDataPair op, Object arg1, Object arg2) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = serialCounter;
        serialCounter++;
    }

    public String getLine() {
        return result + "=" + arg1 + op + arg2;
    }

    public Object getArg1() {
        return arg1;
    }

    public Object getArg2() {
        return arg2;
    }

    public int getResult() {
        return result;
    }

    public TokenDataPair getOp() {
        return op;
    }

    public String printLine(int idOp, Stack<Tag> tags) {
        String arg1String;
        String arg2String;

        if (arg1 instanceof TokenDataPair) {
            arg1String = ((TokenDataPair) arg1).getData();
            if (arg1String.equals("true")) {
                arg1String = "1";
            }

            if (arg1String.equals("false")) {
                arg1String = "0";
            }
        } else {
            arg1String = "t" + String.valueOf(((AbstractSyntaxTree) arg1).getId());
        }

        if (arg2 != null) {
            if (arg2 instanceof TokenDataPair) {
                arg2String = ((TokenDataPair) arg2).getData();

                if (arg2String.equals("true")) {
                    arg2String = "1";
                }

                if (arg2String.equals("false")) {
                    arg2String = "0";
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