package com.dbolshak.testtask.exceptions;

/**
 * Our application wraps all exceptions to this one.
 */
public class ApplicationRuntimeException extends RuntimeException {
    public ApplicationRuntimeException(String message) {
        super(message);
    }

    public ApplicationRuntimeException(String message, Exception cause) {
        super(message, cause);
    }
}
