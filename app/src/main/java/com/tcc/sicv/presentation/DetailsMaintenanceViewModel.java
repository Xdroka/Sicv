package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tcc.sicv.data.firebase.MaintenanceRepository;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Logs;
import com.tcc.sicv.data.preferences.PreferencesHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.tcc.sicv.data.model.Status.LOADING;

public class DetailsMaintenanceViewModel extends ViewModel {
    private PreferencesHelper preferencesHelper;
    private MaintenanceRepository repository;
    private MutableLiveData<FlowState<ArrayList<Logs>>> flowState;
    private String codeMaintenance;

    public DetailsMaintenanceViewModel(PreferencesHelper preferencesHelper, String codeMaintenance) {
        this.preferencesHelper = preferencesHelper;
        this.codeMaintenance = codeMaintenance;
        repository = new MaintenanceRepository();
        flowState = new MutableLiveData<>();
        flowState.setValue(new FlowState<ArrayList<Logs>>());
        requestLogsMaintenance();
    }

    public void requestLogsMaintenance() {
        if (flowState.getValue() != null && flowState.getValue().getStatus() == LOADING) return;
        flowState.postValue(new FlowState<ArrayList<Logs>>(null, null, LOADING));
        repository.getLogsInMaintenance(preferencesHelper.getEmail(), codeMaintenance, flowState);
    }

    public LiveData<FlowState<ArrayList<Logs>>> getFlowState() {
        return flowState;
    }

    public String getTotalCost() {
        float totalCost = (float) 0;
        if (flowState.getValue() == null || !flowState.getValue().hasData()){
            return convertToString(totalCost);
        }
        ArrayList<Logs> logsList = flowState.getValue().getData();
        for (Logs log : logsList) {
            try {
                totalCost += Float.parseFloat(log.getCost()
                        .replace("R$", "")
                        .replace(",", "."));
            } catch (NullPointerException ignored) {
            } catch (NumberFormatException ignored) {
            }
        }
        return convertToString(totalCost);
    }

    private String convertToString(Float number) {
        String numberString;
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            numberString = df.format(number)
                    .replace(".", ",");
        } catch (Exception ignored) {
            numberString = "0,00";
        }

        return numberString;
    }
}
