package com.tcc.sicv.base;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.tcc.sicv.data.model.FlowState;

import static com.tcc.sicv.data.model.Status.ERROR;
import static com.tcc.sicv.data.model.Status.SUCCESS;

public class ResultListenerFactory<T> {
   public Result<T> create(@NonNull final MutableLiveData<FlowState<T>> liveData){
       return new Result<T>() {
           @Override
           public void onSuccess(T data) {
               liveData.postValue(new FlowState<>(data, null, SUCCESS));
           }

           @Override
           public void onFailure(Throwable throwable) {
                liveData.postValue(new FlowState<T>(null, throwable, ERROR));
           }
       };
   }
}
