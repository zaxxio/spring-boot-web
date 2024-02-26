package org.wsd.app.security.auth.impl;

public class TwoFactorFailedException extends Exception {

    public TwoFactorFailedException(String message) {
        super(message);
    }

}
