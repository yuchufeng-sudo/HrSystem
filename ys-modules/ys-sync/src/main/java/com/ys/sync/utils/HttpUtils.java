package com.ys.sync.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.sync.config.ShiftcareProperties;
import com.ys.sync.domain.ErrorResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/30
 */
@Component
public class HttpUtils {

    private final CloseableHttpClient httpClient;
    private final ShiftcareProperties shiftcareProperties;
    private final ObjectMapper objectMapper;
    private static final String ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX";

    public HttpUtils(CloseableHttpClient httpClient,
                            ShiftcareProperties shiftcareProperties,
                            ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.shiftcareProperties = shiftcareProperties;
        this.objectMapper = objectMapper;
    }

    /**
     * Generic API invocation method.
     *
     * @param path          API endpoint path (e.g., /api/v3/leaves)
     * @param httpMethod    HTTP method (GET/POST/PUT/DELETE etc.)
     * @param queryParams   Query parameters (will be appended to URL)
     * @param requestBody   Request body parameters (use List<NameValuePair> for form data,
     *                      String or entity class for JSON)
     * @param responseType  Target class for response parsing
     * @param <T>           Generic type of the response object
     * @return Parsed response object of type T
     */
    public <T> T callApi(String path,
                         HttpMethod httpMethod,
                         Map<String, Object> queryParams,
                         Object requestBody,
                         Class<T> responseType) {
        try {
            // 1. Build full URL
            String baseUrl = shiftcareProperties.getApiUrl();
            URIBuilder uriBuilder = new URIBuilder(baseUrl + path);

            // 2. Add query parameters
            if (queryParams != null && !queryParams.isEmpty()) {
                for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value != null) {
                        String paramValue = formatParamValue(value);
                        uriBuilder.addParameter(key, paramValue);
                    }
                }
            }

            URI uri = uriBuilder.build();

            // 3. Create HTTP request
            HttpUriRequest request = createHttpRequest(uri, httpMethod);

            // 4. Set authentication header
            setAuthHeader(request);

            // 5. Set request body
            if (requestBody != null) {
                setRequestBody(request, requestBody);
            }

            // 6. Execute request and handle response
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode >= 200 && statusCode < 300) {
                    HttpEntity responseEntity = response.getEntity();
                    if (responseEntity != null) {
                        String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                        return objectMapper.readValue(responseBody, responseType);
                    }
                } else {
                    String errorBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    throw new RuntimeException(String.format(
                            "API request failed: status=%d, path=%s, error=%s",
                            statusCode, path, errorBody));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error calling API: " + path, e);
        }
        return null;
    }

    // ------------------------------
    // Form parameter building utilities
    // ------------------------------

    /**
     * Create empty form parameters list
     */
    public List<NameValuePair> createFormParams() {
        return new ArrayList<>();
    }

    /**
     * Add parameter to form (skip null values)
     * Support: String, Number, Boolean, Date
     */
    public void addFormParam(List<NameValuePair> formParams, String key, Object value) {
        if (formParams == null || key == null || value == null) {
            return;
        }
        String paramValue = formatFormParamValue(value);
        formParams.add(new BasicNameValuePair(key, paramValue));
    }

    /**
     * Add multi-value parameter (e.g., user_ids[] = 1&user_ids[] = 2)
     */
    public void addMultiValueFormParam(List<NameValuePair> formParams, String key, List<?> values) {
        if (formParams == null || key == null || values == null || values.isEmpty()) {
            return;
        }
        for (Object value : values) {
            if (value != null) {
                String paramValue = formatFormParamValue(value);
                formParams.add(new BasicNameValuePair(key, paramValue));
            }
        }
    }

    /**
     * Format form parameter values (handle Date type with ISO 8601)
     */
    private String formatFormParamValue(Object value) {
        if (value instanceof Date) {
            // Use SimpleDateFormat for Date (note: create new instance for thread-safety)
            SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_PATTERN);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.format((Date) value);
        } else if (value instanceof Boolean) {
            return value.toString().toLowerCase();
        }
        return value.toString();
    }

    /**
     * Formats parameter values (handles special types like ZonedDateTime).
     *
     * @param value The parameter value to format
     * @return Formatted string representation of the value
     */
    private String formatParamValue(Object value) {
        if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_PATTERN);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.format((Date) value);
        }
        return value.toString();
    }

    /**
     * Creates an HTTP request object based on the specified method.
     *
     * @param uri    The request URI
     * @param method The HTTP method
     * @return HttpUriRequest instance corresponding to the method
     */
    private HttpUriRequest createHttpRequest(URI uri, HttpMethod method) {
        switch (method) {
            case GET:
                return new HttpGet(uri);
            case POST:
                return new HttpPost(uri);
            case PUT:
                return new HttpPut(uri);
            case DELETE:
                return new HttpDelete(uri);
            case PATCH:
                return new HttpPatch(uri);
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }

    /**
     * Sets the Basic authentication header for the request.
     *
     * @param request The HTTP request to configure
     */
    private void setAuthHeader(HttpUriRequest request) {
        String auth = shiftcareProperties.getUsername() + ":" + shiftcareProperties.getPassword();
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + new String(encodedAuth);
        request.setHeader("Authorization", authHeader);
    }

    /**
     * Sets the request body, handling different types (form parameters, JSON entities, etc.).
     *
     * @param request    The HTTP request
     * @param requestBody The body content to set
     * @throws Exception If there's an error processing the request body
     */
    private void setRequestBody(HttpUriRequest request, Object requestBody) throws Exception {
        if (!(request instanceof HttpEntityEnclosingRequest)) {
            throw new IllegalArgumentException("HTTP method " + request.getMethod() + " does not support request body");
        }

        HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
        HttpEntity entity;

        // Handle form parameters
        if (requestBody instanceof List) {
            List<?> list = (List<?>) requestBody;
            if (!list.isEmpty() && list.get(0) instanceof NameValuePair) {
                @SuppressWarnings("unchecked")
                List<? extends NameValuePair> formParams = (List<? extends NameValuePair>) list;
                entity = new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8);
                entityRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            } else {
                // Convert list to JSON
                String json = objectMapper.writeValueAsString(requestBody);
                entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            }
        }
        // Handle JSON entities
        else if (!(requestBody instanceof String)) {
            String json = objectMapper.writeValueAsString(requestBody);
            entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        }
        // Handle string content (JSON or other formats)
        else {
            entity = new StringEntity((String) requestBody, StandardCharsets.UTF_8);
        }

        entityRequest.setEntity(entity);
    }

    /**
     * Enumeration of supported HTTP methods.
     */
    public enum HttpMethod {
        GET, POST, PUT, DELETE, PATCH
    }

}
