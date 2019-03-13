package com.tcc.sicv.data.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.sicv.data.model.User;
import com.tcc.sicv.base.Result;

import static com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH;

public class AuthRepository {
    private final FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public AuthRepository() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void logout(final Result<Void> result) {
        mAuth.signOut();
        result.onSuccess(null);
    }

    public void signIn(final String email, String password, final Result<String> result) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            result.onSuccess(email);
                        } else {
                            result.onFailure(task.getException());
                        }

                    }
                }
        );
    }

    public void getUserProfile(final String email, final Result<User> result) {
        db.collection(USER_COLLECTION_PATH).document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String cpf = (String) documentSnapshot.get("cpf");
                        String date = (String) documentSnapshot.get("date");
                        String name = (String) documentSnapshot.get("name");
                        String tel = (String) documentSnapshot.get("tel");
                        result.onSuccess(new User(email, "", name, cpf, tel, date));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onFailure(e);
                    }
                });
    }

    private void createUserDocument(final User user, final Result<String> result) {
        db.collection(USER_COLLECTION_PATH).document(user.getEmail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result.onSuccess(user.getEmail());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onFailure(e);
                    }
                });
    }

    public void checkAccountExistAndCreateUser(
            final User user,
            final Result<String> result
    ) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createUserDocument(user, result);
                        } else {
                            result.onFailure(task.getException());
                        }
                    }
                });
    }
}
