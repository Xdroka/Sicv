package com.tcc.sicv.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.tcc.sicv.base.ResultListenerFactory
import com.tcc.sicv.data.firebase.TicketRepository
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.FlowState.Companion.loading
import com.tcc.sicv.data.model.Status.*
import com.tcc.sicv.data.model.Ticket
import com.tcc.sicv.data.preferences.PreferencesHelper

class TicketDetailsViewModel(
        ticketJson: String, preferencesHelper: PreferencesHelper,
        loadedTicket: Boolean
) : ViewModel() {
    private val flowState: MutableLiveData<FlowState<Boolean>>
    var ticket: Ticket? = null
        private set

    init {
        handleWithJson(ticketJson)
        flowState = MutableLiveData()
        if (loadedTicket) {
            flowState.postValue(FlowState(true, null, SUCCESS))
        }
        getTickets(preferencesHelper)
    }

    private fun getTickets(preferencesHelper: PreferencesHelper) {
        val resultListener = ResultListenerFactory<Boolean>().create(flowState)
        flowState.postValue(loading())
        val repository = TicketRepository()
        ticket?.let { repository.setTicket(preferencesHelper.email?: "", it, resultListener) }
    }

    private fun handleWithJson(ticketJson: String) {
        try {
            this.ticket = Gson().fromJson<Ticket>(ticketJson, Ticket::class.java)
        } catch (e: JsonSyntaxException) {
            flowState.postValue(FlowState.failure(e))
        } catch (e: IllegalStateException) {
            flowState.postValue(FlowState.failure(e))
        }

    }

    fun getFlowState(): LiveData<FlowState<Boolean>> {
        return flowState
    }
}
