package com.ys.common.core.exception;

import com.ys.common.core.exception.base.BaseBusinessException;

/**
 * Database operation exception
 */
public class DatabaseOperationException extends BaseBusinessException {
    public DatabaseOperationException(String operation, Throwable cause) {
        super("DATABASE_ERROR",
                String.format("Database operation failed: %s", operation),
                cause);
    }

    public DatabaseOperationException(String operation, String details) {
        super("DATABASE_ERROR",
                String.format("Database operation failed: %s. Details: %s", operation, details));
    }
}
