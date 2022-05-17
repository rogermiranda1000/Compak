package lexic;

import entities.TokenDataPair;

import java.util.List;

/**
 * The interface TokenRequest.
 */
public interface TokenRequest {
    /**
     * Request next token with data.
     *
     * @return the token data pair
     */
    public TokenDataPair requestNextToken();

    /**
     * Gets current line.
     *
     * @return the current line
     */
    public int getCurrentLine();

    /**
     * Gets current column.
     *
     * @return the current column
     */
    public int getCurrentColumn();

    /**
     * Return all tokens.
     *
     * @param tokens list with tokens
     */
    public void returnTokens(List<TokenDataPair> tokens);

    /**
     * Return token as data.
     *
     * @param token token with data
     */
    public void returnTokens(TokenDataPair token);
}
