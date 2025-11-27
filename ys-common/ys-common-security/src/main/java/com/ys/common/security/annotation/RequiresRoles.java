package com.ys.common.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Role Authentication Annotation
 * User must have specified role identifiers to access the annotated method
 *
 * @author ruoyi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface RequiresRoles
{
    /**
     * Role identifiers that need to be validated
     */
    String[] value() default {};

    /**
     * Validation logic: AND | OR, default is AND
     */
    Logical logical() default Logical.AND;
}
