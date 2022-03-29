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
    private static Production digit;
    private static Production letter;
    private static Production anyCharacter;
    private static Production sentencies;
    private static Production possibleSentencies;
    private static Production possibleAssignacio;
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

        declaracioBucle = new Production(
                new Object[]{Token.BUCLE, Token.OPN_PARENTH, declaracioBucleSub, Token.CLS_PARENTH, Token.OPN_CONTEXT, sentencies, Token.CLS_CONTEXT}
        );

        declaracioBucleSub = new Production(
                new Object[]{n0},
                new Object[]{Token.FOR, nomVariable, Token.IN, Token.RANGE, Token.OPN_PARENTH, valueNumber, Token.CLS_PARENTH}
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
                new Object[]{Token.ID}
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
        return this.start;
    }
}
