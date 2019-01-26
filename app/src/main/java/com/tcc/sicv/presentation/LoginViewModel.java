package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.tcc.sicv.presentation.model.State;
import com.tcc.sicv.presentation.model.User;
import com.tcc.sicv.presentation.model.ViewState;

import java.util.regex.Pattern;

import static com.tcc.sicv.presentation.model.State.EMPTY;
import static com.tcc.sicv.presentation.model.State.INVALID;
import static com.tcc.sicv.presentation.model.State.VALID;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<ViewState<User>> viewState;
    private User user;
    private MutableLiveData<State> emailState;
    private MutableLiveData<State> passwordState;

    public LoginViewModel() {
        user = new User();
        this.viewState = new MutableLiveData<>();
        viewState.setValue(new ViewState<User>());
    }

    public void sigin(String email, String password){
        user.setEmail(email);
        user.setPassword(password);
    }

    private void validateEmail(String email) {
        String regex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        if(pattern.matcher(email).find()){ emailState.postValue(VALID); }
        else {
            if(email.isEmpty()) emailState.postValue(EMPTY);
            else emailState.postValue(INVALID);
        }
    }

    private void validatePassword(String password){
        if(password.length() >= 6) passwordState.postValue(VALID);
        else {
            if(password.isEmpty()) passwordState.postValue(EMPTY);
            else passwordState.postValue(INVALID);
        }
    }

    public LiveData<ViewState<User>> getViewState() { return viewState; }

    public LiveData<State> getEmailState() { return emailState; }

    public LiveData<State> getPasswordState() { return passwordState; }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
