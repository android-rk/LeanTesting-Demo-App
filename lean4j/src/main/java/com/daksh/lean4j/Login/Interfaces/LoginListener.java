package com.daksh.lean4j.Login.Interfaces;

import com.daksh.lean4j.Storage.AccessToken;

public interface LoginListener {

    /**
     * The onSuccess interface is executed when the OAuth process flow is completed
     * successfully and the user has been logged in.
     * With the execution of this method, AccessTokens are saved in their class under Storage
     */
    void onSuccess(AccessToken accessToken);
}
