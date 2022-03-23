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

    }

    public GrammarAnalizer() {}

    public Production getEntryPoint() {
        return this.value; // TODO
    }
}
