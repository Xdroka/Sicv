package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Patterns;

import com.tcc.sicv.data.model.User;
import com.tcc.sicv.data.remote.firebase.AuthRepository;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.State;
import com.tcc.sicv.presentation.model.Status;

import java.util.Date;

import static com.tcc.sicv.presentation.model.State.EMPTY;
import static com.tcc.sicv.presentation.model.State.INVALID;
import static com.tcc.sicv.presentation.model.State.MIN_AGE;
import static com.tcc.sicv.presentation.model.State.VALID;

public class SignUpViewModel extends ViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<FlowState<Boolean>> flowState;
    private MutableLiveData<State> nameState;
    private MutableLiveData<State> cpfState;
    private MutableLiveData<State> telState;
    private MutableLiveData<State> dateState;
    private MutableLiveData<State> emailState;
    private MutableLiveData<State> passwordState;
    private MutableLiveData<State> confirmPassState;

    public SignUpViewModel() {
        authRepository = new AuthRepository();
        flowState = new MutableLiveData<>();
        nameState = new MutableLiveData<>();
        cpfState = new MutableLiveData<>();
        telState = new MutableLiveData<>();
        dateState = new MutableLiveData<>();
        emailState = new MutableLiveData<>();
        passwordState = new MutableLiveData<>();
        confirmPassState = new MutableLiveData<>();
    }

    private boolean isEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private Boolean processEmailData(String email) {
        if (email.isEmpty()) {
            emailState.postValue(EMPTY);
            return false;
        } else if (isEmail(email)) {
            emailState.postValue(VALID);
            return true;
        } else {
            emailState.postValue(INVALID);
            return false;
        }
    }

    private Boolean processNameData(String name) {
        if (name.isEmpty()) {
            nameState.postValue(EMPTY);
            return false;
        } else {
            nameState.postValue(VALID);
            return true;
        }
    }

    private Boolean processPasswordData(String password) {
        int PASSWORD_MIN_LENGHT = 6;
        if (password.isEmpty()) {
            passwordState.postValue(EMPTY);
            return false;
        } else if (password.length() < PASSWORD_MIN_LENGHT) {
            passwordState.postValue(INVALID);
            return false;
        } else {
            passwordState.postValue(VALID);
            return true;
        }
    }

    private Boolean processConfirmPasswordData(String confirmPassword, String password) {
        if (!confirmPassword.equals(password)) {
            confirmPassState.postValue(INVALID);
            return false;
        } else {
            confirmPassState.postValue(VALID);
            return true;
        }
    }

    private Boolean processTelData(String telephone) {
        int TELEPHONE_MIN_LENGHT = 14;
        if (telephone.isEmpty()) {
            telState.postValue(EMPTY);
            return false;
        } else if (telephone.length() < TELEPHONE_MIN_LENGHT) {
            telState.postValue(INVALID);
            return false;
        } else {
            telState.postValue(VALID);
            return true;
        }
    }

    private Boolean processCpfData(String cpf) {
        int CPF_MIN_LENGHT = 14;
        if (cpf.isEmpty()) {
            cpfState.postValue(EMPTY);
            return false;
        } else if (cpf.length() != CPF_MIN_LENGHT) {
            cpfState.postValue(INVALID);
            return false;
        } else {
            cpfState.postValue(VALID);
            return true;
        }
    }

    private int getAge(int year, int month, int day) {
        Date now = new Date();
        int nowMonth = now.getMonth() + 1;
        int nowYear = now.getYear() + 1900;
        int result = nowYear - year;

        if (month > nowMonth) {
            result--;
        } else if (month == nowMonth) {
            int nowDay = now.getDate();

            if (day > nowDay) {
                result--;
            }
        }
        return result;
    }

    private boolean validDate(int year, int month, int day) {
        return year >= 1900 && month >= 1 && month <= 12 && day >= 1 && day <= 32;
    }

    private Boolean processDateData(String date) {
        int DATE_MIN_LENGHT = 10;
        int USER_MIN_AGE = 18;
        int userAge = 0;
        int day = 0;
        int month = 0;
        int year = 0;
        if (date.length() == DATE_MIN_LENGHT) {
            day = Integer.parseInt(date.split("/")[0]);
            month = Integer.parseInt(date.split("/")[1]);
            year = Integer.parseInt(date.split("/")[2]);
            userAge = getAge(year, month, day);
        }
        if (date.isEmpty()) {
            dateState.postValue(EMPTY);
            return false;
        } else if (date.length() != DATE_MIN_LENGHT) {
            dateState.postValue(INVALID);
            return false;
        } else if (!validDate(year, month, day)) {
            dateState.postValue(INVALID);
            return false;
        } else if (userAge < USER_MIN_AGE) {
            dateState.postValue(MIN_AGE);
            return false;
        } else {
            dateState.postValue(VALID);
            return true;
        }
    }

    public void processAllValidation(
            String email,
            String password,
            String name,
            String cpf,
            String tel,
            String date,
            String confirmPassword
    ) {
        Boolean validEmail = processEmailData(email);
        Boolean validPassword = processPasswordData(password);
        Boolean validName = processNameData(name);
        Boolean validCpf = processCpfData(cpf);
        Boolean validTelephone = processTelData(tel);
        Boolean validDate = processDateData(date);
        Boolean validConfirmPassword = processConfirmPasswordData(confirmPassword, password);

        if (
                validEmail && validName && validPassword && validConfirmPassword &&
                        validTelephone && validCpf && validDate
        ) {
            flowState.postValue(new FlowState<Boolean>(null, null, Status.LOADING));

            authRepository.checkAccountExistAndCreateUser(
                    new User(
                            email,
                            password,
                            name,
                            cpf,
                            tel,
                            date
                    ),
                    flowState
            );
        }
    }

    public LiveData<FlowState<Boolean>> getFlowState() {
        return flowState;
    }

    public LiveData<State> getEmailState() {
        return emailState;
    }

    public LiveData<State> getPasswordState() {
        return passwordState;
    }

    public LiveData<State> getNameState() {
        return nameState;
    }

    public LiveData<State> getTelState() {
        return telState;
    }

    public LiveData<State> getCpfState() {
        return cpfState;
    }

    public LiveData<State> getDateState() {
        return dateState;
    }

    public LiveData<State> getConfirmPassState() {
        return confirmPassState;
    }
}
