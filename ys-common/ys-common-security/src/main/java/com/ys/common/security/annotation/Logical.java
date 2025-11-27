package com.ys.common.security.annotation;

/**
 * Verification mode of permission annotation
 *
 * @author ys
 *
 */
public enum Logical
{
    /**
     * Must have all elements
     */
    AND,

    /**
     * Just need to have one of the elements
     */
    OR
}
