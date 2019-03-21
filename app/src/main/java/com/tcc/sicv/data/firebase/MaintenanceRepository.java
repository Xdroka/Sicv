package com.tcc.sicv.data.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tcc.sicv.base.Result;
import com.tcc.sicv.data.model.Logs;
import com.tcc.sicv.data.model.MaintenanceVehicle;
import com.tcc.sicv.data.model.Vehicle;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import static com.tcc.sicv.utils.Constants.CODE_VEHICLE_FIELD;
import static com.tcc.sicv.utils.Constants.COST_FIELD;
import static com.tcc.sicv.utils.Constants.DATE_FIELD;
import static com.tcc.sicv.utils.Constants.DATE_FORMAT;
import static com.tcc.sicv.utils.Constants.DESCRIPTION_FIELD;
import static com.tcc.sicv.utils.Constants.IMAGE_FIELD;
import static com.tcc.sicv.utils.Constants.LOGS_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.MAINTENANCE_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.MAINTENANCE_FIELD;
import static com.tcc.sicv.utils.Constants.MODEL_FIELD;
import static com.tcc.sicv.utils.Constants.RELEASE_VEHICLE_FIELD;
import static com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.VEHICLES_COLLECTION_PATH;

public class MaintenanceRepository {
    private final FirebaseFirestore db;

    public MaintenanceRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void setVehicleInMaintenance(
            final String email,
            Vehicle vehicle,
            final String date,
            final Result<MaintenanceVehicle> result
    ) {
        DocumentReference document = db.collection(USER_COLLECTION_PATH)
                .document(email)
                .collection(MAINTENANCE_COLLECTION_PATH)
                .document();

        final MaintenanceVehicle maintenanceVehicle = new MaintenanceVehicle(
                document.getId(), vehicle.getModelo(), vehicle.getImagem(),
                vehicle.getCodigo(), false
        );
        document
                .set(maintenanceVehicle)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onFailure(e);
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                        public void onSuccess(Void aVoid) {
                        setFirstLogInMaintenance(email, maintenanceVehicle, date, result);
                    }
                });
    }

    private void setFirstLogInMaintenance(
            final String email, final MaintenanceVehicle maintenanceVehicle,
            String date,
            final Result<MaintenanceVehicle> result
    ){
        String firstLogDescription = "Entrega do veículo a concessionária";
        db.collection(USER_COLLECTION_PATH).document(email).collection(MAINTENANCE_COLLECTION_PATH)
                .document(maintenanceVehicle.getMaintenanceCode())
                .collection(LOGS_COLLECTION_PATH)
                .add(new Logs(date, firstLogDescription, "R$ 0,00"))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        setMyVehicleInMaintenance(email, maintenanceVehicle, result);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onFailure(e);
                    }
                });
    }

    private void setMyVehicleInMaintenance(
            String email, final MaintenanceVehicle maintenance,
            final Result<MaintenanceVehicle> result
    ){
        db.collection(USER_COLLECTION_PATH).document(email).collection(VEHICLES_COLLECTION_PATH)
                .document(maintenance.getCod_veiculo())
                .update(MAINTENANCE_FIELD, true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result.onSuccess(maintenance);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onFailure(e);
                    }
                });
    }

    public void getVehiclesInMaintenance(
            final String email,
            final Result<ArrayList<MaintenanceVehicle>> result
    ) {
        db.collection(USER_COLLECTION_PATH).document(email).collection(MAINTENANCE_COLLECTION_PATH)
                .orderBy(MODEL_FIELD)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<MaintenanceVehicle> maintenanceList = new ArrayList<>();
                        for (DocumentSnapshot item : queryDocumentSnapshots.getDocuments()) {
                            MaintenanceVehicle maintenance = new MaintenanceVehicle(
                                    item.getId(),
                                    (String) item.get(MODEL_FIELD),
                                    (String) item.get(IMAGE_FIELD),
                                    (String) item.get(CODE_VEHICLE_FIELD),
                                    item.getBoolean(RELEASE_VEHICLE_FIELD)
                            );
                            maintenanceList.add(maintenance);
                        }
                        result.onSuccess(maintenanceList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onFailure(e);
                    }
                });
    }

    public void getLogsInMaintenance(
            String email,
            String maintenanceCode,
            final Result<ArrayList<Logs>> result
    ) {
        db.collection(USER_COLLECTION_PATH).document(email)
                .collection(MAINTENANCE_COLLECTION_PATH)
                .document(maintenanceCode)
                .collection(LOGS_COLLECTION_PATH)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Logs> logsList = new ArrayList<>();
                        for (DocumentSnapshot item : queryDocumentSnapshots.getDocuments()) {
                            logsList.add(new Logs(
                                    (String) item.get(DATE_FIELD),
                                    (String) item.get(DESCRIPTION_FIELD),
                                    (String) item.get(COST_FIELD)
                            ));
                        }
                        orderListByDate(logsList);
                        result.onSuccess(logsList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onFailure(e);
                    }
                });
    }

    private void orderListByDate(ArrayList<Logs> logsList) {
        Collections.sort(logsList, new Comparator<Logs>() {
            DateFormat f = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            @Override
            public int compare(Logs o1, Logs o2) {
                try {
                    return f.parse(o1.getData()).compareTo(f.parse(o2.getData()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }
}
