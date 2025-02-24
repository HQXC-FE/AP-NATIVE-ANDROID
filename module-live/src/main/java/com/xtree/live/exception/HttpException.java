package com.xtree.live.exception;

public class HttpException extends RuntimeException {
    private final int code;
    private final String message;

    private static String getMessage(String apiPath, int httpCode, String msg) {
        return apiPath + "\nHTTP " + httpCode + " " + msg;
    }

    public HttpException(String apiPath, int httpCode,String message) {
        super(getMessage(apiPath, httpCode, message));
        this.code = httpCode;
        this.message = message;
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
