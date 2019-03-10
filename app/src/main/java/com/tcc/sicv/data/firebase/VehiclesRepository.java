package com.tcc.sicv.data.firebase;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Vehicle;

import java.util.ArrayList;

import static com.tcc.sicv.data.model.Status.ERROR;
import static com.tcc.sicv.data.model.Status.SUCCESS;
import static com.tcc.sicv.utils.Constants.IMAGE_FIELD;
import static com.tcc.sicv.utils.Constants.MAINTENANCE_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.MAINTENANCE_FIELD;
import static com.tcc.sicv.utils.Constants.MARK_FIELD;
import static com.tcc.sicv.utils.Constants.MODEL_FIELD;
import static com.tcc.sicv.utils.Constants.POWER_FIELD;
import static com.tcc.sicv.utils.Constants.PRICE_FIELD;
import static com.tcc.sicv.utils.Constants.SPEED_FIELD;
import static com.tcc.sicv.utils.Constants.TYPE_FIELD;
import static com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.VEHICLES_COLLECTION_PATH;

public class VehiclesRepository {

    private final FirebaseFirestore db;

    public VehiclesRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void getMyVehicles(
            String email,
            final MutableLiveData<FlowState<ArrayList<Vehicle>>> flowState
    ) {
        db.collection(USER_COLLECTION_PATH).document(email).collection(VEHICLES_COLLECTION_PATH)
                .orderBy(MODEL_FIELD)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Vehicle> list = new ArrayList<>();
                        for (DocumentSnapshot item : queryDocumentSnapshots.getDocuments()) {
                            list.add(new Vehicle(
                                            (String) item.get(IMAGE_FIELD),
                                            (String) item.get(MODEL_FIELD),
                                            (String) item.get(POWER_FIELD),
                                            (String) item.get(PRICE_FIELD),
                                            (String) item.get(SPEED_FIELD),
                                            (String) item.get(MARK_FIELD),
                                            (String) item.get(TYPE_FIELD),
                                            item.getId(),
                                            item.getBoolean(MAINTENANCE_FIELD)
                                    )
                            );
                        }
                        flowState.postValue(new FlowState<>(list, null, SUCCESS));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        flowState.postValue(new FlowState<ArrayList<Vehicle>>(null, e, ERROR));
                    }
                });
    }

    public void getAllVehicles(final MutableLiveData<FlowState<ArrayList<Vehicle>>> flowState) {
        db.collection(VEHICLES_COLLECTION_PATH)
                .orderBy(MODEL_FIELD)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Vehicle> list = new ArrayList<>();
                        for (DocumentSnapshot item : queryDocumentSnapshots.getDocuments()) {
                            list.size();
                            list.add(new Vehicle(
                                            (String) item.get(IMAGE_FIELD),
                                            (String) item.get(MODEL_FIELD),
                                            (String) item.get(POWER_FIELD),
                                            (String) item.get(PRICE_FIELD),
                                            (String) item.get(SPEED_FIELD),
                                            (String) item.get(MARK_FIELD),
                                            (String) item.get(TYPE_FIELD),
                                            "",
                                            item.getBoolean(MAINTENANCE_FIELD)
                                    )
                            );
                        }
                        flowState.postValue(new FlowState<>(list, null, SUCCESS));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        flowState.postValue(new FlowState<ArrayList<Vehicle>>(null, e, ERROR));
                    }
                });
    }

    public void buyVehicle(
            final String email, String model,
            final MutableLiveData<FlowState<Vehicle>> result
    ) {
        db.collection(VEHICLES_COLLECTION_PATH).document(model).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String imagem = (String) documentSnapshot.get(IMAGE_FIELD);
                        String modelo = (String) documentSnapshot.get(MODEL_FIELD);
                        String potencia = (String) documentSnapshot.get(POWER_FIELD);
                        String preco = (String) documentSnapshot.get(PRICE_FIELD);
                        String velocidade = (String) documentSnapshot.get(SPEED_FIELD);
                        String marca = (String) documentSnapshot.get(MARK_FIELD);
                        String tipo = (String) documentSnapshot.get(TYPE_FIELD);

                        putVehicleInUserVehicles(imagem, modelo, potencia, preco, velocidade,
                                marca, tipo, email, result);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.postValue(new FlowState<Vehicle>(null, e, ERROR));
                    }
                });
    }

    private void putVehicleInUserVehicles(
            String imagem, String modelo,
            String potencia, String preco,
            String velocidade, String marca,
            String tipo, String email,
            final MutableLiveData<FlowState<Vehicle>> result
    ) {
        final Vehicle vehicle = new Vehicle(
                imagem, modelo, potencia, preco, velocidade, marca, tipo, null, false
        );
        db.collection(USER_COLLECTION_PATH).document(email).collection(VEHICLES_COLLECTION_PATH)
                .add(vehicle)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        vehicle.setCodigo(documentReference.getId());
                        result.postValue(new FlowState<>(vehicle, null, SUCCESS));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.postValue(new FlowState<Vehicle>(null, e, ERROR));
                    }
                });
    }
}

