package com.tcc.sicv.data.firebase;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.User;
import com.tcc.sicv.data.model.Vehicle;

import java.util.ArrayList;

import static com.tcc.sicv.data.model.Status.ERROR;
import static com.tcc.sicv.data.model.Status.SUCCESS;
import static com.tcc.sicv.utils.Constants.IMAGE_FIELD;
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

    public void getMyVehicles(String email, final MutableLiveData<FlowState<ArrayList<Vehicle>>> flowState) {
        db.collection(USER_COLLECTION_PATH).document(email).collection(VEHICLES_COLLECTION_PATH)
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
                                            item.getId()
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
                })
        ;
    }

    public void getAllVehicles(final MutableLiveData<FlowState<ArrayList<Vehicle>>> flowState) {
        db.collection(VEHICLES_COLLECTION_PATH)
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
                                            ""
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
                })
        ;
    }

    public void buyVehicle(final String email, String model,
                           final MutableLiveData<FlowState<Boolean>> result) {

        db.collection(VEHICLES_COLLECTION_PATH).document(model).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String imagem = (String) documentSnapshot.get("imagem");
                        String modelo = (String) documentSnapshot.get("modelo");
                        String potencia = (String) documentSnapshot.get("potencia");
                        String preco = (String) documentSnapshot.get("preco");
                        String velocidade = (String) documentSnapshot.get("velocidade");
                        String marca = (String) documentSnapshot.get("marca");
                        String tipo = (String) documentSnapshot.get("tipo");

                        putVehicleInUserVehicles(imagem, modelo, potencia, preco, velocidade,
                                marca, tipo, email, result);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                            result.postValue(
                                    new FlowState<Boolean>(null, e, ERROR)
                            );
                    }
                });

    }

    private void putVehicleInUserVehicles(String imagem, String modelo,
                                          String potencia, String preco,
                                          String velocidade, String marca,
                                          String tipo, String email,
                                          final MutableLiveData<FlowState<Boolean>> result) {
        db.collection(USER_COLLECTION_PATH).document(email).collection(VEHICLES_COLLECTION_PATH)
                .add(
                        new Vehicle(
                                imagem,
                                modelo,
                                potencia,
                                preco,
                                velocidade,
                                marca,
                                tipo,
                                null
                        )
                )
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        result.postValue(
                                new FlowState<Boolean>(null, null, SUCCESS)
                        );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                            result.postValue(
                                    new FlowState<Boolean>(null, e, ERROR)
                            );
                    }
                });
    }
}

