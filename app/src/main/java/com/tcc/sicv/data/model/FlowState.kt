package com.tcc.sicv.data.model

import com.tcc.sicv.data.model.Status.*

data class FlowState<T>(
        val data: T?,
        val throwable: Throwable?,
        val status: Status
) {
    val isLoading: Boolean?
        get() = status == Status.LOADING

    companion object {
        inline fun <reified T> loading() = FlowState<T>(null, null, LOADING)
        inline fun <reified T> success(data: T) = FlowState(data, null, SUCCESS)
        inline fun <reified T> failure(e: Throwable?) = FlowState<T>(null, e, ERROR)
    }
}
