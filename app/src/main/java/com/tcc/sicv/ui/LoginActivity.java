package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tcc.sicv.R;
import com.tcc.sicv.presentation.LoginViewModel;
import com.tcc.sicv.presentation.model.State;
import com.tcc.sicv.presentation.model.User;
import com.tcc.sicv.presentation.model.ViewState;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel mViewModel;
    private Button button;
    private TextInputEditText emailEt;
    private TextInputEditText passwordEt;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mViewModel = new LoginViewModel();
        setup();
        creatingObservers();
    }

    private void setup() {
        mAuth = FirebaseAuth.getInstance();
        passwordEt = findViewById(R.id.passwordEt);
        emailEt = findViewById(R.id.emailEt);
        button = findViewById(R.id.signButton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void creatingObservers() {
        mViewModel.getViewState().observe(this, new Observer<ViewState<User>>() {
            @Override
            public void onChanged(@Nullable ViewState<User> userViewState) {
                handleWithMainFlow(userViewState);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sigInFlow();
            }
        });
        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        mViewModel.getEmailState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                handleWithEmailState(state);
            }
        });
        mViewModel.getPasswordState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                handleWithPasswordState(state);
            }
        });
    }

    private void handleWithPasswordState(State state) {

    }

    private void handleWithEmailState(State state) {

    }

    private void sigInFlow() {
        try {
            String email = Objects.requireNonNull(emailEt.getText()).toString();
            String password = Objects.requireNonNull(passwordEt.getText()).toString();

        } catch (NullPointerException e) { e.printStackTrace(); }

    }

    private void handleWithMainFlow(ViewState<User> userViewState) {

    }

}
