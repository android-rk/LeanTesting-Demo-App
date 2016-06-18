package com.daksh.lean4j.Exceptions;

public class APIKeyException extends RuntimeException {

    public APIKeyException() {
        super("LeanTesting client and client secret Keys missing. Please set Client and ClientSecret keys. " +
                "Refer https://github.com/dakshsrivastava/Loglr/blob/master/README.md for details.");
    }
}
