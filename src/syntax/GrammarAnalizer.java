package syntax;

import entities.Token;

/**
 * Aquesta classe contindrà totes les produccions de Gramàtica
 */
public class GrammarAnalizer implements GrammarRequest {
    private static Production declaracioVariable;
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
        declaracioVariable = new Production(
                new Object[]{Token.STR, nomVariable},
                new Object[]{Token.BIG, nomVariable},
                new Object[]{Token.INT, nomVariable},
                new Object[]{Token.BIT, nomVariable},
                new Object[]{Token.FLO, nomVariable}
        );
    }

    public GrammarAnalizer() {}

    public Production getEntryPoint() {
        return this.value; // TODO
    }
}
