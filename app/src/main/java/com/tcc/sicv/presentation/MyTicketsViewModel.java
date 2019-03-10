package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tcc.sicv.data.firebase.TicketRepository;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Ticket;
import com.tcc.sicv.data.preferences.PreferencesHelper;

import java.util.ArrayList;

import static com.tcc.sicv.data.model.Status.LOADING;

public class MyTicketsViewModel extends ViewModel {
    private PreferencesHelper preferencesHelper;
    private MutableLiveData<FlowState<ArrayList<Ticket>>> flowState;
    private TicketRepository repository;

    public MyTicketsViewModel(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
        flowState = new MutableLiveData<>();
        flowState.setValue(new FlowState<ArrayList<Ticket>>());
        repository = new TicketRepository();
        requestMyTickets();
    }

    public void requestMyTickets() {
        if(flowState.getValue() == null || flowState.getValue().isLoading()) return;
        flowState.postValue(new FlowState<ArrayList<Ticket>>(null, null, LOADING));
        repository.getTickets(preferencesHelper.getEmail(), flowState);
    }

    public LiveData<FlowState<ArrayList<Ticket>>> getFlowState() { return flowState; }
}
