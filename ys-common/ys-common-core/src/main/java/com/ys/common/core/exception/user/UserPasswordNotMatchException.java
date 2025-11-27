package com.ys.common.core.exception.user;

/**
 * User Password Incorrect or Non - compliant Exception Class
 * 
 * @author ys
 */
public class UserPasswordNotMatchException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException()
    {
        super("user.password.not.match", null);
    }
}
