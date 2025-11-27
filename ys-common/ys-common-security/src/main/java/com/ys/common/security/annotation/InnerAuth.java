package com.ys.common.security.annotation;

import java.lang.annotation.*;

/**
 *
 * Internal authentication annotation
 * @author ys
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InnerAuth
{
    /**
     * Verify user information
     */
    boolean isUser() default false;
}
