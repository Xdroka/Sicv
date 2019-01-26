package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.tcc.sicv.R;
import com.tcc.sicv.presentation.LoginViewModel;
import com.tcc.sicv.presentation.model.User;
import com.tcc.sicv.presentation.model.ViewState;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel mViewModel;
    private Button button = findViewById(R.id.signButton);
    private TextInputEditText emailEt = findViewById(R.id.emailEt);
    private TextInputEditText passwordEt = findViewById(R.id.passwordEt);
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mViewModel = new LoginViewModel();
        creatingObservers();
        mAuth = FirebaseAuth.getInstance();
    }

    private void creatingObservers(){
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
    }

    private void sigInFlow() {

    }

    private void handleWithMainFlow(ViewState<User> userViewState) {

    }

}
