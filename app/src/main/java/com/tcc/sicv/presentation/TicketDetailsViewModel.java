package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tcc.sicv.data.firebase.TicketRepository;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Ticket;
import com.tcc.sicv.data.preferences.PreferencesHelper;

import static com.tcc.sicv.data.model.Status.LOADING;

public class TicketDetailsViewModel extends ViewModel {
    private MutableLiveData<FlowState<Boolean>> flowState;
    private Ticket ticket;

    public TicketDetailsViewModel(Ticket ticket, PreferencesHelper preferencesHelper) {
        this.ticket = ticket;
        flowState = new MutableLiveData<>();
        flowState.setValue(new FlowState<Boolean>(null, null, LOADING));
        TicketRepository repository = new TicketRepository();
        repository.setTicket(preferencesHelper.getEmail(), ticket, flowState);
    }

    public LiveData<FlowState<Boolean>> getFlowState() { return flowState; }

    public Ticket getTicket() { return ticket; }
}
