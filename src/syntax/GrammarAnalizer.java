package syntax;

import entities.Token;

import javax.sound.sampled.Port;

/**
 * Aquesta classe contindrà totes les produccions de Gramàtica
 */
public class GrammarAnalizer implements GrammarRequest {
    private static Production n0;
    private static Production n0Sub;
    private static Production n1;
    private static Production n1Sub;
    private static Production n2;
    private static Production n2Sub;
    private static Production n3;
    private static Production n3Sub;
    private static Production negat;
    private static Production n5;
    private static Production ID;
    private static Production symbolSumaResta;
    private static Production symbolDivMult;
    private static Production symbolCond;

    private final Production value_bit = new Production(
            new Object[]{Token.TRUE},
            new Object[]{Token.FALSE}
    );
    private final Production value = new Production(
            new Object[]{Token.NUMBER},
            // TODO afegir floats
            new Object[]{Token.STRING_VALUE},
            new Object[]{value_bit}
    );

    static {
        n0 = new Production(
                new Object[]{n1, n0Sub}
        );
        n0Sub = new Production(
                new Object[]{symbolCond, n1, n0Sub},
                new Object[]{}
        );
        n1 = new Production(
                new Object[]{n2, n1Sub}
        );
        n1Sub = new Production(
                new Object[]{symbolSumaResta, n2, n1Sub},
                new Object[]{}
        );
        n2 = new Production(
                new Object[]{n3, n2Sub}
        );
        n2Sub = new Production(
                new Object[]{symbolDivMult, n3, n2Sub},
                new Object[]{}
        );
        n3 = new Production(
                new Object[]{n5, n3Sub}
        );
        n3Sub = new Production(
                new Object[]{Token.RAISE, n5, n3Sub},
                new Object[]{}
        );
        negat = new Production(
                new Object[]{Token.NOT},
                new Object[]{}
        );
        n5 = new Production(
                new Object[]{negat, Token.OPN_PARENTH, n0, Token.CLS_PARENTH},
                new Object[]{ID}
        );
        ID = new Production(
                new Object[]{id}
        );
        symbolSumaResta = new Production(
                new Object[]{Token.SUM},
                new Object[]{Token.SUBSTRACT}
        );
        symbolDivMult = new Production(
                new Object[]{Token.MULT},
                new Object[]{Token.DIVIDE},
                new Object[]{Token.MOD}
        );
        symbolCond = new Production(
                new Object[]{Token.OR},
                new Object[]{Token.AND},
                new Object[]{Token.COMP},
                new Object[]{Token.LT},
                new Object[]{Token.GT}
        );
    }

    public GrammarAnalizer() {}

    public Production getEntryPoint() {
        return this.value; // TODO
    }
}
