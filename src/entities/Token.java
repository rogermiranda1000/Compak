package entities;

import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
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
    FOR("for"),
    ID(Pattern.compile("^(\\d*\\w+[\\w\\d]*)$")),
    NUMBER(Pattern.compile("^(\\d+)$")),
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

    @Nullable
    private String matches(String str) {
        if (this.match != null) return this.match.equals(str) ? str : null;
        else {
            Matcher m = this.reg_match.matcher(str);
            return m.matches() ? m.group(1) : null;
        }
    }

    public static TokenDataPair getMatch(String str) {
        String match;
        for(Token t : Token.values()) {
            if ((match = t.matches(str)) != null) {
                return new TokenDataPair(t, match);
            }
        }
        return new TokenDataPair(Token.NONE);
    }
}
