package syntax;

import entities.Token;

/**
 * Aquesta classe contindrà totes les produccions de Gramàtica
 */
public class GrammarAnalizer implements GrammarRequest {
    private static Production id;
    private static Production idStr;
    private static Production idNumOrFloat;
    private static Production idCond;
    private static Production digit;
    private static Production letter;
    private static Production anyCharacter;

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
        id = new Production(
                new Object[]{value},
                new Object[]{nomVariable}
        );
        idStr = new Production(
                new Object[]{valueString},
                new Object[]{nomVariable}
        );
        idNumOrFloat = new Production(
                //TODO: Fer el REGEX al enum
                new Object[]{valueNumber, possibleFloat},
                new Object[]{nomVariable}
        );
        idCond = new Production(
                new Object[]{valueBit},
                new Object[]{nomVariable}
        );
        digit = new Production(
                new Object[]{Token.NUMBER}
        );
    }

    public GrammarAnalizer() {}

    public Production getEntryPoint() {
        return this.value; // TODO
    }
}
