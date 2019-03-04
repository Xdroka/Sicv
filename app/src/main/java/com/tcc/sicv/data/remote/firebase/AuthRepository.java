package com.tcc.sicv.data.remote.firebase;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.sicv.data.model.User;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.Status;
import com.tcc.sicv.ui.Exceptions;

import java.util.Objects;

import static com.tcc.sicv.presentation.model.Status.ERROR;

public class AuthRepository {
    private final FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public AuthRepository() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public Boolean check() {
        return true;
    }

//    public void signIn(final User user, final MutableLiveData<FlowState<User>> result) {
//        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(
//                new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser currentUser = mAuth.getCurrentUser();
//                            String phoneNumber = currentUser != null ? currentUser.getPhoneNumber() : "";
//                            Toast.makeText(app,
//                                    phoneNumber + " Authentication successful.", LENGTH_SHORT).show();
//                            result.postValue(new FlowState<>(user, null, SUCCESS));
//                        } else {
//                            Exception exception = task.getException();
//                            Toast.makeText(app, "Authentication failed." +
//                                    exception, LENGTH_SHORT).show();
//
//                            if (exception != null) {
//                                result.postValue(new FlowState<User>(null, exception, ERROR));
//                            } else {
//                                result.postValue(new FlowState<User>(null, new Exception(), ERROR));
//                            }
//                        }
//                    }
//                }
//        );
//    }

    public void checkAccountExistAndCreateUser(final User user, final MutableLiveData<FlowState<Boolean>> result) {
        DocumentReference docRef = db.collection("usuarios").document(user.getCpf());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (Objects.requireNonNull(document).exists()) {
                        result.postValue(
                                new FlowState<Boolean>(null,
                                        new Exceptions.InvalidUserCPFData(),
                                        ERROR
                                )
                        );
                    } else {
                        createUserAuth(user, result);
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof FirebaseNetworkException) {
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

    private void createUserDocument(final User user, final MutableLiveData<FlowState<Boolean>> result) {
        db.collection("usuarios").document(user.getCpf())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result.postValue(new FlowState<>(true, null, Status.SUCCESS));
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

    private void createUserAuth(
            final User user,
            final MutableLiveData<FlowState<Boolean>> result
    ) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createUserDocument(user,result);
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
