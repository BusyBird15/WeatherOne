package com.google.appinventor.components.runtime.errors;

public class YailRuntimeError extends RuntimeError {
    private String errorType;

    public YailRuntimeError(String message, String errorType2) {
        super(message);
        this.errorType = errorType2;
    }

    public String getErrorType() {
        return this.errorType;
    }
}
