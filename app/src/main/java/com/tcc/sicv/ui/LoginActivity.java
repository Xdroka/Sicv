package com.tcc.sicv.ui;

import android.os.Bundle;

import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;

public class LoginActivity extends BaseActivity {
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
//        mViewModel = new LoginViewModel( ((AppBase) getApplication()).getIntractor());
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
//        mViewModel.getFlowState().observe(this, new Observer<FlowState<User>>() {
//            @Override
//            public void onChanged(@Nullable FlowState<User> userFlowState) {
//                handleWithMainFlow(userFlowState);
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
//        switch (state){
//            case EMPTY:
//                passwordEt.setError(getString(R.string.error_empty_password));
//                break;
//            case VALID:
//                passwordEt.setError(null);
//                break;
//            case INVALID:
//                passwordEt.setError(getString(R.string.error_invalid_password));
//                break;
//        }
//    }
//
//    private void handleWithEmailState(State state) {
//        switch (state){
//            case EMPTY:
//                emailEt.setError(getString(R.string.error_empty_email));
//                break;
//            case VALID:
//                emailEt.setError(null);
//                break;
//            case INVALID:
//                emailEt.setError(getString(R.string.error_invalid_email));
//                break;
//        }
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
//    private void handleWithMainFlow(FlowState<User> userFlowState) {
//
//    }

}
