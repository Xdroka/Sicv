package com.tcc.sicv.data.firebase;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Ticket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.tcc.sicv.data.model.Status.ERROR;
import static com.tcc.sicv.data.model.Status.SUCCESS;
import static com.tcc.sicv.utils.Constants.TICKET_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH;

public class TicketRepository {
    private final FirebaseFirestore db;

    public TicketRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void setTicket(final String email, Ticket ticket, final MutableLiveData<FlowState<Boolean>> result) {
        ticket.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                Locale.getDefault()).format(new Date()));

        db.collection(USER_COLLECTION_PATH).document(email).collection(TICKET_COLLECTION_PATH)
                .document(ticket.getTicketId())
                .set(ticket)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result.postValue(new FlowState<>(true, null, SUCCESS));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.postValue(new FlowState<Boolean>(null, e, ERROR));
                    }
                });
    }
}
