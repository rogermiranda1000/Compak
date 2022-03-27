package syntax;

import entities.Token;

import java.util.List;

public class FirstFollowData {
    private final String name;
    private final Production production;

    private final List<Token> first;
    private final List<Token> follow;

    public FirstFollowData(String name, Production production, List<Token> first, List<Token> follow) {
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

    public List<Token> getFirst() {
        return this.first;
    }

    public List<Token> getFollow() {
        return this.follow;
    }
}
