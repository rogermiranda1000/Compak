package syntax;

import entities.Token;

import java.util.List;

public class FirstFollowTest extends GrammarRequest {
    protected static final Production E = new Production(),
            Ep = new Production(),
            T = new Production(),
            Tp = new Production(),
            F = new Production();

    static {
        E.addProduction(T, Ep, Token.EOF);
        Ep.addProduction(Token.SUM, T, Ep)
                .addProduction();
        T.addProduction(F, Tp);
        Tp.addProduction(Token.MULT, F, Tp)
                .addProduction();
        F.addProduction(Token.ID)
                .addProduction(Token.OPN_PARENTH, E, Token.CLS_PARENTH);
    }

    @Override
    public Production getEntryPoint() {
        return E;
    }

    public static void main(String[] args) {
        GrammarRequest gr = new FirstFollowTest();
        List<FirstFollowData> ff = gr.getFirstFollow();

        System.out.println();
    }
}
