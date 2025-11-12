package com.ys.hr.sign.entiry;

public class ResponseEntity<T> {
    /**
     * The HTTP Status code of the response
     */
    private int statusCode;

    /**
     * The response body (Content for SUCCESS)
     */
    private T body;

    /**
     * Error Message (provided in case of Failure)
     */
    private String errorMessage;

    // Constructor
    public ResponseEntity(int statusCode, T body, String errorMessage) {
        this.statusCode = statusCode;
        this.body = body;
        this.errorMessage = errorMessage;
    }

    // Getter and Setter
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
