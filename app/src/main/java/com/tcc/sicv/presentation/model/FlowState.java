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

    public <T> FlowState(){}

    public void loading(){
        data = null;
        throwable = null;
        status = Status.LOADING;
    }

    public FlowState<T> success(T data){
        this.data = data;
        throwable = null;
        status = Status.SUCCESS;
        return this;
    }

    public FlowState<T> error(Throwable throwable){
        data = null;
        this.throwable = throwable;
        status = Status.ERROR;
        return this;
    }

    public Boolean isLoading(){ return status == Status.LOADING; }

    public Boolean hasData(){ return data != null; }
}
