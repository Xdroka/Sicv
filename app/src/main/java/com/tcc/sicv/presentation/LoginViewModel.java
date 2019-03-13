package com.tcc.sicv.presentation;

import java.util.regex.Pattern;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.tcc.sicv.base.Result;
import com.tcc.sicv.base.ResultListenerFactory;
import com.tcc.sicv.data.model.State;

import android.arch.lifecycle.MutableLiveData;

import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.firebase.AuthRepository;
import com.tcc.sicv.data.preferences.PreferencesHelper;

import static com.tcc.sicv.data.model.State.EMPTY;
import static com.tcc.sicv.data.model.State.INVALID;
import static com.tcc.sicv.data.model.State.VALID;
import static com.tcc.sicv.data.model.Status.LOADING;
import static com.tcc.sicv.data.model.Status.NEUTRAL;

public class LoginViewModel extends ViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<FlowState<String>> flowState;
    private MutableLiveData<State> emailState;
    private MutableLiveData<State> passwordState;
    private PreferencesHelper preferencesHelper;
    private Result<String> resultListener;

    public LoginViewModel(PreferencesHelper preferences) {
        flowState = new MutableLiveData<>();
        flowState.setValue(new FlowState<String>(null, null, NEUTRAL));
        resultListener = new ResultListenerFactory<String>().create(flowState);
        authRepository = new AuthRepository();
        preferencesHelper = preferences;
        emailState = new MutableLiveData<>();
        passwordState = new MutableLiveData<>();
    }

    public void saveUser(String email) {
        preferencesHelper.saveUser(email);
    }

    public String getUser() {
        return preferencesHelper.getEmail();
    }

    public void signIn(String email, String password) {
        if (flowState.getValue() != null && flowState.getValue().getStatus() == LOADING) return;
        validateEmail(email);
        validatePassword(password);
        if (emailState.getValue() != VALID || passwordState.getValue() != VALID) return;

        flowState.postValue(new FlowState<String>(null, null, LOADING));
        authRepository.signIn(email, password, resultListener);
    }

    private void validateEmail(String email) {
        String regex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(email).find()) {
            emailState.setValue(VALID);
        } else {
            if (email.isEmpty()) emailState.setValue(EMPTY);
            else emailState.setValue(INVALID);
        }
    }

    private void validatePassword(String password) {
        if (password.length() >= 6) passwordState.setValue(VALID);
        else {
            if (password.isEmpty()) passwordState.setValue(EMPTY);
            else passwordState.setValue(INVALID);
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

}
