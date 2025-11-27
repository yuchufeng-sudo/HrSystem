package com.ys.common.core.exception;

import com.ys.common.core.exception.base.BaseBusinessException;

/**
 * Data validation exception
 */
public class DataValidationException extends BaseBusinessException {

    public DataValidationException(String message) {
        super("DATA_VALIDATION_ERROR", message);
    }

    public DataValidationException(String field, String message) {
        super("DATA_VALIDATION_ERROR",
                String.format("Validation failed for field '%s': %s", field, message),
                field, message);
    }
}
