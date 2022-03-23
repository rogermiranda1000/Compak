package syntax;

import entities.Token;

/**
 * Aquesta classe contindrà totes les produccions de Gramàtica
 */
public class GrammarAnalizer implements GrammarRequest {
    private static Production condicional;
    private static Production elseConditional;

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
        condicional = new Production(
                new Object[]{Token.IF, Token.OPN_PARENTH, n0, Token.CLS_PARENTH, elseConditional}
        );
        elseConditional = new Production(
                new Object[]{Token.ELSE, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT},
                new Object[]{}
        );
    }

    public GrammarAnalizer() {}

    public Production getEntryPoint() {
        return this.value; // TODO
    }
}
