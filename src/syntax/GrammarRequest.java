package syntax;

import entities.Token;

import java.lang.reflect.Field;
import java.util.*;

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

    private Set<Token> getFirst(Production p) {
        Set<Token> r = new HashSet<>();

        for (Object[] production : p.getProduccions()) {
            /**
             * If x is a terminal, then FIRST(x) = { ‘x’ }
             * If x -> ε, is a production rule, then add ε to FIRST(x).
             * If X -> Y1 Y2 Y3….Yn is a production,
             *      FIRST(X) = FIRST(Y1)
             *      If FIRST(Y1) contains ε then FIRST(X) = { FIRST(Y1) – ε } U { FIRST(Y2) }
             *      If FIRST (Yi) contains ε for all i = 1 to n, then add Є to FIRST(X).
             *
             * @author https://www.geeksforgeeks.org/first-set-in-syntax-analysis/
             */

            if (production.length == 0) r.add(Token.EPSILON); // ε
            else if (production[0] instanceof Token) r.add((Token) production[0]); // x is a terminal
            else {
                boolean found_epsilon = true;
                for (int x = 0; x < production.length && found_epsilon /* if FIRST(Y1) contains Є then FIRST(X) = { FIRST(Y1) – Є } U { FIRST(Y2) } */; x++) {
                    Set<Token> next = this.getFirst((Production) production[x]);
                    if (!next.remove(Token.EPSILON)) {
                        found_epsilon = false; // no hay epsilon
                    }
                    r.addAll(next);
                }
                if (found_epsilon) r.add(Token.EPSILON); // FIRST (Yi) contains ε for all i = 1 to n
            }
        }

        return r;
    }

    private Set<Token> getFirst(Object p) {
        if (p instanceof Production) return this.getFirst((Production) p);

        // si no és una producció, és un token
        Set<Token> r = new HashSet<>();
        r.add((Token) p);
        return r;
    }

    private Map<Production, Set<Token>> getFollows() {
        Map<Production, Set<Token>> r = new HashMap<>();
        for (Map.Entry<String, Production> p : this.getProductions().entrySet()) r.put(p.getValue(), new HashSet<>());

        for (Map.Entry<String, Production> _p : this.getProductions().entrySet()) {
            Production production = _p.getValue();

            /**
             * The starting production contains the EOF: FOLLOW(S) = { $ }
             * If A -> pBq is a production (where p and q can be a whole string), then FOLLOW(B) = {FIRST(q) - ε}
             * If A -> pB is a production, then FOLLOW(B) = FOLLOW(A)
             * If A -> pBq is a production and FIRST(q) contains ε, then FOLLOW(B) = { FIRST(q) – ε } U FOLLOW(A)
             *
             * @author https://www.geeksforgeeks.org/follow-set-in-syntax-analysis/
             */

            if (production.equals(this.getEntryPoint())) r.get(production).add(Token.EOF); // the starting production contains the EOF
            for (Object[] p : production.getProduccions()) {
                if (p.length == 0) continue;

                Set<Token> first;
                for (int x = 0; x < p.length - 1 /* tiene que tener algo delante */; x++) {
                    if (p[x] instanceof Production) {
                        first = this.getFirst(p[x+1]);
                        first.remove(Token.EPSILON);
                        r.get((Production) p[x]).addAll(first); // if A -> pBq is a production, then FOLLOW(B) = {FIRST(q) - ε}
                    }
                }

                if (p[p.length-1] instanceof Production) r.get((Production) p[p.length-1]).addAll(r.get(production)); // if A -> pB is a production, then FOLLOW(B) = FOLLOW(A)

                first = this.getFirst(p[p.length - 1]); // first q
                for (int x = p.length - 2; x >= 0 && first.contains(Token.EPSILON); x--) {
                    if (p[x] instanceof Production) {
                        // if A -> pBq is a production and FIRST(q) contains ε, then FOLLOW(B) = { FIRST(q) – ε } U FOLLOW(A)
                        first.remove(Token.EPSILON);
                        r.get((Production) p[x]).addAll(first);
                        r.get((Production) p[x]).addAll(r.get(production));
                    }

                    first = this.getFirst(p[x]); // q
                }
            }
        }

        return r;
    }

    public List<FirstFollowData> getFirstFollow() {
        List<FirstFollowData> r = new ArrayList<>();
        Map<Production, Set<Token>> follows = this.getFollows();
        for (Map.Entry<String, Production> e : this.getProductions().entrySet()) {
            Production p = e.getValue();
            r.add(new FirstFollowData(e.getKey(), p, this.getFirst(p), follows.get(p)));
        }
        return r;
    }
}
