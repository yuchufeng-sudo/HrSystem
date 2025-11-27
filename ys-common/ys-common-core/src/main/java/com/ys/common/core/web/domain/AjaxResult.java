package com.ys.common.core.web.domain;

import com.ys.common.core.constant.HttpStatus;
import com.ys.common.core.utils.StringUtils;

import java.util.HashMap;
import java.util.Objects;

/**
 * "Operation Message reminder"
 *
 * @author ys
 */
public class AjaxResult extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /** Status code */
    public static final String CODE_TAG = "code";

    /** Return Content  */
    public static final String MSG_TAG = "msg";

    /** Data Object */
    public static final String DATA_TAG = "data";

    /**
     * Initialize a new Create AjaxResult Object to represent an empty Message。
     */
    public AjaxResult()
    {
    }

    /**
     *Initialize a newly created AjaxResult Object.
     *
     * @param code Status code
     * @param msg Return Content
     */
    public AjaxResult(int code, String msg)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     *Initialize a newly created AjaxResult Object.
     *
     * @param code Status code
     * @param msg Return Content
     * @param dataObject
     */
    public AjaxResult(int code, String msg, Object data)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * Return SUCCESSMessage
     *
     * @return SUCCESSMessage
     */
    public static AjaxResult success()
    {
        return AjaxResult.success("Operation SUCCESS");
    }

    /**
     * Return
     *
     * @return SUCCESSMessage
     */
    public static AjaxResult success(Object data)
    {
        return AjaxResult.success("Operation SUCCESS", data);
    }

    /**
     * Return SUCCESSMessage
     *
     * @param msg Return Content
     * @return SUCCESSMessage
     */
    public static AjaxResult success(String msg)
    {
        return AjaxResult.success(msg, null);
    }

    /**
     * Return SUCCESSMessage
     *
     * @param msg Return Content
     * @param dataObject
     * @return SUCCESSMessage
     */
    public static AjaxResult success(String msg, Object data)
    {
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * Return Warning Message
     *
     * @param msg Return Content
     * @return Warning Message
     */
    public static AjaxResult warn(String msg)
    {
        return AjaxResult.warn(msg, null);
    }

    /**
     * Return Warning Message
     *
     * @param msg Return Content
     * @param dataObject
     * @return Warning Message
     */
    public static AjaxResult warn(String msg, Object data)
    {
        return new AjaxResult(HttpStatus.WARN, msg, data);
    }

    /**
     * Return Error Message
     *
     * @return Error Message
     */
    public static AjaxResult error()
    {
        return AjaxResult.error("Operation Failure");
    }

    /**
     * Return Error Message
     *
     * @param msg Return Content
     * @return Error Message
     */
    public static AjaxResult error(String msg)
    {
        return AjaxResult.error(msg, null);
    }

    /**
     * Return Error Message
     *
     * @param msg Return Content
     * @param dataObject
     * @return Error Message
     */
    public static AjaxResult error(String msg, Object data)
    {
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }

    /**
     * Return Error Message
     *
     * @param code Status code
     * @param msg Return Content
     * @return Error Message
     */
    public static AjaxResult error(int code, String msg)
    {
        return new AjaxResult(code, msg, null);
    }

    /**
     *
     *
     * @return Result
     */
    public boolean isSuccess()
    {
        return Objects.equals(HttpStatus.SUCCESS, this.get(CODE_TAG));
    }

    /**
     *
     *
     * @return Result
     */
    public boolean isWarn()
    {
        return Objects.equals(HttpStatus.WARN, this.get(CODE_TAG));
    }

    /**
     *
     *
     * @return Result
     */
    public boolean isError()
    {
        return Objects.equals(HttpStatus.ERROR, this.get(CODE_TAG));
    }

    /**
     *
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public AjaxResult put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }
}
