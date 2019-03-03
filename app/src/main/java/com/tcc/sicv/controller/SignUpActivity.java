package com.tcc.sicv.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.sicv.R;
import com.tcc.sicv.data.model.State;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private String TAG = "aqui";
    private AppCompatEditText emailEt;
    private AppCompatEditText passwordEt;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setup();

        creatingObservers();
        //configFirebase();
    }

    private void creatingObservers() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setup() {
        passwordEt = findViewById(R.id.passwordEt);
        emailEt = findViewById(R.id.emailEt);
        button = findViewById(R.id.signUpButton);
    }

    private void configFirebase() {
        DocumentReference db = FirebaseFirestore.getInstance().document("usuarios/pedro");

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("email", "pedro");
        user.put("senha", "pedro01");

// Add a new document with a generated ID
        db.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Aqui", "Sucesso");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Aqui", "Deu ruim");
            }
        });
    }

    private void handleWithPasswordState(State state) {
        switch (state) {
            case EMPTY:
                passwordEt.setError(getString(R.string.error_empty_password));
                break;
            case VALID:
                passwordEt.setError(null);
                break;
            case INVALID:
                passwordEt.setError(getString(R.string.error_invalid_password));
                break;
        }
    }

    private void handleWithEmailState(State state) {
        switch (state) {
            case EMPTY:
                emailEt.setError(getString(R.string.error_empty_email));
                break;
            case VALID:
                emailEt.setError(null);
                break;
            case INVALID:
                emailEt.setError(getString(R.string.error_invalid_email));
                break;
        }

    }
}