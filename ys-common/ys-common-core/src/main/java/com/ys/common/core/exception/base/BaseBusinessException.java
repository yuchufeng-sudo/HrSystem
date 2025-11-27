package com.ys.common.core.exception.base;

/**
 * Base business exception class
 */
public class BaseBusinessException extends RuntimeException {

    private final String errorCode;
    private final Object[] args;

    public BaseBusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.args = null;
    }

    public BaseBusinessException(String errorCode, String message, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.args = args;
    }

    public BaseBusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.args = null;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object[] getArgs() {
        return args;
    }
}
