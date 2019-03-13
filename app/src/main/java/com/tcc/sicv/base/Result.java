package com.tcc.sicv.base;

public interface Result<T> {
    void onSuccess(T data);
    void onFailure(Throwable throwable);
}
