package com.tcc.sicv.ui;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;

import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.State;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.SignUpViewModel;
import com.tcc.sicv.utils.OnAlertButtonClick;
import com.vicmikhailau.maskededittext.MaskedEditText;

import java.util.Objects;

public class SignUpActivity extends BaseActivity {
    private AlertDialog successSignUpDialog;
    private SignUpViewModel mViewModel;
    private AppCompatEditText nameEt;
    private MaskedEditText cpfEt;
    private MaskedEditText telEt;
    private MaskedEditText dateEt;
    private AppCompatEditText emailEt;
    private AppCompatEditText passwordEt;
    private AppCompatEditText confirmPasswordEt;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mViewModel = new SignUpViewModel(new PreferencesHelper(getApplication()));
        setup();
        creatingObservers();
    }

    private void setup() {
        nameEt = findViewById(R.id.nameEt);
        cpfEt = findViewById(R.id.cpfEt);
        telEt = findViewById(R.id.telephoneEt);
        dateEt = findViewById(R.id.dateEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        confirmPasswordEt = findViewById(R.id.confirmPasswordEt);
        button = findViewById(R.id.signUpButton);
    }

    private void creatingObservers() {
        mViewModel.getFlowState().observe(this, new Observer<FlowState<String>>() {
            @Override
            public void onChanged(@Nullable FlowState<String> flowState) {
                if (flowState != null) {
                    handleWithMainFlow(flowState);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String email = Objects.requireNonNull(emailEt.getText()).toString();
                    String password = Objects.requireNonNull(passwordEt.getText()).toString();
                    String name = Objects.requireNonNull(nameEt.getText()).toString();
                    String cpf = Objects.requireNonNull(cpfEt.getText()).toString();
                    String tel = Objects.requireNonNull(telEt.getText()).toString();
                    String date = Objects.requireNonNull(dateEt.getText()).toString();
                    String confirmPass = Objects.requireNonNull(confirmPasswordEt.getText()).toString();
                    mViewModel.processAllValidation(
                            email,
                            password,
                            name,
                            cpf,
                            tel,
                            date,
                            confirmPass
                    );
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
        mViewModel.getEmailState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                if (state != null) {
                    handleWithEmailState(state);
                }
            }
        });
        mViewModel.getPasswordState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                if (state != null) {
                    handleWithPasswordState(state);
                }
            }
        });
        mViewModel.getNameState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                if (state != null) {
                    handleWithNameState(state);
                }
            }
        });
        mViewModel.getCpfState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                if (state != null) {
                    handleWithCpfState(state);
                }
            }
        });
        mViewModel.getTelState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                if (state != null) {
                    handleWithTelState(state);
                }
            }
        });
        mViewModel.getDateState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                if (state != null) {
                    handleWithDateState(state);
                }
            }
        });
        mViewModel.getConfirmPassState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                if (state != null) {
                    handleWithConfirmPassState(state);
                }
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

    private void handleWithDateState(State state) {
        switch (state) {
            case EMPTY:
                dateEt.setError(getString(R.string.empty_date));
                break;
            case VALID:
                dateEt.setError(null);
                break;
            case INVALID:
                dateEt.setError(getString(R.string.invalid_date));
                break;
            case MIN_AGE:
                dateEt.setError(getString(R.string.invalid_age_date));
                break;
        }
    }

    private void handleWithTelState(State state) {
        switch (state) {
            case EMPTY:
                telEt.setError(getString(R.string.empty_telephone));
                break;
            case VALID:
                telEt.setError(null);
                break;
            case INVALID:
                telEt.setError(getString(R.string.invalid_telephone));
                break;
        }
    }

    private void handleWithCpfState(State state) {
        switch (state) {
            case EMPTY:
                cpfEt.setError(getString(R.string.empty_cpf));
                break;
            case VALID:
                cpfEt.setError(null);
                break;
            case INVALID:
                cpfEt.setError(getString(R.string.invalid_cpf));
                break;
        }
    }

    private void handleWithNameState(State state) {
        switch (state) {
            case EMPTY:
                nameEt.setError(getString(R.string.empty_name));
                break;
            case VALID:
                nameEt.setError(null);
                break;
        }
    }

    private void handleWithConfirmPassState(State state) {
        switch (state) {
            case VALID:
                confirmPasswordEt.setError(null);
                break;
            case INVALID:
                confirmPasswordEt.setError(getString(R.string.error_confirm_password));
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

    private void handleWithMainFlow(FlowState<String> flowState) {
        switch (flowState.getStatus()) {
            case LOADING:
                showLoadingDialog();
                break;
            case SUCCESS:
                hideLoadingDialog();
                String email = flowState.getData();
                if (email == null) return;
                mViewModel.saveUser(email);
                createConfirmAndExitDialog(getString(R.string.successfulSignUp),dismissListener);
                break;
            case ERROR:
                hideLoadingDialog();
                handleErrors(flowState.getThrowable());
                break;
        }
    }

    private DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    };
}