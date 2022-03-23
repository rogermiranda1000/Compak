package syntax;

import entities.Token;

import javax.crypto.spec.OAEPParameterSpec;
import java.io.ObjectStreamException;

/**
 * Aquesta classe contindrà totes les produccions de Gramàtica
 */
public class GrammarAnalizer implements GrammarRequest {
    private static Production value;
    private static Production valueNumber;
    private static Production possibleDigit;
    private static Production possibleFloat;
    private static Production valueString;
    private static Production contingutStr;
    private static Production nomVariable;
    private static Production possibleStr;
    private static Production nomFuncio;
    private static Production possibleChar;
    private static Production valueBit;

    static {
        value = new Production(
                new Object[]{Token.NUMBER},
                // TODO afegir floats
                new Object[]{Token.STRING_VALUE},
                new Object[]{valueBit}
        );
        valueNumber = new Production(
                new Object[]{digit, possibleDigit}
        );
        possibleDigit = new Production(
                new Object[]{digit, possibleDigit},
                new Object[]{}
        );
        possibleFloat = new Production(
                new Object[]{valueNumber},
                new Object[]{}
        );
        valueString = new Production(
                new Object[]{Token.STRING_VALUE}
        );
        contingutStr = new Production(
                new Object[]{letter, contingutStr},
                new Object[]{digit, contingutStr},
                new Object[]{anyCharacter, contingutStr},
                new Object[]{}
        );
        nomVariable = new Production(
                new Object[]{letter, possibleStr}
        );
        possibleStr = new Production(
                new Object[]{letter, possibleStr},
                new Object[]{digit, possibleStr},
                new Object[]{}
        );
        nomFuncio = new Production(
                new Object[]{letter, possibleChar}
        );
        possibleChar = new Production(
                new Object[]{letter, possibleChar},
                new Object[]{}
        );
        valueBit = new Production(
                new Object[]{Token.TRUE},
                new Object[]{Token.FALSE}
        );

    }

    public GrammarAnalizer() {}

    public Production getEntryPoint() {
        return this.value; // TODO
    }
}
