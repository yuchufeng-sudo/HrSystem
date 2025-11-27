package com.ys.common.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Permission authentication: Specific permissions are required to access this method
 * @author ys
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface RequiresPermissions
{
    /**
     * NPermission code that needs to be verified
     */
    String[] value() default {};

    /**
     * Verification mode: AND | OR, default AND
     */
    Logical logical() default Logical.AND;
}
