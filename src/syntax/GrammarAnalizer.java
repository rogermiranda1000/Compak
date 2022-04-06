package syntax;

import entities.Token;

/**
 * Aquesta classe contindrà totes les produccions de Gramàtica
 */
public class GrammarAnalizer extends GrammarRequest {
    protected static final Production declaracioFuncio = new Production();
    protected static final Production arguments = new Production();
    protected static final Production argumentsSub = new Production();
    protected static final Production opcions = new Production();
    protected static final Production start = new Production();
    protected static final Production possiblesOpcions = new Production();
    protected static final Production condicional = new Production();
    protected static final Production elseConditional = new Production();
    protected static final Production declaracioBucle = new Production();
    protected static final Production declaracioBucleSub = new Production();
    protected static final Production n0 = new Production();
    protected static final Production n0Sub = new Production();
    protected static final Production n1 = new Production();
    protected static final Production n1Sub = new Production();
    protected static final Production n2 = new Production();
    protected static final Production n2Sub = new Production();
    protected static final Production n3 = new Production();
    protected static final Production n3Sub = new Production();
    protected static final Production n5 = new Production();
    protected static final Production sentencies = new Production();
    protected static final Production possibleSentencies = new Production();
    protected static final Production possibleAssignacio = new Production();

    protected static final Production valueBit = new Production(
            new Object[]{Token.TRUE},
            new Object[]{Token.FALSE}
    );

    protected static final Production value = new Production(
            new Object[]{Token.NUMBER},
            new Object[]{Token.FLOAT},
            new Object[]{Token.STRING_VALUE},
            new Object[]{valueBit}
    );

    protected static final Production tipus = new Production(
            new Object[]{Token.STR},
            new Object[]{Token.BIG},
            new Object[]{Token.INT},
            new Object[]{Token.BIT},
            new Object[]{Token.FLO}
    );

    protected static final Production declaracioFuncioSub = new Production(
            new Object[]{Token.RET_TYPE, tipus},
            new Object[]{}
    );

    protected static final Production declaracioVariable = new Production(
            new Object[]{tipus, Token.ID}
    );

    protected static final Production symbolSumaResta = new Production(
            new Object[]{Token.SUM},
            new Object[]{Token.SUBSTRACT}
    );

    protected static final Production symbolDivMult = new Production(
            new Object[]{Token.MULT},
            new Object[]{Token.DIVIDE},
            new Object[]{Token.MOD}
    );

    protected static final Production symbolCond = new Production(
            new Object[]{Token.OR},
            new Object[]{Token.AND},
            new Object[]{Token.COMP},
            new Object[]{Token.LT},
            new Object[]{Token.GT}
    );

    protected static final Production negat = new Production(
            new Object[]{Token.NOT},
            new Object[]{}
    );

    protected static final Production id = new Production(
            new Object[]{value},
            new Object[]{Token.ID}
    );

    static {
        declaracioFuncio.addProduction(Token.FUNC, Token.ID, Token.OPN_PARENTH, arguments, Token.CLS_PARENTH, declaracioFuncioSub, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT);

        arguments.addProduction(tipus, Token.ID, argumentsSub);

        argumentsSub.addProduction(Token.COMMA, arguments)
                .addProduction();

        possiblesOpcions.addProduction(declaracioFuncio)
                .addProduction(declaracioVariable);

        opcions.addProduction(possiblesOpcions, opcions)
                .addProduction();

        start.addProduction(opcions, Token.MAIN, Token.OPN_PARENTH, Token.CLS_PARENTH, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT);

        condicional.addProduction(Token.IF, Token.OPN_PARENTH, n0, Token.CLS_PARENTH, elseConditional);

        elseConditional.addProduction(Token.ELSE, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT)
                .addProduction();

        declaracioBucle.addProduction(Token.BUCLE, Token.OPN_PARENTH, declaracioBucleSub, Token.CLS_PARENTH, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT);

        declaracioBucleSub.addProduction(n0)
                .addProduction(Token.FOR, Token.ID, Token.IN, Token.RANGE, Token.OPN_PARENTH, Token.NUMBER, Token.CLS_PARENTH);

        n0.addProduction(n1, n0Sub);

        n0Sub.addProduction(symbolCond, n1, n0Sub)
                .addProduction();

        n1.addProduction(n2, n1Sub);

        n1Sub.addProduction(symbolSumaResta, n2, n1Sub)
                .addProduction();

        n2.addProduction(n3, n2Sub);

        n2Sub.addProduction(symbolDivMult, n3, n2Sub)
                .addProduction();

        n3.addProduction(n5, n3Sub);

        n3Sub.addProduction(Token.RAISE, n5, n3Sub)
                .addProduction();

        n5.addProduction(Token.NOT, Token.OPN_PARENTH, n0, Token.CLS_PARENTH)
                .addProduction(Token.OPN_PARENTH, n0, Token.CLS_PARENTH)
                .addProduction(id);

        sentencies.addProduction(possibleSentencies, sentencies)
                .addProduction(Token.RETURN, id, Token.EOL)
                .addProduction();

        possibleSentencies.addProduction(declaracioVariable, possibleAssignacio, Token.EOL)
                .addProduction(Token.ID, Token.ASSIGN, n0, Token.EOL)
                .addProduction(condicional)
                .addProduction(declaracioBucle);

        possibleAssignacio.addProduction(Token.ASSIGN, n0)
                .addProduction();
    }

    public GrammarAnalizer() {}

    public Production getEntryPoint() {
        return start;
    }
}
