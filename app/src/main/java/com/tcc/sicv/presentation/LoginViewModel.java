package com.tcc.sicv.presentation;

import java.util.regex.Pattern;
import com.tcc.sicv.data.model.User;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.tcc.sicv.presentation.model.State;
import android.arch.lifecycle.MutableLiveData;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.Status;
import com.tcc.sicv.presentation.model.UserLogin;
import com.tcc.sicv.data.firebase.AuthRepository;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import static com.tcc.sicv.presentation.model.State.EMPTY;
import static com.tcc.sicv.presentation.model.State.INVALID;
import static com.tcc.sicv.presentation.model.State.VALID;
import static com.tcc.sicv.presentation.model.Status.LOADING;

public class LoginViewModel extends ViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<FlowState<User>> flowState;
    private UserLogin user;
    private MutableLiveData<State> emailState;
    private MutableLiveData<State> passwordState;
    private PreferencesHelper preferencesHelper;

    public LoginViewModel(PreferencesHelper preferences) {
        user = new UserLogin("", "");
        flowState = new MutableLiveData<>();
        flowState.setValue(new FlowState<User>());
        authRepository = new AuthRepository();
        preferencesHelper = preferences;
        emailState = new MutableLiveData<>();
        passwordState = new MutableLiveData<>();
    }

    public void saveUser(){
        FlowState<User> value = flowState.getValue();
        if (value != null && value.getStatus() == Status.SUCCESS && value.getData() != null){
            preferencesHelper.saveUser(value.getData());
        }
    }

    public void signIn(String email, String password) {
        flowState.postValue(new FlowState<User>(null, null, LOADING));
        validateEmail(email);
        validatePassword(password);
        if(emailState.getValue() != VALID || passwordState.getValue() != VALID){

            return;
        }

        user.setEmail(email);
        user.setPassword(password);
        authRepository.signIn(user, flowState);
    }

    private void validateEmail(String email) {
        String regex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(email).find()) {
            emailState.postValue(VALID);
        } else {
            if (email.isEmpty()) emailState.postValue(EMPTY);
            else emailState.postValue(INVALID);
        }
    }

    private void validatePassword(String password) {
        if (password.length() >= 6) passwordState.postValue(VALID);
        else {
            if (password.isEmpty()) passwordState.postValue(EMPTY);
            else passwordState.postValue(INVALID);
        }
    }

    public LiveData<FlowState<User>> getFlowState() {
        return flowState;
    }

    public LiveData<State> getEmailState() {
        return emailState;
    }

    public LiveData<State> getPasswordState() {
        return passwordState;
    }

}
