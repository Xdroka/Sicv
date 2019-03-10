package com.tcc.sicv.data.firebase;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Ticket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.tcc.sicv.data.model.Status.ERROR;
import static com.tcc.sicv.data.model.Status.SUCCESS;
import static com.tcc.sicv.utils.Constants.CODE_VEHICLE_FIELD;
import static com.tcc.sicv.utils.Constants.CODE_VEHICLE_TICKET_FIELD;
import static com.tcc.sicv.utils.Constants.COST_TICKET_FIELD;
import static com.tcc.sicv.utils.Constants.MAINTENANCE_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.MAINTENANCE_FIELD;
import static com.tcc.sicv.utils.Constants.TICKET_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.TIME_TICKET_FIELD;
import static com.tcc.sicv.utils.Constants.TYPE_FIELD;
import static com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH;

public class TicketRepository {
    private final FirebaseFirestore db;

    public TicketRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void setTicket(final String email, final Ticket ticket, final MutableLiveData<FlowState<Boolean>> result) {
        ticket.setTime(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                Locale.getDefault()).format(new Date()));

        db.collection(USER_COLLECTION_PATH).document(email).collection(TICKET_COLLECTION_PATH)
                .document(ticket.getTicketId())
                .set(ticket)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (ticket.getTipo().equals(MAINTENANCE_FIELD)) {
                            removingVehicleFromMaintenance(
                                    email, ticket, result
                            );
                        } else result.postValue(new FlowState<>(true, null, SUCCESS));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.postValue(new FlowState<Boolean>(null, e, ERROR));
                    }
                });
    }

    private void removingVehicleFromMaintenance(
            final String email, final Ticket ticket, final MutableLiveData<FlowState<Boolean>> result
    ) {
        db.collection(USER_COLLECTION_PATH).document(email).collection(MAINTENANCE_COLLECTION_PATH)
                .whereEqualTo(CODE_VEHICLE_FIELD, ticket.getCodigoVeiculo())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot item : queryDocumentSnapshots.getDocuments()){
                            String maintenanceCode = item.getId();
                            db.collection(USER_COLLECTION_PATH)
                                    .document(email)
                                    .collection(MAINTENANCE_COLLECTION_PATH)
                                    .document(maintenanceCode)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            result.postValue(new FlowState<>(true, null, SUCCESS));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            result.postValue(new FlowState<Boolean>(
                                                    null, e , ERROR
                                            ));
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.postValue(new FlowState<Boolean>(null, e, ERROR));
                    }
                });
    }

    public void getTickets(String email, final MutableLiveData<FlowState<ArrayList<Ticket>>> result) {
        db.collection(USER_COLLECTION_PATH).document(email)
                .collection(TICKET_COLLECTION_PATH)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Ticket> ticketList = new ArrayList<>();
                        for(DocumentSnapshot item: queryDocumentSnapshots.getDocuments()){
                            ticketList.add(new Ticket(
                                    (String) item.get(COST_TICKET_FIELD),
                                    (String) item.get(TYPE_FIELD),
                                    (String) item.get(CODE_VEHICLE_TICKET_FIELD),
                                    (String) item.get(TIME_TICKET_FIELD),
                                    item.getId()
                            ));
                        }
                        result.postValue(new FlowState<>(ticketList, null, SUCCESS));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.postValue(
                                new FlowState<ArrayList<Ticket>>(null, null, ERROR)
                        );
                    }
                });
    }
}
