package com.flyingpig.exception;

public class RateLimitException extends RuntimeException {
    public RateLimitException() {
        super();
    }

    public RateLimitException(String message) {
        super(message);
    }

    public RateLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public RateLimitException(Throwable cause) {
        super(cause);
    }
}
