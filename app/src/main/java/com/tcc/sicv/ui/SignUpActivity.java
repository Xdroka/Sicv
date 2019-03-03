package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.presentation.SignUpViewModel;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.State;
import com.vicmikhailau.maskededittext.MaskedEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends BaseActivity {
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
        mViewModel = new SignUpViewModel();
        setup();

        creatingObservers();
        //configFirebase();
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
        mViewModel.getFlowState().observe(this, new Observer<FlowState<Boolean>>() {
            @Override
            public void onChanged(@Nullable FlowState<Boolean> userFlowState) {
                handleWithMainFlow(userFlowState);
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

    private void handleWithMainFlow(FlowState<Boolean> userFlowState) {
        Toast.makeText(this, "Sucesso", Toast.LENGTH_SHORT).show();
    }
}