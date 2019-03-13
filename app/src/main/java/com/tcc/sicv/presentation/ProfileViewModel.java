package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tcc.sicv.base.Result;
import com.tcc.sicv.base.ResultListenerFactory;
import com.tcc.sicv.data.firebase.AuthRepository;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.User;
import com.tcc.sicv.data.preferences.PreferencesHelper;

import static com.tcc.sicv.data.model.Status.LOADING;

public class ProfileViewModel extends ViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<FlowState<User>> flowState;
    private MutableLiveData<FlowState<Void>> logoutState;
    private PreferencesHelper preferencesHelper;
    private Result<User> mainResultListener;
    private Result<Void> logoutListener;

    public ProfileViewModel(PreferencesHelper preferences) {
        authRepository = new AuthRepository();
        logoutState = new MutableLiveData<>();
        logoutListener = new ResultListenerFactory<Void>().create(logoutState);
        flowState = new MutableLiveData<>();
        mainResultListener = new ResultListenerFactory<User>().create(flowState);
        preferencesHelper = preferences;
        getUserProfile();
    }

    public void logout() {
        logoutState.postValue(new FlowState<Void>(null, null, LOADING));
        preferencesHelper.logout();
        authRepository.logout(logoutListener);
    }

    public void getUserProfile() {
        flowState.postValue(new FlowState<User>(null, null, LOADING));
        authRepository.getUserProfile(preferencesHelper.getEmail(), mainResultListener);
    }

    public LiveData<FlowState<User>> getFlowState() {
        return flowState;
    }

    public LiveData<FlowState<Void>> getLogoutState() {
        return logoutState;
    }
}
