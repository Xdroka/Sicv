package com.tcc.sicv.data.remote.firebase;

import android.app.Application;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import static android.widget.Toast.LENGTH_SHORT;

public class Auth{
    private Application app;
    private FirebaseAuth mAuth;

    public Auth(Application app){
        this.app = app;
        mAuth = FirebaseAuth.getInstance();
    }

    public Boolean check(){
        return true;
    }

    public void sign(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(app,
                                     user.getPhoneNumber() + " Authentication successful.", LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(app, "Authentication failed." +
                                    task.getException().getMessage().toString(), LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}
