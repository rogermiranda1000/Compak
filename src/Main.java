import entities.Token;
import entities.TokenDataPair;
import lexic.TokenBuffer;
import lexic.TokenRequest;
import preprocesser.CodeProcessor;
import preprocesser.LineRequest;

import java.io.FileNotFoundException;

public class Main {
    private static final String PATH = "file.sus";

    public static void main(String[] args) throws FileNotFoundException {
        LineRequest lr = new CodeProcessor(Main.PATH);
        TokenRequest tr = new TokenBuffer(lr);

        TokenDataPair token;
        while ((token = tr.requestNextToken()).getToken() != Token.EOF) System.out.println(token);
    }
}
