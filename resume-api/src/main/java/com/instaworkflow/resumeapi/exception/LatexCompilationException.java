package com.instaworkflow.resumeapi.exception;

public class LatexCompilationException extends RuntimeException {
    public LatexCompilationException(String message) {
        super(message);
    }
    
    public LatexCompilationException(String message, Throwable cause) {
        super(message, cause);
    }
}
