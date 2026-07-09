package com.example.resumeapi.exception;

public class PdfRenderException extends RuntimeException {

    public PdfRenderException(String message) {
        super(message);
    }

    public PdfRenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
