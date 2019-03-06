package com.tcc.sicv.data.firebase;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Logs;
import com.tcc.sicv.data.model.MaintenanceVehicle;

import java.util.ArrayList;

import static com.tcc.sicv.data.model.Status.ERROR;
import static com.tcc.sicv.data.model.Status.SUCCESS;
import static com.tcc.sicv.utils.Constants.CODE;
import static com.tcc.sicv.utils.Constants.CODE_VEHICLE_FIELD;
import static com.tcc.sicv.utils.Constants.COST_FIELD;
import static com.tcc.sicv.utils.Constants.DATE_FIELD;
import static com.tcc.sicv.utils.Constants.DESCRIPTION_FIELD;
import static com.tcc.sicv.utils.Constants.IMAGE_FIELD;
import static com.tcc.sicv.utils.Constants.LOGS_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.MAINTENANCE_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.MODEL_FIELD;
import static com.tcc.sicv.utils.Constants.TOTAL_COST_FIELD;
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
                                    CODE + item.getId(),
                                    (String) item.get(MODEL_FIELD),
                                    (String) item.get(IMAGE_FIELD),
                                    (String) item.get(CODE_VEHICLE_FIELD),
                                    (String) item.get(TOTAL_COST_FIELD)
                            );
                            maintenanceList.add(maintenance);
                        }
                        result.postValue(new FlowState<>(maintenanceList, null, SUCCESS));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.postValue(
                                new FlowState<ArrayList<MaintenanceVehicle>>(null, e, ERROR)
                        );
                    }
                });
    }

    public void getLogsInMaintenance(
            String email,
            String maintenanceCode,
            final MutableLiveData<FlowState<ArrayList<Logs>>> result
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
                        result.postValue(new FlowState<>(logsList, null, SUCCESS));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.postValue(new FlowState<ArrayList<Logs>>(
                                null, e, ERROR
                        ));
                    }
                });
    }
}
