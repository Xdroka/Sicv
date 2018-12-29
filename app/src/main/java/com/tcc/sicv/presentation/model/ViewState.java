package com.tcc.sicv.presentation.model;

public class ViewState<T> {
    public T data = null;
    public Throwable throwable = null;
    public Status status = Status.NEUTRAL;

    public ViewState(T data, Throwable throwable, Status status){
        this.data = data;
        this.throwable = throwable;
        this.status = status;
    }

    public ViewState(){}

}
