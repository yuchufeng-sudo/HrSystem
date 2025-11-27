package com.ys.common.core.constant;

/**
 *
 *
 * @author ys
 */
public class HttpStatus
{
    /**
     * Operation SUCCESS
     */
    public static final int SUCCESS = 200;

    /**
     * ObjectCreate SUCCESS
     */
    public static final int CREATED = 201;

    /**
     *  The request has been accepted.
     */
    public static final int ACCEPTED = 202;

    /**
     * The Operation has been executed successfully, but no data is returned.
     */
    public static final int NO_CONTENT = 204;

    /**
     * The resource has been removed.
     */
    public static final int MOVED_PERM = 301;

    /**
     * Redirect
     */
    public static final int SEE_OTHER = 303;

    /**
     * The resource has not been modified.
     */
    public static final int NOT_MODIFIED = 304;

    /**
     * Parameters list error (missing, format mismatch)
     */
    public static final int BAD_REQUEST = 400;

    /**
     * Unauthorized
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * Access is restricted, and the authorization has expired.
     */
    public static final int FORBIDDEN = 403;

    /**
     * Resource or service not found.
     */
    public static final int NOT_FOUND = 404;

    /**
     * Not allowed HTTP method
     */
    public static final int BAD_METHOD = 405;

    /**
     *Resource conflict, or resource is locked.
     */
    public static final int CONFLICT = 409;

    /**
     * Unsupported data, media type
     */
    public static final int UNSUPPORTED_TYPE = 415;

    /**
     *  Internal system error.
     */
    public static final int ERROR = 500;

    /**
     *Interface Not Implemented
     */
    public static final int NOT_IMPLEMENTED = 501;

    /**
     *  System warning message
     */
    public static final int WARN = 601;
}
