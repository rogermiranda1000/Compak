package intermediateCode;

import entities.Token;
import entities.TokenDataPair;

import java.io.Serial;

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

    @Override
    public String toString(){
        return (op.toString() + arg1.toString() + arg2.toString() + serialCounter);

    }
}
