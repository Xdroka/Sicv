package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tcc.sicv.base.Result;
import com.tcc.sicv.base.ResultListenerFactory;
import com.tcc.sicv.data.firebase.MaintenanceRepository;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.MaintenanceVehicle;

import java.util.ArrayList;

public class MaintenanceViewModel extends ViewModel {
    private MutableLiveData<FlowState<ArrayList<MaintenanceVehicle>>> flowState;
    private MaintenanceRepository maintenanceRepository;
    private PreferencesHelper preferencesHelper;
    private Result<ArrayList<MaintenanceVehicle>> resultListener;

    public MaintenanceViewModel(PreferencesHelper preferences){
        flowState = new MutableLiveData<>();
        resultListener = new ResultListenerFactory<ArrayList<MaintenanceVehicle>>()
                .create(flowState);
        flowState.setValue(new FlowState<ArrayList<MaintenanceVehicle>>());
        maintenanceRepository = new MaintenanceRepository();
        preferencesHelper = preferences;
        requestVehiclesInMaintenance();
    }

    public void requestVehiclesInMaintenance(){
        if(flowState.getValue() != null && flowState.getValue().isLoading()) return;
        //noinspection ConstantConditions
        flowState.getValue().loading();
        maintenanceRepository.getVehiclesInMaintenance(preferencesHelper.getEmail(), resultListener);
    }

    public LiveData<FlowState<ArrayList<MaintenanceVehicle>>> getFlowState() { return flowState; }
}
