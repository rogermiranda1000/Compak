package syntax;

import entities.Token;

/**
 * Aquesta classe contindrà totes les produccions de Gramàtica
 */
public class GrammarAnalizer implements GrammarRequest {
    private static Production declaracioFuncio;
    private static Production declaracioFuncioSub;
    private static Production arguments;
    private static Production argumentsSub;
    private static Production opcions;
    private static Production start;
    private static Production possiblesOpcions;
    private static Production declaracioVariable;
    private static Production condicional;
    private static Production elseConditional;

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
        declaracioFuncio = new Production(
                new Object[]{Token.FUNC, nomFuncio, Token.OPN_PARENTH, arguments, Token.CLS_PARENTH, declaracioFuncioSub, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT}
        );

        declaracioFuncioSub = new Production(
                new Object[]{Token.RET_TYPE, tipus},
                new Object[]{}
        );

        arguments = new Production(
                new Object[]{tipus, nomVariable, argumentsSub}
        );

        argumentsSub = new Production(
                new Object[]{Token.COMMA, arguments},
                new Object[]{}
        );

        possiblesOpcions = new Production(
                new Object[]{declaracioFuncio},
                new Object[]{declaracioVariable}
        );

        opcions = new Production(
                new Object[]{possiblesOpcions, opcions},
                new Object[]{}
        );

        start = new Production(
                new Object[]{opcions, Token.MAIN, Token.OPN_PARENTH, Token.CLS_PARENTH, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT}
        );

        declaracioVariable = new Production(
                new Object[]{Token.STR, nomVariable},
                new Object[]{Token.BIG, nomVariable},
                new Object[]{Token.INT, nomVariable},
                new Object[]{Token.BIT, nomVariable},
                new Object[]{Token.FLO, nomVariable}
        );

        condicional = new Production(
                new Object[]{Token.IF, Token.OPN_PARENTH, n0, Token.CLS_PARENTH, elseConditional}
        );
        elseConditional = new Production(
                new Object[]{Token.ELSE, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT},
                new Object[]{}
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
