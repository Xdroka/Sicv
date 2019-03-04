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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.sicv.data.model.User;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.UserLogin;
import com.tcc.sicv.ui.Exceptions;
import static com.tcc.sicv.presentation.model.Status.ERROR;
import static com.tcc.sicv.presentation.model.Status.SUCCESS;

public class AuthRepository {
    private static final String USER_COLLECTION_PATH = "usuarios";
    private final FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public AuthRepository() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public Boolean check() {
        return true;
    }

    public void signIn(final UserLogin user, final MutableLiveData<FlowState<User>> result) {
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String phoneNumber = currentUser != null ? currentUser.getPhoneNumber()
                                    : "";
                            postUserFromDb(user.getEmail(), result);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                result.postValue(
                                        new FlowState<User>(null, new Exceptions.InvalidLogin(), ERROR)
                                );
                            } else {
                                result.postValue(
                                        new FlowState<User>(null, new Exception(), ERROR)
                                );
                            }
                        }
                    }
                }
        );
    }

    private void postUserFromDb(final String email, final MutableLiveData<FlowState<User>> userMutableLiveData) {
        db.collection(USER_COLLECTION_PATH).document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String cpf = (String) documentSnapshot.get("cpf");
                        String date = (String) documentSnapshot.get("date");
                        String name = (String) documentSnapshot.get("name");
                        String tel = (String) documentSnapshot.get("tel");

                        userMutableLiveData.postValue(
                                new FlowState<>(
                                        new User(email, "", name, cpf, tel, date),
                                        null,
                                        SUCCESS
                                )
                        );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        userMutableLiveData.postValue(
                                new FlowState<User>(null, new Exception(), ERROR)
                        );
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
}
