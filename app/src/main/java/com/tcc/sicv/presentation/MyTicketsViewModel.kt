package com.tcc.sicv.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.tcc.sicv.base.Result
import com.tcc.sicv.base.ResultListenerFactory
import com.tcc.sicv.data.firebase.TicketRepository
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.Ticket
import com.tcc.sicv.data.preferences.PreferencesHelper

import com.tcc.sicv.data.model.Status.LOADING

class MyTicketsViewModel(private val preferencesHelper: PreferencesHelper) : ViewModel() {
    private val flowState: MutableLiveData<FlowState<MutableList<Ticket>>> = MutableLiveData()
    private val repository: TicketRepository
    private val resultListener: Result<MutableList<Ticket>>

    init {
        resultListener = ResultListenerFactory<MutableList<Ticket>>().create(flowState)
        repository = TicketRepository()
        requestMyTickets()
    }

    fun requestMyTickets() {
        if (flowState.value?.isLoading == true) return
        flowState.postValue(FlowState(null, null, LOADING))
        repository.getTickets(preferencesHelper.email?:"", resultListener)
    }

    fun getFlowState(): LiveData<FlowState<MutableList<Ticket>>> = flowState
}
