package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Patterns;

import com.tcc.sicv.data.firebase.AuthRepository;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.State;
import com.tcc.sicv.data.model.Status;
import com.tcc.sicv.data.model.User;
import com.tcc.sicv.data.preferences.PreferencesHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.tcc.sicv.data.model.State.EMPTY;
import static com.tcc.sicv.data.model.State.INVALID;
import static com.tcc.sicv.data.model.State.MIN_AGE;
import static com.tcc.sicv.data.model.State.VALID;
import static com.tcc.sicv.utils.Constants.DATE_FORMAT;
import static com.tcc.sicv.utils.Constants.DATE_MIN_LENGHT;
import static com.tcc.sicv.utils.Constants.USER_MIN_AGE;

public class SignUpViewModel extends ViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<FlowState<String>> flowState;
    private MutableLiveData<State> nameState;
    private MutableLiveData<State> cpfState;
    private MutableLiveData<State> telState;
    private MutableLiveData<State> dateState;
    private MutableLiveData<State> emailState;
    private MutableLiveData<State> passwordState;
    private MutableLiveData<State> confirmPassState;
    private PreferencesHelper preferencesHelper;

    public SignUpViewModel(PreferencesHelper preferences) {
        authRepository = new AuthRepository();
        flowState = new MutableLiveData<>();
        nameState = new MutableLiveData<>();
        cpfState = new MutableLiveData<>();
        telState = new MutableLiveData<>();
        dateState = new MutableLiveData<>();
        emailState = new MutableLiveData<>();
        passwordState = new MutableLiveData<>();
        confirmPassState = new MutableLiveData<>();
        preferencesHelper = preferences;
    }

    private static boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.US);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void saveUser(String email) {
        preferencesHelper.saveUser(email);
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
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month - 1, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    private Boolean processDateData(String date) {
        int userAge = 0;
        if (date.length() == DATE_MIN_LENGHT && isDateValid(date)) {
            int day = Integer.parseInt(date.split("/")[0]);
            int month = Integer.parseInt(date.split("/")[1]);
            int year = Integer.parseInt(date.split("/")[2]);
            userAge = getAge(year, month, day);
        }
        if (date.isEmpty()) {
            dateState.postValue(EMPTY);
            return false;
        } else if (date.length() != DATE_MIN_LENGHT || !isDateValid(date)) {
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
            flowState.postValue(new FlowState<String>(null, null, Status.LOADING));

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

    public LiveData<FlowState<String>> getFlowState() {
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
