package syntax;

import entities.Token;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GrammarRequest {
    public abstract Production getEntryPoint();

    public Map<String, Production> getProductions() {
        HashMap<String, Production> r = new HashMap<>();
        try {
            for (Field f : this.getClass().getDeclaredFields()) {
                if (!(f.getType().equals(Production.class))) continue;
                r.put(f.getName(), (Production)f.get(this));
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return r;
    }

    private static List<Token> getFirst(Production p) {
        List<Token> r = new ArrayList<>();

        for (Object[] production : p.getProduccions()) {
            /**
             * If x is a terminal, then FIRST(x) = { ‘x’ }
             * If x-> ε, is a production rule, then add ε to FIRST(x).
             * If X->Y1 Y2 Y3….Yn is a production,
             *      FIRST(X) = FIRST(Y1)
             *      If FIRST(Y1) contains Є then FIRST(X) = { FIRST(Y1) – Є } U { FIRST(Y2) }
             *      If FIRST (Yi) contains Є for all i = 1 to n, then add Є to FIRST(X).
             *
             * @author https://www.geeksforgeeks.org/first-set-in-syntax-analysis/
             */

            if (production.length == 0) r.add(Token.EPSILON); // ε
            else if (production[0] instanceof Token) r.add((Token) production[0]); // x is a terminal
            else {
                boolean found_epsilon = true;
                for (int x = 0; x < production.length && found_epsilon /* if FIRST(Y1) contains Є then FIRST(X) = { FIRST(Y1) – Є } U { FIRST(Y2) } */; x++) {
                    List<Token> next = GrammarRequest.getFirst((Production) production[x]);
                    if (!next.remove(Token.EPSILON)) {
                        found_epsilon = false; // no hay epsilon
                    }
                    r.addAll(next);
                }
                if (found_epsilon) r.add(Token.EPSILON); // FIRST (Yi) contains Є for all i = 1 to n
            }
        }

        return r;
    }

    private static List<Token> getFollow(Production p) {
        return null;
    }

    public List<FirstFollowData> getFirstFollow() {
        List<FirstFollowData> r = new ArrayList<>();
        for (Map.Entry<String, Production> e : this.getProductions().entrySet()) {
            Production p = e.getValue();
            r.add(new FirstFollowData(e.getKey(), p, GrammarRequest.getFirst(p), GrammarRequest.getFollow(p)));
        }
        return r;
    }
}
