package com.tcc.sicv.data.firebase;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.Logs;
import com.tcc.sicv.presentation.model.MaintenanceVehicle;

import java.util.ArrayList;

import static com.tcc.sicv.utils.Constants.CODE_VEHICLE_FIELD;
import static com.tcc.sicv.utils.Constants.COST_FIELD;
import static com.tcc.sicv.utils.Constants.DATE_FIELD;
import static com.tcc.sicv.utils.Constants.DESCRIPTION_FIELD;
import static com.tcc.sicv.utils.Constants.IMAGE_FIELD;
import static com.tcc.sicv.utils.Constants.LOGS_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.MAINTENANCE_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.MODEL_FIELD;
import static com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH;

public class MaintenanceRepository {
    private final FirebaseFirestore db;

    public MaintenanceRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getVehiclesInMaintenance(
            final String email,
            final MutableLiveData<FlowState<ArrayList<MaintenanceVehicle>>> result
    ) {
        db.collection(USER_COLLECTION_PATH).document(email).collection(MAINTENANCE_COLLECTION_PATH)
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
                                    new ArrayList<Logs>()
                            );
                            getLogsInMaintenance(email, maintenance, maintenanceList, result);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (result.getValue() != null) result.postValue(result.getValue().error(e));
                    }
                });
    }

    private void getLogsInMaintenance(
            String email,
            final MaintenanceVehicle maintenance,
            final ArrayList<MaintenanceVehicle> maintenanceList,
            final MutableLiveData<FlowState<ArrayList<MaintenanceVehicle>>> result
    ) {
        db.collection(USER_COLLECTION_PATH).document(email)
                .collection(MAINTENANCE_COLLECTION_PATH)
                .document(maintenance.getMaintenanceCode())
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
                        maintenance.setLogs(logsList);
                        maintenanceList.add(maintenance);
                        FlowState<ArrayList<MaintenanceVehicle>> value = result.getValue();
                        if (result.getValue() != null) result.postValue(result.getValue()
                                .success(maintenanceList)
                        );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (result.getValue() != null) result.postValue(result.getValue().error(e));
                    }
                });
    }
}
