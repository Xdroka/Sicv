package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Vehicle;

import static com.tcc.sicv.data.model.Status.ERROR;
import static com.tcc.sicv.data.model.Status.SUCCESS;

public class VehicleDetailsViewModel extends ViewModel {
    private MutableLiveData<FlowState<Vehicle>> flowState;

    public VehicleDetailsViewModel() {
        flowState = new MutableLiveData<>();
    }

    public void getVehicle(String gson) {
        try {
            Vehicle clickedVehicle = new Gson().fromJson(gson, Vehicle.class);
            flowState.postValue(new FlowState<>(clickedVehicle, null, SUCCESS));
        } catch (JsonParseException e) {
            flowState.postValue(new FlowState<Vehicle>(null, e, ERROR));
        } catch (IllegalStateException e) {
            flowState.postValue(new FlowState<Vehicle>(null, e, ERROR));
        }
    }

    public LiveData<FlowState<Vehicle>> getFlowState() {
        return flowState;
    }
}
