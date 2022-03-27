package syntax;

import entities.Token;

import java.util.ArrayList;
import java.util.List;

public class Production {
    /**
     * Producció nul·la
     */
    public static final Production EPSILON = new Production();

    private final List<Object[]> produccions;

    private static void checkArgument(Object[] production) throws IllegalArgumentException {
        for (Object o : production) {
            if (!(o instanceof Production || o instanceof Token)) throw new IllegalArgumentException("L'array només pot contenir produccions i tokens! (S'ha rebut " + (o == null ? "null" : o.getClass().toString()) + ")");
        }
    }

    /**
     *
     * @param productions S'han de passar n (sent n el número d'ors de la producció) arrays que continguin produccions o tokens
     */
    public Production(Object[] ...productions) {
        this.produccions = new ArrayList<>();

        // check arguments
        for (Object[] p : productions) {
            Production.checkArgument(p);
            this.produccions.add(p);
        }
    }

    public Production addProduction(Object ...production) {
        Production.checkArgument(production);
        this.produccions.add(production);
        return this;
    }
}
