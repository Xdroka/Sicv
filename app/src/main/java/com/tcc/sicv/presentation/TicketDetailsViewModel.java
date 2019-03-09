package com.tcc.sicv.presentation;

import android.arch.lifecycle.ViewModel;

import com.tcc.sicv.data.firebase.TicketRepository;
import com.tcc.sicv.data.model.Ticket;

public class TicketDetailsViewModel extends ViewModel {
    private TicketRepository repository;

    public TicketDetailsViewModel(Ticket ticket) {

    }
}
