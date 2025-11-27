package com.ys.common.core.utils;

import com.alibaba.fastjson2.JSON;
import com.ys.common.core.constant.Constants;
import com.ys.common.core.domain.R;
import com.ys.common.core.text.Convert;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 *
 *
 * @author ys
 */
public class ServletUtils
{
    /**
     * Obtain StringParameters
     */
    public static String getParameter(String name)
    {
        return Objects.requireNonNull(getRequest()).getParameter(name);
    }

    /**
     * Obtain StringParameters
     */
    public static String getParameter(String name, String defaultValue)
    {
        return Convert.toStr(Objects.requireNonNull(getRequest()).getParameter(name), defaultValue);
    }

    /**
     * Obtain IntegerParameters
     */
    public static Integer getParameterToInt(String name)
    {
        return Convert.toInt(Objects.requireNonNull(getRequest()).getParameter(name));
    }

    /**
     * Obtain IntegerParameters
     */
    public static Integer getParameterToInt(String name, Integer defaultValue)
    {
        return Convert.toInt(Objects.requireNonNull(getRequest()).getParameter(name), defaultValue);
    }

    /**
     * Obtain BooleanParameters
     */
    public static Boolean getParameterToBool(String name)
    {
        return Convert.toBool(Objects.requireNonNull(getRequest()).getParameter(name));
    }

    /**
     * Obtain BooleanParameters
     */
    public static Boolean getParameterToBool(String name, Boolean defaultValue)
    {
        return Convert.toBool(Objects.requireNonNull(getRequest()).getParameter(name), defaultValue);
    }

    /**
     *  all Request Parameters
     *
     * @param request  RequestObject{@link ServletRequest}
     * @return Map
     */
    public static Map<String, String[]> getParams(ServletRequest request)
    {
        final Map<String, String[]> map = request.getParameterMap();
        return Collections.unmodifiableMap(map);
    }

    /**
     *  all Request Parameters
     *
     * @param request  RequestObject{@link ServletRequest}
     * @return Map
     */
    public static Map<String, String> getParamMap(ServletRequest request)
    {
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : getParams(request).entrySet())
        {
            params.put(entry.getKey(), StringUtils.join(entry.getValue(), ","));
        }
        return params;
    }

    /**
     * Obtain request
     */
    public static HttpServletRequest getRequest()
    {
        try
        {
            return Objects.requireNonNull(getRequestAttributes()).getRequest();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtain response
     */
    public static HttpServletResponse getResponse()
    {
        try
        {
            return Objects.requireNonNull(getRequestAttributes()).getResponse();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtain session
     */
    public static HttpSession getSession()
    {
        return Objects.requireNonNull(getRequest()).getSession();
    }

    public static ServletRequestAttributes getRequestAttributes()
    {
        try
        {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            return (ServletRequestAttributes) attributes;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String getHeader(HttpServletRequest request, String name)
    {
        String value = request.getHeader(name);
        if (StringUtils.isEmpty(value))
        {
            return StringUtils.EMPTY;
        }
        return urlDecode(value);
    }

    public static Map<String, String> getHeaders(HttpServletRequest request)
    {
        Map<String, String> map = new LinkedCaseInsensitiveMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        if (enumeration != null)
        {
            while (enumeration.hasMoreElements())
            {
                String key = enumeration.nextElement();
                String value = request.getHeader(key);
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     *
     *
     * @param response
     * @param string
     */
    public static void renderString(HttpServletResponse response, String string)
    {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request)
    {
        String accept = request.getHeader("accept");
        if (accept != null && accept.contains("application/json"))
        {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest"))
        {
            return true;
        }

        String uri = request.getRequestURI();
        if (StringUtils.inStringIgnoreCase(uri, ".json", ".xml"))
        {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        return StringUtils.inStringIgnoreCase(ajax, "json", "xml");
    }

    /**
     *
     *
     * @param str Content
     * @return
     */
    public static String urlEncode(String str)
    {
        try
        {
            return URLEncoder.encode(str, Constants.UTF8);
        }
        catch (UnsupportedEncodingException e)
        {
            return StringUtils.EMPTY;
        }
    }

    /**
     * Content
     *
     * @param str Content
     * @return
     */
    public static String urlDecode(String str)
    {
        try
        {
            return URLDecoder.decode(str, Constants.UTF8);
        }
        catch (UnsupportedEncodingException e)
        {
            return StringUtils.EMPTY;
        }
    }

    /**
     *
     *
     * @param response ServerHttpResponse
     * @param value
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value)
    {
        return webFluxResponseWriter(response, HttpStatus.OK, value, R.FAIL);
    }

    /**
     *
     *
     * @param response ServerHttpResponse
     * @param code
     * @param value
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value, int code)
    {
        return webFluxResponseWriter(response, HttpStatus.OK, value, code);
    }

    /**
     *
     *
     * @param response ServerHttpResponse
     * @param status
     * @param code
     * @param value
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, HttpStatus status, Object value, int code)
    {
        return webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, status, value, code);
    }

    /**
     *
     *
     * @param response ServerHttpResponse
     * @param contentType content-type
     * @param status
     * @param code
     * @param value
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String contentType, HttpStatus status, Object value, int code)
    {
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        R<?> result = R.fail(code, value.toString());
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONString(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }
}
