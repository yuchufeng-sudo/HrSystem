package com.ys.common.core.utils;

import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.constant.TokenConstants;
import com.ys.common.core.text.Convert;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;

/**
 *
 *
 * @author ys
 */
public class JwtUtils
{
    public static final String secret = TokenConstants.SECRET;

    /**
     *
     *
     * @param claims
     * @return
     */
    public static String createToken(Map<String, Object> claims)
    {
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     *
     *
     * @param token
     * @return
     */
    public static Claims parseToken(String token)
    {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     *
     *
     * @param token
     * @return
     */
    public static String getUserKey(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     *
     *
     * @param claims
     * @return User ID
     */
    public static String getUserKey(Claims claims)
    {
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     *
     *
     * @param token
     * @return User ID
     */
    public static String getUserId(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     *
     *
     * @param claims
     * @return User ID
     */
    public static String getUserId(Claims claims)
    {
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /*
    * Obtain Enterprise Number
    * */
    public static String getEnterpriseId(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_ENTERPRISE_ID);
    }

    /*
    * Obtain Enterprise Number
    * */
    public static String getEnterpriseId(Claims claims)
    {
        return getValue(claims, SecurityConstants.DETAILS_ENTERPRISE_ID);
    }

    /**
     *
     *
     * @param token
     * @return User Name
     */
    public static String getUserName(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     *
     *
     * @param claims
     * @return User Name
     */
    public static String getUserName(Claims claims)
    {
        return getValue(claims, SecurityConstants.DETAILS_USERNAME);
    }

    /**
     *
     *
     * @param claims
     * @param key
     * @return
     */
    public static String getValue(Claims claims, String key)
    {
        return Convert.toStr(claims.get(key), "");
    }
}
