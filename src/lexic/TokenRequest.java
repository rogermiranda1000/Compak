package lexic;

import entities.TokenDataPair;

import java.util.List;

public interface TokenRequest {
    public TokenDataPair requestNextToken();
    public int getCurrentLine();
    public int getCurrentColumn();
    public void returnTokens(List<TokenDataPair> tokens);
}
