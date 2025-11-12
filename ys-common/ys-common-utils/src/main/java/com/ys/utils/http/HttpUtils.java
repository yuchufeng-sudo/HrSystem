package com.ys.utils.http;

import com.ys.common.core.constant.Constants;
import com.ys.common.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

/**
 * General HTTP sending method
 *
 * @author ruoyi
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * Sends a GET request to the specified URL
     *
     * @param url   URL to send the request to
     * @param param Request parameters, which should be in the form of
     *              name1=value1&name2=value2.
     * @return Response result representing the remote resource
     */
    public static String sendGet(String url, String param) {
        return sendGet(url, param, Constants.UTF8);
    }

    /**
     * Sends a GET request to the specified URL
     *
     * @param url         The URL to send the request to
     * @param param       Request parameters, which should be in the form of
     *                    name1=value1&name2=value2.
     * @param contentType Encoding type
     * @return The response result representing the remote resource
     */
    public static String sendGet(String url, String param, String contentType) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = StringUtils.isNotBlank(param) ? url + "?" + param : url;
            log.info("sendGet - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("recv - {}", result);
        } catch (ConnectException e) {
            log.error("HttpUtils.sendGet invocation failed with ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            log.error("HttpUtils.sendGet invocation failed with SocketTimeoutException, url=" + url + ",param=" + param,
                    e);
        } catch (IOException e) {
            log.error("HttpUtils.sendGet invocation failed with IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            log.error("HttpsUtil.sendGet invocation failed with Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                log.error("invocation of in.close() failed with Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }
}
