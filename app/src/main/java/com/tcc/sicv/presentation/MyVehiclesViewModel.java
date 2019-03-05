package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tcc.sicv.data.firebase.AuthRepository;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.Vehicle;

import java.util.ArrayList;

import static com.tcc.sicv.presentation.model.Status.LOADING;
import static com.tcc.sicv.presentation.model.Status.NEUTRAL;

public class MyVehiclesViewModel extends ViewModel{
    private MutableLiveData<FlowState<ArrayList<Vehicle>>> flowState;
    private AuthRepository authRepository;
    private PreferencesHelper preferencesHelper;

    public MyVehiclesViewModel(PreferencesHelper preferences) {
        preferencesHelper = preferences;
        authRepository = new AuthRepository();
        flowState = new MutableLiveData<>();
        flowState.postValue(new FlowState<ArrayList<Vehicle>>(null, null, NEUTRAL));
        requestMyVehicles();
    }

    public void requestMyVehicles() {
        if (flowState.getValue() != null && flowState.getValue().getStatus() == LOADING) return;
        flowState.postValue(new FlowState<ArrayList<Vehicle>>(null, null, LOADING));
        authRepository.getMyVehicles(preferencesHelper.getEmail(), flowState);
    }

    public LiveData<FlowState<ArrayList<Vehicle>>> getFlowState() {
        return flowState;
    }
}
