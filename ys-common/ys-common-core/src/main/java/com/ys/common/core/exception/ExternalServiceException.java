package com.ys.common.core.exception;

import com.ys.common.core.exception.base.BaseBusinessException;

/**
 * External service exception
 */
public class ExternalServiceException extends BaseBusinessException {

  public ExternalServiceException(String serviceName, String message) {
    super("EXTERNAL_SERVICE_ERROR",
            String.format("External service '%s' error: %s", serviceName, message),
            serviceName);
  }

  public ExternalServiceException(String serviceName, String message, Throwable cause) {
    super("EXTERNAL_SERVICE_ERROR",
            String.format("External service '%s' error: %s", serviceName, message),
            cause);
  }
}
