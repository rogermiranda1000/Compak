package syntax;

import entities.Token;

public class Production {
    /**
     * Producció nul·la
     */
    public static final Production EPSILON = new Production();

    private final Object [][]produccions;

    /**
     *
     * @param productions S'han de passar n (sent n el número d'ors de la producció) arrays que continguin produccions o tokens
     */
    public Production(Object[] ...productions) {
        // check arguments
        for (Object[] p : productions) {
            for (Object o : p) {
                if (!(o instanceof Production || o instanceof Token)) throw new IllegalArgumentException("L'array només pot contenir produccions i tokens!");
            }
        }

        this.produccions = productions;
    }
}
