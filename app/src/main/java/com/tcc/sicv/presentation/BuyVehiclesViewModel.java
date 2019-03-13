package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tcc.sicv.base.Result;
import com.tcc.sicv.base.ResultListenerFactory;
import com.tcc.sicv.data.firebase.VehiclesRepository;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Vehicle;
import com.tcc.sicv.data.preferences.PreferencesHelper;

import java.util.ArrayList;

import static com.tcc.sicv.data.model.Status.LOADING;
import static com.tcc.sicv.data.model.Status.NEUTRAL;

public class BuyVehiclesViewModel  extends ViewModel {
    private MutableLiveData<FlowState<ArrayList<Vehicle>>> flowState;
    private VehiclesRepository vehiclesRepository;
    private Result<ArrayList<Vehicle>> resultListener;

    public BuyVehiclesViewModel() {
        vehiclesRepository = new VehiclesRepository();
        flowState = new MutableLiveData<>();
        resultListener = new ResultListenerFactory<ArrayList<Vehicle>>().create(flowState);
        flowState.postValue(new FlowState<ArrayList<Vehicle>>(null, null, NEUTRAL));
        requestMyVehicles();
    }

    public void requestMyVehicles() {
        if (flowState.getValue() != null && flowState.getValue().getStatus() == LOADING) return;
        flowState.postValue(new FlowState<ArrayList<Vehicle>>(null, null, LOADING));
        vehiclesRepository.getAllVehicles(resultListener);
    }

    public LiveData<FlowState<ArrayList<Vehicle>>> getFlowState() {
        return flowState;
    }
}
