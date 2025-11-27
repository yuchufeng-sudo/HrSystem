package com.ys.common.core.exception.file;

import com.ys.common.core.exception.base.BaseException;

/**
 * File Information Exception Class
 *
 * @author ys
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args, String msg)
    {
        super("file", code, args, msg);
    }

}
