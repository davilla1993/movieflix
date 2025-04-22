package com.follysitou.movieflix_backend.exceptions;

public class TokenExpirationException extends RuntimeException {

    public TokenExpirationException() {
    }

    public TokenExpirationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TokenExpirationException(Throwable cause) {
        super(cause);
    }

    public TokenExpirationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenExpirationException(String message) {
        super(message);
    }
}
