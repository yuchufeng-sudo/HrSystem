package com.ys.common.core.exception;

import com.ys.common.core.exception.base.BaseBusinessException;

/**
 * Data access exception
 */
public class DataAccessException extends BaseBusinessException {

    public DataAccessException(String operation, String message) {
        super("DATA_ACCESS_ERROR",
                String.format("Data access error during '%s': %s", operation, message),
                operation);
    }

    public DataAccessException(String operation, String message, Throwable cause) {
        super("DATA_ACCESS_ERROR",
                String.format("Data access error during '%s': %s", operation, message),
                cause);
    }
}
