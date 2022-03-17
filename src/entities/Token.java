package entities;

import java.util.regex.Pattern;

public enum Token {
    IF("if"),
    ELSE("else"),
    BUCLE("loop"),
    EOL(";"),
    ASSIGN("="),
    INT("int"),
    BIG("big"),
    FLO("lf"),
    STR("str"),
    BIT("bit"),
    FUNC("func"),
    SUM("+"),
    SUBSTRACT("-"),
    MULT("*"),
    DIVIDE("/"),
    MOD("%"),
    RAISE("^"),
    NOT("¡"),
    FACT("!"),
    AND("&"),
    OR("|"),
    TRUE("true"),
    FALSE("false"),
    MAIN("main"),
    LT("<"),
    GT(">"),
    RET_TYPE(":"),
    IN("in"),
    RANGE("range"),
    COMP("=="),
    OPN_CONTEXT("{"),
    CLS_CONTEXT("}"),
    OPN_PARENTH("("),
    CLS_PARENTH(")"),
    COMMA(","),
    ID(Pattern.compile("^(\\d*\\w+[\\w\\d]*)$")),
    STRING_VALUE(Pattern.compile("^\"((?:[^\"]|(?<=\\\\)\")*)\"$")),

    // reserved characters
    NONE(Pattern.compile("^(.*)$")),
    EOF("");

    private String match;
    private Pattern reg_match;

    private Token(String match) {
        this.match = match;
    }

    private Token(Pattern match) {
        this.reg_match = match;
    }

    private boolean matches(String str) {
        if (this.match != null) return this.match.equals(str);
        else return this.reg_match.matcher(str).matches();
    }

    public static Token getMatch(String str) {
        for(Token t : Token.values()) {
            if (t.matches(str)) return t;
        }
        return Token.NONE;
    }
}
