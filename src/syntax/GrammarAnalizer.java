package syntax;

import entities.Token;

/**
 * Aquesta classe contindrà totes les produccions de Gramàtica
 */
public class GrammarAnalizer extends GrammarRequest {
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
    private static Production declaracioBucle;
    private static Production declaracioBucleSub;
    private static Production n0;
    private static Production n0Sub;
    private static Production n1;
    private static Production n1Sub;
    private static Production n2;
    private static Production n2Sub;
    private static Production n3;
    private static Production n3Sub;
    private static Production negat;
    private static Production n5;
    private static Production symbolSumaResta;
    private static Production symbolDivMult;
    private static Production symbolCond;
    private static Production id;
    private static Production idStr;
    private static Production idNumOrFloat;
    private static Production idCond;
    private static Production sentencies;
    private static Production possibleSentencies;
    private static Production possibleAssignacio;

    private static final Production valueBit = new Production(
            new Object[]{Token.TRUE},
            new Object[]{Token.FALSE}
    );

    private static final Production value = new Production(
            new Object[]{Token.NUMBER},
            new Object[]{Token.FLOAT},
            new Object[]{Token.STRING_VALUE},
            new Object[]{valueBit}
    );

    private static final Production tipus = new Production(
        new Object[]{Token.STR},
        new Object[]{Token.BIG},
        new Object[]{Token.INT},
        new Object[]{Token.BIT},
        new Object[]{Token.FLO}
    );

    static {
        declaracioFuncio = new Production(
                new Object[]{Token.FUNC, Token.ID, Token.OPN_PARENTH, arguments, Token.CLS_PARENTH, declaracioFuncioSub, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT}
        );

        declaracioFuncioSub = new Production(
                new Object[]{Token.RET_TYPE, tipus},
                new Object[]{}
        );

        arguments = new Production(
                new Object[]{tipus, Token.ID, argumentsSub}
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
                new Object[]{tipus, Token.ID}
        );

        condicional = new Production(
                new Object[]{Token.IF, Token.OPN_PARENTH, n0, Token.CLS_PARENTH, elseConditional}
        );

        elseConditional = new Production(
                new Object[]{Token.ELSE, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT},
                new Object[]{}
        );

        declaracioBucle = new Production(
                new Object[]{Token.BUCLE, Token.OPN_PARENTH, declaracioBucleSub, Token.CLS_PARENTH, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT}
        );

        declaracioBucleSub = new Production(
                new Object[]{n0},
                new Object[]{Token.FOR, Token.ID, Token.IN, Token.RANGE, Token.OPN_PARENTH, Token.NUMBER, Token.CLS_PARENTH}
        );

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
                new Object[]{}
        );
        n2 = new Production(
                new Object[]{n3, n2Sub}
        );
        n2Sub = new Production(
                new Object[]{symbolDivMult, n3, n2Sub},
                new Object[]{}
        );
        n3 = new Production(
                new Object[]{n5, n3Sub}
        );
        n3Sub = new Production(
                new Object[]{Token.RAISE, n5, n3Sub},
                new Object[]{}
        );
        negat = new Production(
                new Object[]{Token.NOT},
                new Object[]{}
        );
        n5 = new Production(
                new Object[]{negat, Token.OPN_PARENTH, n0, Token.CLS_PARENTH},
                new Object[]{Token.ID}
        );
        symbolSumaResta = new Production(
                new Object[]{Token.SUM},
                new Object[]{Token.SUBSTRACT}
        );
        symbolDivMult = new Production(
                new Object[]{Token.MULT},
                new Object[]{Token.DIVIDE},
                new Object[]{Token.MOD}
        );
        symbolCond = new Production(
                new Object[]{Token.OR},
                new Object[]{Token.AND},
                new Object[]{Token.COMP},
                new Object[]{Token.LT},
                new Object[]{Token.GT}
        );

        id = new Production(
                new Object[]{value},
                new Object[]{Token.ID}
        );
        idStr = new Production(
                new Object[]{Token.STRING_VALUE},
                new Object[]{Token.ID}
        );
        idNumOrFloat = new Production(
                new Object[]{Token.NUMBER},
                new Object[]{Token.FLOAT},
                new Object[]{Token.ID}
        );
        idCond = new Production(
                new Object[]{valueBit},
                new Object[]{Token.ID}
        );
        sentencies = new Production(
                new Object[]{possibleSentencies, sentencies},
                new Object[]{Token.RETURN, id, Token.EOL},
                new Object[]{}
        );
        possibleSentencies = new Production(
                new Object[]{declaracioVariable, possibleAssignacio, Token.EOL},
                new Object[]{Token.ID, Token.ASSIGN, n0, Token.EOL},
                new Object[]{condicional},
                new Object[]{declaracioBucle}
        );
        possibleAssignacio = new Production(
                new Object[]{Token.ASSIGN, n0},
                new Object[]{}
        );
    }

    public GrammarAnalizer() {}

    public Production getEntryPoint() {
        return this.start;
    }
}
