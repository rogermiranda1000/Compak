package syntax;

import entities.Token;

/**
 * Aquesta classe contindrà totes les produccions de Gramàtica
 */
public class GrammarAnalizer implements GrammarRequest {
    private static Production opcions;
    private static Production start;
    private static Production possiblesOpcions;



    private static final Production value_bit = new Production(
            new Object[]{Token.TRUE},
            new Object[]{Token.FALSE}
    );
    private static final Production value = new Production(
            new Object[]{Token.NUMBER},
            // TODO afegir floats
            new Object[]{Token.STRING_VALUE},
            new Object[]{value_bit}
    );


    static {
        possiblesOpcions = new Production(
                new Object[]{declaracioFuncio},
                new Object[]{declaracioVariable}
        );
        opcions = new Production(
                new Object[]{possiblesOpcions, opcions},
                new Object[]{Production.EPSILON}
        );
        start = new Production(
                new Object[]{opcions, Token.MAIN, Token.OPN_PARENTH, Token.CLS_PARENTH, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT}
        );
    }

    public GrammarAnalizer() {}

    public void accept(String tokenExpected) throws GrammarException {
        if(tokenExpected.equals(value)) return;
        throw new GrammarException("Esperavem el token " + tokenExpected + " i no s'ha trobat");
    }

    public Production getEntryPoint() {
        return this.start;
    }



}
