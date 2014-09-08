package com.dbolshak.testtask.rest.exceptions;

/**
 * Created by dbolshak on 03.09.2014.
 */
public class ApplicationRuntimeException extends RuntimeException {
    public ApplicationRuntimeException(String message) {
        super(message);
    }

    public ApplicationRuntimeException(String message, Exception cause) {
        super(message, cause);
    }
}
