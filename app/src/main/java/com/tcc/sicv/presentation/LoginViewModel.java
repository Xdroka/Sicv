package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.State;
import com.tcc.sicv.presentation.model.User;

import java.util.regex.Pattern;

import static com.tcc.sicv.presentation.model.State.EMPTY;
import static com.tcc.sicv.presentation.model.State.INVALID;
import static com.tcc.sicv.presentation.model.State.VALID;

public class LoginViewModel extends ViewModel {
//    private MutableLiveData<FlowState<User>> flowState;
//    private User user;
//    private MutableLiveData<State> emailState;
//    private MutableLiveData<State> passwordState;
//    private LoginIteractor interactor;
//
//    public LoginViewModel(LoginIteractor interactor) {
//        user = new User();
//        this.flowState = new MutableLiveData<>();
//        flowState.setValue(new FlowState<User>());
//        this.interactor = interactor;
//    }
//
//    private void setObserver() {
//
//    }
//
//    public void sigin(String email, String password) {
//        user.setEmail(email);
//        user.setPassword(password);
//
//        interactor.sigin(user, flowState);
//    }
//
//    private void validateEmail(String email) {
//        String regex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
//        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
//        if (pattern.matcher(email).find()) {
//            emailState.postValue(VALID);
//        } else {
//            if (email.isEmpty()) emailState.postValue(EMPTY);
//            else emailState.postValue(INVALID);
//        }
//    }
//
//    private void validatePassword(String password) {
//        if (password.length() >= 6) passwordState.postValue(VALID);
//        else {
//            if (password.isEmpty()) passwordState.postValue(EMPTY);
//            else passwordState.postValue(INVALID);
//        }
//    }
//
//    public LiveData<FlowState<User>> getFlowState() {
//        return flowState;
//    }
//
//    public LiveData<State> getEmailState() {
//        return emailState;
//    }
//
//    public LiveData<State> getPasswordState() {
//        return passwordState;
//    }

}
