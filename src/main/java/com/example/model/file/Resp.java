package com.example.model.file;

public class Resp<T> {

    private boolean success;
    private String message;
    private T Data;

    public Resp() {
    }

    public Resp(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        Data = data;
    }

    public static Resp success() {
        return new Resp(true, "", null);
    }

    public static Resp success(Object o) {
        return new Resp(true, "", o);
    }

    public static Resp fail(String msg) {
        return new Resp(false, msg, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
