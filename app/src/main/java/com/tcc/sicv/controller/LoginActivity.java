package com.tcc.sicv.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tcc.sicv.R;

public class LoginActivity extends AppCompatActivity {
//    private LoginViewModel mViewModel;
//    private Button button;
//    private TextInputEditText emailEt;
//    private TextInputEditText passwordEt;
//    private FirebaseAuth mAuth;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        mViewModel = new LoginViewModel();
//        setup();
//        creatingObservers();
    }
//
//    private void setup() {
//        mAuth = FirebaseAuth.getInstance();
//        passwordEt = findViewById(R.id.passwordEt);
//        emailEt = findViewById(R.id.emailEt);
//        button = findViewById(R.id.signButton);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//    }
//
//    private void creatingObservers() {
//        mViewModel.getViewState().observe(this, new Observer<ViewState<User>>() {
//            @Override
//            public void onChanged(@Nullable ViewState<User> userViewState) {
//                handleWithMainFlow(userViewState);
//            }
//        });
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sigInFlow();
//            }
//        });
//        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
//            }
//        });
//        mViewModel.getEmailState().observe(this, new Observer<State>() {
//            @Override
//            public void onChanged(@Nullable State state) {
//                handleWithEmailState(state);
//            }
//        });
//        mViewModel.getPasswordState().observe(this, new Observer<State>() {
//            @Override
//            public void onChanged(@Nullable State state) {
//                handleWithPasswordState(state);
//            }
//        });
//    }
//
//    private void handleWithPasswordState(State state) {
//
//    }
//
//    private void handleWithEmailState(State state) {
//
//    }
//
//    private void sigInFlow() {
//        try {
//            String email = Objects.requireNonNull(emailEt.getText()).toString();
//            String password = Objects.requireNonNull(passwordEt.getText()).toString();
//
//        } catch (NullPointerException e) { e.printStackTrace(); }
//
//    }
//
//    private void handleWithMainFlow(ViewState<User> userViewState) {
//
//    }

}
