package com.tcc.sicv.presentation.model;

public class FlowState<T> {
    private T data = null;
    private Throwable throwable = null;
    private Status status = Status.NEUTRAL;

    public T getData() {
        return data;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public Status getStatus() {
        return status;
    }

    public FlowState(T data, Throwable throwable, Status status){
        this.data = data;
        this.throwable = throwable;
        this.status = status;
    }

    public FlowState(){}

}
