package com.ys.common.core.exception;

import com.ys.common.core.exception.base.BaseBusinessException;

/**
 * Resource not found exception
 */
public class ResourceNotFoundException extends BaseBusinessException {

  public ResourceNotFoundException(String resourceName, String resourceId) {
    super("RESOURCE_NOT_FOUND",
            String.format("%s not found with id: %s", resourceName, resourceId),
            resourceName, resourceId);
  }
}
