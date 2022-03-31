package lexic;

import entities.TokenDataPair;

public interface TokenRequest {
    public TokenDataPair requestNextToken();
    public int getCurrentLine();
    public int getCurrentColumn();
}
