package syntax;

public class GrammarException extends Exception {
    public GrammarException(String printInfo) {
        System.out.println(printInfo);
    }
}
