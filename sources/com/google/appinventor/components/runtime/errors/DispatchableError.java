package com.google.appinventor.components.runtime.errors;

import com.google.appinventor.components.runtime.util.ErrorMessages;
import java.util.Arrays;

public class DispatchableError extends RuntimeError {
    private final Object[] arguments;
    private final int errorCode;

    public DispatchableError(int errorCode2) {
        super(ErrorMessages.formatMessage(errorCode2, (Object[]) null));
        this.errorCode = errorCode2;
        this.arguments = new Object[0];
    }

    public DispatchableError(int errorCode2, Object... arguments2) {
        super(ErrorMessages.formatMessage(errorCode2, arguments2));
        this.errorCode = errorCode2;
        this.arguments = arguments2;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public Object[] getArguments() {
        return Arrays.copyOf(this.arguments, this.arguments.length);
    }
}
