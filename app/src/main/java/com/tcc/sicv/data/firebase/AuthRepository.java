package com.tcc.sicv.data.firebase;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.User;

import static com.tcc.sicv.data.model.Status.ERROR;
import static com.tcc.sicv.data.model.Status.SUCCESS;
import static com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH;

public class AuthRepository {
    private final FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public AuthRepository() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void logout(final MutableLiveData<FlowState<Void>> result) {
        mAuth.signOut();
        result.postValue(new FlowState<Void>(null, null, SUCCESS));
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
                        } else {
                            result.postValue(
                                    new FlowState<String>(null, task.getException(), ERROR)
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
                        result.postValue(
                                new FlowState<User>(null, e, ERROR)
                        );
                    }
                });
    }

    private void createUserDocument(final User user, final MutableLiveData<FlowState<String>> result) {
        db.collection(USER_COLLECTION_PATH).document(user.getEmail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result.postValue(new FlowState<>(user.getEmail(), null, SUCCESS));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.postValue(
                                new FlowState<String>(null, e, ERROR)
                        );
                    }
                });
    }

    public void checkAccountExistAndCreateUser(
            final User user,
            final MutableLiveData<FlowState<String>> result
    ) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createUserDocument(user, result);
                        } else {
                            result.postValue(
                                    new FlowState<String>(null, task.getException(), ERROR)
                            );
                        }
                    }
                });
    }
}
