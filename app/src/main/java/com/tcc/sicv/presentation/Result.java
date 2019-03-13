package com.tcc.sicv.presentation;

public interface Result<T> {
    void onSucess(T data);
    void onFailure(Throwable throwable);
}
