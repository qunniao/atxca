package com.atxca.aop;

public class LogicRuntimeException extends RuntimeException {
    private int status;

    public LogicRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogicRuntimeException(String message, int status) {
        super(message);
        this.status = status;
    }

    public LogicRuntimeException(String message, Throwable cause, int status) {
        super(message, cause);
        this.status = status;
    }

    public LogicRuntimeException(String message) {
        super(message);
    }

    public LogicRuntimeException(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
