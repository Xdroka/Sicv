package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;

import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.LoginViewModel;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.State;

import java.util.Objects;

public class LoginActivity extends BaseActivity {
    private LoginViewModel mViewModel;
    private Button button;
    private AppCompatEditText emailEt;
    private AppCompatEditText passwordEt;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mViewModel = new LoginViewModel(new PreferencesHelper(getApplication()));
        setup();
        creatingObservers();
    }

    private void setup() {
        passwordEt = findViewById(R.id.passwordEt);
        emailEt = findViewById(R.id.emailEt);
        button = findViewById(R.id.signButton);
        registerButton = findViewById(R.id.registerButton);
    }

    private void creatingObservers() {
        String email = mViewModel.getUser();
        if (email != null) {
            callHomeActivity();
        }

        mViewModel.getFlowState().observe(this, new Observer<FlowState<String>>() {
            @Override
            public void onChanged(@Nullable FlowState<String> userFlowState) {
                if (userFlowState == null) return;
                handleWithMainFlow(userFlowState);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sigInFlow();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        mViewModel.getEmailState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                if (state == null) return;
                handleWithEmailState(state);
            }
        });

        mViewModel.getPasswordState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                if (state == null) return;
                handleWithPasswordState(state);
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

    private void sigInFlow() {
        try {
            String email = Objects.requireNonNull(emailEt.getText()).toString();
            String password = Objects.requireNonNull(passwordEt.getText()).toString();
            mViewModel.signIn(email, password);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void handleWithMainFlow(FlowState<String> userFlowState) {
        switch (userFlowState.getStatus()) {
            case ERROR:
                hideLoadingDialog();
                handleErrors(userFlowState.getThrowable());
                break;
            case NEUTRAL:
                break;
            case LOADING:
                showLoadingDialog();
                break;
            case SUCCESS:
                hideLoadingDialog();
                String email = userFlowState.getData();
                if (email == null) return;
                mViewModel.saveUser(email);
                callHomeActivity();
                break;
        }
    }

    private void callHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
