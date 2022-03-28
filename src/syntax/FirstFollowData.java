package syntax;

import entities.Token;
import java.util.Set;

public class FirstFollowData {
    private final String name;
    private final Production production;

    private final Set<Token> first;
    private final Set<Token> follow;

    public FirstFollowData(String name, Production production, Set<Token> first, Set<Token> follow) {
        this.name = name;
        this.production = production;
        this.first = first;
        this.follow = follow;
    }

    public String getName() {
        return this.name;
    }

    public Production getProduction() {
        return this.production;
    }

    public Set<Token> getFirst() {
        return this.first;
    }

    public Set<Token> getFollow() {
        return this.follow;
    }
}
