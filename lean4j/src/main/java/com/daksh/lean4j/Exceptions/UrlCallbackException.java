package com.daksh.lean4j.Exceptions;

public class UrlCallbackException extends RuntimeException {

    public UrlCallbackException() {
        super("No callback URL registered. Please set a callback URL same as one entered while registering " +
                "aplication with LeanTesting using setUrlCallBack()");
    }
}
