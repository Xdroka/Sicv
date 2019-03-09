package com.tcc.sicv.data.firebase;

import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Ticket;

import java.text.SimpleDateFormat;

import static com.tcc.sicv.utils.Constants.TICKET_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH;

public class TicketRepository {
    private final FirebaseFirestore db;

    public TicketRepository(){
        db = FirebaseFirestore.getInstance();
    }

    public void setTicket(String email, Ticket ticket, MutableLiveData<FlowState<Boolean>> result){
    }
}
