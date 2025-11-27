package com.ys.common.core.exception;


import com.ys.common.core.exception.base.BaseBusinessException;

/**
 * Permission denied exception
 */
public class PermissionDeniedException extends BaseBusinessException {

    public PermissionDeniedException(String message) {
        super("PERMISSION_DENIED", message);
    }

    public PermissionDeniedException(String resource, String action) {
        super("PERMISSION_DENIED",
                String.format("Permission denied for %s on %s", action, resource),
                resource, action);
    }
}
