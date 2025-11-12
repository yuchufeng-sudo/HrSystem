package com.ys.auth.form;

/**
 * USER Login Object
 *
 * @author ruoyi
 */
public class LoginBody
{
    /**
     * USER Name
     */
    private String username;

    /**
     * USER Password
     */
    private String password;

    private String email;

    private String code;

    private String token;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
