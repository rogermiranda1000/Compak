package intermediateCode;

import entities.Token;

public class ThreeAddressLine {
    private final Token op;
    private final String arg1;
    private final String arg2;
    private final String result;

    public ThreeAddressLine(Token op, String arg1, String arg2, String result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public String getLine() {
        return result + "=" + arg1 + op + arg2;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getResult() {
        return result;
    }

    public Token getOp() {
        return op;
    }
}
