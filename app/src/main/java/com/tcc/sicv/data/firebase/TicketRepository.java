package com.tcc.sicv.data.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tcc.sicv.base.Result;
import com.tcc.sicv.data.model.Ticket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.tcc.sicv.utils.Constants.CODE_VEHICLE_FIELD;
import static com.tcc.sicv.utils.Constants.CODE_VEHICLE_TICKET_FIELD;
import static com.tcc.sicv.utils.Constants.COST_TICKET_FIELD;
import static com.tcc.sicv.utils.Constants.DATE_BUY_TICKET_FIELD;
import static com.tcc.sicv.utils.Constants.MAINTENANCE_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.MAINTENANCE_FIELD;
import static com.tcc.sicv.utils.Constants.TICKET_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.TIME_TICKET_FIELD;
import static com.tcc.sicv.utils.Constants.TYPE_FIELD;
import static com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.VEHICLES_COLLECTION_PATH;

public class TicketRepository {
    private final FirebaseFirestore db;

    public TicketRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void setTicket(final String email, final Ticket ticket, final Result<Boolean> result) {
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
                        } else result.onSuccess(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onFailure(e);
                    }
                });
    }

    private void removingVehicleFromMaintenance(
            final String email, final Ticket ticket, final Result<Boolean> result
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
                                       updateFieldInVehicle(email, ticket.getCodigoVeiculo(), result);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            result.onFailure(e);
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onFailure(e);
                    }
                });
    }

    private void updateFieldInVehicle(
            String email, String codeVehicle, final Result<Boolean> result
    ){
        db.collection(USER_COLLECTION_PATH).document(email)
                .collection(VEHICLES_COLLECTION_PATH)
                .document(codeVehicle)
                .update(MAINTENANCE_FIELD, false)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onFailure(e);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result.onSuccess(true);
                    }
                });
    }

    public void getTickets(String email, final Result<ArrayList<Ticket>> result) {
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
                                    item.getId(),
                                    (String) item.get(DATE_BUY_TICKET_FIELD)
                            ));
                        }
                        result.onSuccess(ticketList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onFailure(e);
                    }
                });
    }
}
