package com.tcc.sicv.base

import android.arch.lifecycle.MutableLiveData

import com.tcc.sicv.data.model.FlowState

import com.tcc.sicv.data.model.Status.ERROR
import com.tcc.sicv.data.model.Status.SUCCESS

class ResultListenerFactory<T> {
    fun create(liveData: MutableLiveData<FlowState<T>>): Result<T> {
        return object : Result<T> {
            override fun onSuccess(data: T) {
                liveData.postValue(FlowState(data, null, SUCCESS))
            }

            override fun onFailure(throwable: Throwable?) {
                liveData.postValue(FlowState(null, throwable, ERROR))
            }
        }
    }
}
