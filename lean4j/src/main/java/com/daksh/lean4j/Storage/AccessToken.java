package com.daksh.lean4j.Storage;

public class AccessToken {

    //object for singleTon pattern
    private static AccessToken accessToken;

    //Access token to be used to make API calls
    private static String ACCESS_TOKEN;

    //The type of token
    private static String TOKEN_TYPE;

    //Validity in millis
    private static Long TTL;

    private AccessToken() {
        //Empty Constructor made private to hinder creation of objects
    }

    /**
     * A method to implement singlePattern
     * @return AccessToken
     */
    public static AccessToken getInstance() {
        if(accessToken != null)
            return accessToken;
        else
            return new AccessToken();
    }

    /**
     * Returns the AccessToken
     * @return String
     */
    public static String getAccessToken() {
        return ACCESS_TOKEN;
    }

    /**
     * Assigns the AccessToken
     * @param accessToken AccessToken
     */
    public static void setToken(String accessToken) {
        ACCESS_TOKEN = accessToken;
    }

    /**
     * Returns the Token Type
     * @return String
     */
    public static String getTokenType() {
        return TOKEN_TYPE;
    }

    /**
     * Sets the Token type
     * @param tokenType Token type
     */
    public static void setTokenType(String tokenType) {
        TOKEN_TYPE = tokenType;
    }

    /**
     * Returns the TTL for the Token
     * @return
     */
    public static Long getTTL() {
        return TTL;
    }

    /**
     * Sets the Time to live for the access token received
     * @param TTL TTl
     */
    public static void setTTL(Long TTL) {
        AccessToken.TTL = TTL;
    }
}
