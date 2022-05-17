package syntax;

import entities.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Production of the Compak grammar.
 */
public class Production {
    private final List<Object[]> produccions;

    private static void checkArgument(Object[] production) throws IllegalArgumentException {
        for (Object o : production) {
            if (!(o instanceof Production || o instanceof Token)) throw new IllegalArgumentException("L'array només pot contenir produccions i tokens! (S'ha rebut " + (o == null ? "null" : o.getClass().toString()) + ")");
        }
    }

    /**
     * Class constructor for a new Production.
     *
     * @param productions arrays containing productions or tokens must be passed n (n being the number of golds in the production).
     */
    public Production(Object[] ...productions) {
        this.produccions = new ArrayList<>();

        // check arguments
        for (Object[] p : productions) {
            Production.checkArgument(p);
            this.produccions.add(p);
        }
    }

    /**
     * Add productions.
     *
     * @param production the production
     * @return the production
     */
    public Production addProduction(Object ...production) {
        Production.checkArgument(production);
        this.produccions.add(production);
        return this;
    }

    /**
     * Gets productions.
     *
     * @return the produccions
     */
    public List<Object[]> getProductions() {
        return this.produccions;
    }
}
