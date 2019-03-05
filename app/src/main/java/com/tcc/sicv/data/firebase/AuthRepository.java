package com.tcc.sicv.data.firebase;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.BoringLayout;

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

public class AuthRepository {
    private final FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public AuthRepository() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void logout(final MutableLiveData<FlowState<Void>> result){
        mAuth.signOut();
        result.postValue(new FlowState<Void>(null,null,SUCCESS));
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

    public void getUserProfile(final String email, final MutableLiveData<FlowState<User>> result) {
        db.collection(USER_COLLECTION_PATH).document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String cpf = (String) documentSnapshot.get("cpf");
                        String date = (String) documentSnapshot.get("date");
                        String name = (String) documentSnapshot.get("name");
                        String tel = (String) documentSnapshot.get("tel");
                        result.postValue(
                                new FlowState<>(
                                        new User(email,"",name,cpf,tel,date),
                                        null,
                                        SUCCESS
                                )
                        );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseNetworkException) {
                            result.postValue(
                                    new FlowState<User>(
                                            null,
                                            new Exceptions.NoInternetException(),
                                            ERROR)
                            );
                        } else {
                            result.postValue(
                                    new FlowState<User>(
                                            null,
                                            new Exceptions.ServerException(),
                                            ERROR
                                    )
                            );
                        }
                    }
                });
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
                        if (e instanceof FirebaseNetworkException) {
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
}
