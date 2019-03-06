package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tcc.sicv.data.firebase.AuthRepository;
import com.tcc.sicv.data.model.User;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.data.model.FlowState;

import static com.tcc.sicv.data.model.Status.LOADING;

public class ProfileViewModel extends ViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<FlowState<User>> flowState;
    private MutableLiveData<FlowState<Void>> logoutState;
    private PreferencesHelper preferencesHelper;

    public ProfileViewModel(PreferencesHelper preferences) {
        authRepository = new AuthRepository();
        logoutState = new MutableLiveData<>();
        flowState = new MutableLiveData<>();
        preferencesHelper = preferences;
        flowState.postValue(new FlowState<User>(null, null, LOADING));
    }

    public void logout() {
        logoutState.postValue(new FlowState<Void>(null, null, LOADING));
        preferencesHelper.logout();
        authRepository.logout(logoutState);
    }

    public void getUserProfile() {
        authRepository.getUserProfile(preferencesHelper.getEmail(), flowState);
    }

    public LiveData<FlowState<User>> getFlowState() {
        return flowState;
    }

    public LiveData<FlowState<Void>> getLogoutState() {
        return logoutState;
    }
}
