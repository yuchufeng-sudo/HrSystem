package com.ys.common.core.exception;

import com.ys.common.core.exception.base.BaseBusinessException;

/**
 * Data validation exception
 */
public class ValidationException extends BaseBusinessException {

  public ValidationException(String fieldName, String message) {
    super("VALIDATION_ERROR",
            String.format("Validation failed for field '%s': %s", fieldName, message),
            fieldName);
  }

  public ValidationException(String message) {
    super("VALIDATION_ERROR", message);
  }
}
