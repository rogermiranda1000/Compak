package syntax;

import entities.Token;

/**
 * Aquesta classe contindrà totes les produccions de Gramàtica
 */
public class GrammarAnalizer implements GrammarRequest {
    private static Production declaracioBucle;
    private static Production declaracioBucleSub;
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
        declaracioBucle = new Production(
                new Object[]{Token.BUCLE, Token.OPN_PARENTH, declaracioBucleSub, Token.CLS_PARENTH, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT}
        );
        declaracioBucleSub = new Production(
                new Object[]{n0},
                new Object[]{Token.FOR, nomVariable, Token.IN, Token.RANGE, Token.OPN_PARENTH, valueNumber, Token.CLS_PARENTH}
        );
    }

    public GrammarAnalizer() {}

    public Production getEntryPoint() {
        return this.value; // TODO
    }
}
