package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tcc.sicv.data.firebase.TicketRepository;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Ticket;
import com.tcc.sicv.data.preferences.PreferencesHelper;

import static com.tcc.sicv.data.model.Status.ERROR;
import static com.tcc.sicv.data.model.Status.LOADING;
import static com.tcc.sicv.data.model.Status.SUCCESS;

public class TicketDetailsViewModel extends ViewModel {
    private MutableLiveData<FlowState<Boolean>> flowState;
    private Ticket ticket;

    public TicketDetailsViewModel(
            String ticketJson, PreferencesHelper preferencesHelper,
            Boolean loadedTicket
    ) {
        handleWithJson(ticketJson);
        flowState = new MutableLiveData<>();
        if(loadedTicket){
            flowState.postValue(new FlowState<>(true, null, SUCCESS));
        }
        flowState.setValue(new FlowState<Boolean>(null, null, LOADING));
        TicketRepository repository = new TicketRepository();
        repository.setTicket(preferencesHelper.getEmail(), ticket, flowState);
    }

    private void handleWithJson(String ticketJson) {
        try {
            this.ticket = new Gson().fromJson(ticketJson, Ticket.class);
        } catch (JsonSyntaxException e) {
            flowState.postValue(new FlowState<Boolean>(null, e, ERROR));
        } catch (IllegalStateException e){
            flowState.postValue(new FlowState<Boolean>(null, e, ERROR));
        }
    }

    public LiveData<FlowState<Boolean>> getFlowState() { return flowState; }

    public Ticket getTicket() { return ticket; }
}
