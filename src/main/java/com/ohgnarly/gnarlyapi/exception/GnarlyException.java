package com.ohgnarly.gnarlyapi.exception;

public class GnarlyException extends Exception {
    public GnarlyException(String message) {
        super(message);
    }

    public GnarlyException(String message, Throwable cause) {
        super(message, cause);
    }

    public GnarlyException(Throwable cause) {
        super(cause);
    }
}
