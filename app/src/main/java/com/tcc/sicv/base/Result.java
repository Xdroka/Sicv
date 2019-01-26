package com.tcc.sicv.base;

public class Result<T> {
    private T data;
    private Throwable throwable;

    public T getData() {
        return data;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Result(T data, Throwable throwable) {
        this.data = data;
        this.throwable = throwable;
    }
}
