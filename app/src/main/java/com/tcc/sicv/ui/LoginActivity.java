package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.tcc.sicv.HomeActivity;
import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.model.User;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.LoginViewModel;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.State;

import java.util.Objects;

import static com.tcc.sicv.Constants.USER_FIELD;

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
        User user = mViewModel.getUser();
        if(user != null){
            callHomeActivity(user);
        }

        mViewModel.getFlowState().observe(this, new Observer<FlowState<User>>() {
            @Override
            public void onChanged(@Nullable FlowState<User> userFlowState) {
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
                finish();
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

    private void handleWithMainFlow(FlowState<User> userFlowState) {
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
                User user = userFlowState.getData();
                if (user == null) return;
                mViewModel.saveUser(user);
                callHomeActivity(user);
                break;
        }
    }

    private void callHomeActivity(User user) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(USER_FIELD, new Gson().toJson(user));
        startActivity(intent);
        finish();
    }

}
