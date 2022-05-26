package entities;

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
    }

    public Object getArg1() {
        return arg1;
    }

    public Object getArg2() {
        return arg2;
    }

    public TokenDataPair getOp() {
        return op;
    }
}