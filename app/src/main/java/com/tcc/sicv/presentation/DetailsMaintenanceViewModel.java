package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tcc.sicv.base.Result;
import com.tcc.sicv.base.ResultListenerFactory;
import com.tcc.sicv.data.firebase.MaintenanceRepository;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Logs;
import com.tcc.sicv.data.model.MaintenanceVehicle;
import com.tcc.sicv.data.model.Ticket;
import com.tcc.sicv.data.preferences.PreferencesHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.tcc.sicv.data.model.Status.LOADING;
import static com.tcc.sicv.utils.Constants.MAINTENANCE_COLLECTION_PATH;

public class DetailsMaintenanceViewModel extends ViewModel {
    private PreferencesHelper preferencesHelper;
    private MaintenanceRepository repository;
    private MutableLiveData<FlowState<ArrayList<Logs>>> flowState;
    private Result<ArrayList<Logs>> resultListener;
    private MaintenanceVehicle maintenance;
    private Ticket ticket = null;

    public DetailsMaintenanceViewModel(
            PreferencesHelper preferencesHelper,
            MaintenanceVehicle maintenance
    ) {
        this.preferencesHelper = preferencesHelper;
        this.maintenance = maintenance;
        repository = new MaintenanceRepository();
        flowState = new MutableLiveData<>();
        resultListener = new ResultListenerFactory<ArrayList<Logs>>().create(flowState);
        flowState.setValue(new FlowState<ArrayList<Logs>>());
        requestLogsMaintenance();
    }

    public void requestLogsMaintenance() {
        if ((flowState.getValue() != null && flowState.getValue().getStatus() == LOADING)
                || maintenance == null) return;
        flowState.postValue(new FlowState<ArrayList<Logs>>(null, null, LOADING));
        repository.getLogsInMaintenance(
                preferencesHelper.getEmail(), maintenance.getMaintenanceCode(), resultListener
        );
    }

    public LiveData<FlowState<ArrayList<Logs>>> getFlowState() {
        return flowState;
    }

    private void setTicket(String totalCost){
        Date date = new Date();
        if(flowState.getValue() == null || !flowState.getValue().hasData()) return;

        ticket = new Ticket(
                totalCost,
                MAINTENANCE_COLLECTION_PATH,
                maintenance.getCod_veiculo(),
                "",
                maintenance.getMaintenanceCode(),
                null
        );
    }

    public String getTotalCost() {
        float totalCost = (float) 0;
        String result;
        if (flowState.getValue() == null || !flowState.getValue().hasData()) {
            result = convertToString(totalCost);
            setTicket(result);
            return result;
        }
        ArrayList<Logs> logsList = flowState.getValue().getData();
        for (Logs log : logsList) {
            try {
                totalCost += Float.parseFloat(log.getGasto()
                        .replace("R$", "")
                        .replace(",", "."));
            } catch (NullPointerException ignored) {
            } catch (NumberFormatException ignored) {
            }
        }
        result = convertToString(totalCost);
        setTicket(result);
        return result;
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

        return "R$ " + numberString;
    }

    public Boolean isVehicleFixed() {
        if(maintenance == null) return false;
        return maintenance.getVeiculo_liberado();
    }

    public Ticket getTicket() {
        return ticket;
    }
}
