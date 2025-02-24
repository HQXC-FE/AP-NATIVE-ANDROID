package com.xtree.live.exception;

import androidx.annotation.NonNull;

import java.io.IOException;

public class ApiRequestException extends IOException {
    public ApiRequestException() {
    }

    public ApiRequestException(String message) {
        super(message);
    }

    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiRequestException(Throwable cause) {
        super(cause);
    }

    @NonNull
    @Override
    public String toString() {
        String s = getClass().getSimpleName();
        String message = getLocalizedMessage();
        return (message != null) ? (s + ": " + message) : s;
    }
}

