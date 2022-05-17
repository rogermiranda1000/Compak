package syntax;

import entities.Token;
import java.util.Set;

/**
 * Class FirstFollowData. it saves all data for first and follow algorithm.
 */
public class FirstFollowData {
    private final String name;
    private final Production production;

    private final Set<Token> first;
    private final Set<Token> follow;

    /**
     * Class constructor FirstFollow Data.
     *
     * @param name       the name
     * @param production the production
     * @param first      the first
     * @param follow     the follow
     */
    public FirstFollowData(String name, Production production, Set<Token> first, Set<Token> follow) {
        this.name = name;
        this.production = production;
        this.first = first;
        this.follow = follow;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets production.
     *
     * @return the production
     */
    public Production getProduction() {
        return this.production;
    }

    /**
     * Gets first.
     *
     * @return the first
     */
    public Set<Token> getFirst() {
        return this.first;
    }

    /**
     * Gets follow.
     *
     * @return the follow
     */
    public Set<Token> getFollow() {
        return this.follow;
    }
}
