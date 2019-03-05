package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tcc.sicv.data.firebase.MaintenanceRepository;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.MaintenanceVehicle;

import java.util.ArrayList;

public class MaintenanceViewModel extends ViewModel {
    private MutableLiveData<FlowState<ArrayList<MaintenanceVehicle>>> flowState;
    private MaintenanceRepository repository;
    private PreferencesHelper preferencesHelper;

    public MaintenanceViewModel(PreferencesHelper preferences){
        flowState = new MutableLiveData<>();
        flowState.setValue(new FlowState<ArrayList<MaintenanceVehicle>>());
        repository = new MaintenanceRepository();
        preferencesHelper = preferences;
        requestVehiclesInMaintenance();
    }

    public void requestVehiclesInMaintenance(){
        if(flowState.getValue() != null && flowState.getValue().isLoading()) return;
        //noinspection ConstantConditions
        flowState.getValue().loading();
        repository.getVehiclesInMaintenance(preferencesHelper.getEmail(), flowState);
    }

    public LiveData<FlowState<ArrayList<MaintenanceVehicle>>> getFlowState() { return flowState; }
}
