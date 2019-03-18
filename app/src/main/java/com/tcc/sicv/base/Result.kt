package com.tcc.sicv.base

interface Result<T> {
    fun onSuccess(data: T)
    fun onFailure(throwable: Throwable?)
}
