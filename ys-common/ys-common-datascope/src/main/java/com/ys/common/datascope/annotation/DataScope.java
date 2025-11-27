package com.ys.common.datascope.annotation;

import java.lang.annotation.*;

/**
 *
 *
 * @author ys
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope
{
    /**
     *
     */
    public String deptAlias() default "";

    /**
     *
     */
    public String userAlias() default "";

    /**
     *
     */
    public String permission() default "";
}
