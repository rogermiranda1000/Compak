package entities;

import java.util.regex.Pattern;

public enum Token {
    IF("if"),
    ELSE("else"),
    PARENTHESIS_OPEN("("),
    PARENTHESIS_CLOSE(")"),
    VARIABLE(Pattern.compile("^\\d*\\w+\\d*$")),
    NONE(Pattern.compile("."));

    private String match;
    private Pattern reg_match;

    private Token(String match) {
        this.match = match;
    }

    private Token(Pattern match) {
        this.reg_match = match;
    }

    private boolean matches(String str) {
        if (this.match != null) return this.match.matches(str);
        else return this.reg_match.matcher(str).matches();
    }

    public static Token getMatch(String str) {
        for(Token t : Token.values()) {
            if (t.matches(str)) return t;
        }
        return Token.NONE;
    }
}
