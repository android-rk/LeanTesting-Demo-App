package com.daksh.lean4j.Login;

import android.support.annotation.NonNull;

public class ClientKeys {

    /**
     * The Consumer Key received when a new app is registered with LeanTesting
     * #Mandatory
     */
    private static String CLIENT_KEY;

    /**
     * The Consumer Secret Key received when a new app is registered with LeanTesting
     * #Mandatory
     */
    private static String CLIENT_SECRET_KEY;

    ClientKeys() {
        //Empty constructor to hinder public level access | Package level access given
    }

    /**
     * A constructor that assigns ClientKey and ClientSecretKey for use within the SDK
     * @param strClientKey The LeanTesting Client Key
     * @param strClientSecretKey The LeanTesting Secret Client key
     */
    public ClientKeys(@NonNull String strClientKey, @NonNull String strClientSecretKey) {
        CLIENT_KEY = strClientKey;
        CLIENT_SECRET_KEY = strClientSecretKey;
    }

    /**
     * A method to get the Consumer Key which will be used to access LeanTesting APIs.
     * Without it, the app will fail.
     * #MANDATORY
     * @return CLIENT_KEY
     */
    String getClientKey() {
        return CLIENT_KEY;
    }

    /**
     * A method to get the Client Secret Key which will be used to access LeanTesting APIs.
     * Without it, the app will fail.
     * #MANDATORY
     * @return CLIENT_SECRET_KEY
     */
    String getClientSecretKey() {
        return CLIENT_SECRET_KEY;
    }
}