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
    private static Production n3;
    private static Production n3Sub;
    private static Production n4;
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
                new Object[]{},
                new Object[]{symbolDivMult, n3, n2S}
        );
    }

    public GrammarAnalizer() {}

    public Production getEntryPoint() {
        return this.value; // TODO
    }
}
