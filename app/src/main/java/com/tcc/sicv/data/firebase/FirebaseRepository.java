package com.tcc.sicv.data.firebase;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tcc.sicv.data.Exceptions;
import com.tcc.sicv.data.model.User;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.Vehicle;

import java.util.ArrayList;

import static com.tcc.sicv.presentation.model.Status.ERROR;
import static com.tcc.sicv.presentation.model.Status.SUCCESS;
import static com.tcc.sicv.utils.Constants.IMAGE_FIELD;
import static com.tcc.sicv.utils.Constants.MARK_FIELD;
import static com.tcc.sicv.utils.Constants.MODEL_FIELD;
import static com.tcc.sicv.utils.Constants.POWER_FIELD;
import static com.tcc.sicv.utils.Constants.PRICE_FIELD;
import static com.tcc.sicv.utils.Constants.SPEED_FIELD;
import static com.tcc.sicv.utils.Constants.TYPE_FIELD;
import static com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH;
import static com.tcc.sicv.utils.Constants.VEHICLES_COLLECTION_PATH;

public class FirebaseRepository {
    private final FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public FirebaseRepository() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void signIn(final String email, String password, final MutableLiveData<FlowState<String>> result) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            result.postValue(
                                    new FlowState<>(email, null, SUCCESS)
                            );
                        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            result.postValue(
                                    new FlowState<String>(null, new Exceptions.InvalidPasswordLogin(), ERROR)
                            );
                        } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            result.postValue(
                                    new FlowState<String>(null, new Exceptions.InvalidEmailLogin(), ERROR)
                            );
                        } else {
                            result.postValue(
                                    new FlowState<String>(null, new Exception(), ERROR)
                            );
                        }

                    }
                }
        );
    }

    private void createUserDocument(final User user, final MutableLiveData<FlowState<Boolean>> result) {
        db.collection(USER_COLLECTION_PATH).document(user.getEmail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result.postValue(new FlowState<>(true, null, SUCCESS));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.postValue(
                                new FlowState<Boolean>(
                                        null,
                                        new Exceptions.ServerException(),
                                        ERROR
                                )
                        );
                    }
                });
    }

    public void checkAccountExistAndCreateUser(
            final User user,
            final MutableLiveData<FlowState<Boolean>> result
    ) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createUserDocument(user, result);
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthUserCollisionException) {
                                result.postValue(
                                        new FlowState<Boolean>(
                                                null,
                                                new Exceptions.InvalidUserEmailData(),
                                                ERROR
                                        )
                                );
                            } else if (exception instanceof FirebaseNetworkException) {
                                result.postValue(
                                        new FlowState<Boolean>(
                                                null,
                                                new Exceptions.NoInternetException(),
                                                ERROR)
                                );
                            } else {
                                result.postValue(
                                        new FlowState<Boolean>(
                                                null,
                                                new Exceptions.ServerException(),
                                                ERROR
                                        )
                                );
                            }
                        }
                    }
                });
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
}
