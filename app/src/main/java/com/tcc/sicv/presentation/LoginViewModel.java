package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.tcc.sicv.presentation.model.State;
import com.tcc.sicv.presentation.model.User;
import com.tcc.sicv.presentation.model.ViewState;

import java.util.regex.Pattern;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<ViewState<User>> viewState;
    private User user;
    public MutableLiveData<State> emailState;
    public MutableLiveData<State> passwordState;

    public LoginViewModel() {
        user = new User();
        this.viewState = new MutableLiveData<>();
        viewState.setValue(new ViewState<User>());
    }

    public void sigin(String email, String password){
        user.setEmail(email);
        user.setPassword(password);



    }

    private boolean validateEmail(String email) {
        String regex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).find();
    }

    private boolean validatePassword(String password){
        return password.length() >= 6;
    }

    public LiveData<ViewState<User>> getViewState() {
        return viewState;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
