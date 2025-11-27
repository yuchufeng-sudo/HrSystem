package com.ys.common.log.annotation;

import com.ys.common.log.enums.BusinessType;
import com.ys.common.log.enums.OperatorType;

import java.lang.annotation.*;

/**
 * Custom Operation Log Record Annotation
 *
 * @author ys
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{
    /**
     *  Module
     */
    public String title() default "";

    /**
     *
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     *
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * Whether to save the Parameters of the Request
     */
    public boolean isSaveRequestData() default true;

    /**
     *
     */
    public boolean isSaveResponseData() default true;

    /**
     *
     */
    public String[] excludeParamNames() default {};
}
