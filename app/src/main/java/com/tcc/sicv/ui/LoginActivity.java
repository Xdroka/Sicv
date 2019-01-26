package com.tcc.sicv.ui;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.tcc.sicv.R;
import com.tcc.sicv.presentation.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel mViewModel;
    private Button button = findViewById(R.id.signButton);
    private TextInputEditText emailEt = findViewById(R.id.emailEt);
    private TextInputEditText passwordEt = findViewById(R.id.passwordEt);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mViewModel = new LoginViewModel();
        creatingObservers();
    }

    private void creatingObservers(){
//        mViewModel.g
    }

}
