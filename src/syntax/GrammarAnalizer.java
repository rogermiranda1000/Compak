package syntax;

import entities.Token;

/**
 * Aquesta classe contindrà totes les produccions de Gramàtica
 */
public class GrammarAnalizer implements GrammarRequest {
    private static Production sentencies;
    private static Production possibleSentencies;
    private static Production possibleAssignacio;

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
        sentencies = new Production(
                new Object[]{possibleSentencies, sentencies},
                new Object[]{Token.RETURN, id, Token.EOL},
                new Object[]{}
        );
        possibleSentencies = new Production(
                new Object[]{declaracioVariable, possibleAssignacio, Token.EOL},
                new Object[]{nomVariable, Token.ASSIGN, n0, Token.EOL},
                new Object[]{coondicional},
                new Object[]{declaracioBucle}
        );
        possibleAssignacio = new Production(
                new Object[]{Token.ASSIGN, n0},
                new Object[]{}
        );
    }

    public GrammarAnalizer() {}

    public Production getEntryPoint() {
        return this.value; // TODO
    }
}
