package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.tcc.sicv.data.firebase.VehiclesRepository;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.State;
import com.tcc.sicv.data.model.Vehicle;
import com.tcc.sicv.data.preferences.PreferencesHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.tcc.sicv.data.model.State.EMPTY;
import static com.tcc.sicv.data.model.State.INVALID;
import static com.tcc.sicv.data.model.State.VALID;
import static com.tcc.sicv.data.model.Status.ERROR;
import static com.tcc.sicv.data.model.Status.LOADING;
import static com.tcc.sicv.data.model.Status.NEUTRAL;
import static com.tcc.sicv.data.model.Status.SUCCESS;
import static com.tcc.sicv.utils.Constants.BUY_VEHICLE;
import static com.tcc.sicv.utils.Constants.DATE_FORMAT;
import static com.tcc.sicv.utils.Constants.DATE_MIN_LENGHT;

public class VehicleDetailsViewModel extends ViewModel {
    private MutableLiveData<FlowState<Vehicle>> flowState;
    private MutableLiveData<FlowState<Boolean>> buyState;
    private MutableLiveData<State> dateState;
    private VehiclesRepository vehiclesRepository;
    private PreferencesHelper preferencesHelper;
    private Vehicle selectedVehicle;

    public VehicleDetailsViewModel(PreferencesHelper preferences) {
        preferencesHelper = preferences;
        flowState = new MutableLiveData<>();
        dateState = new MutableLiveData<>();
        buyState = new MutableLiveData<>();
        vehiclesRepository = new VehiclesRepository();
    }

    public void getVehicle(String gson) {
        try {
            selectedVehicle = new Gson().fromJson(gson, Vehicle.class);
            flowState.postValue(new FlowState<>(selectedVehicle, null, SUCCESS));
        } catch (JsonParseException e) {
            flowState.postValue(new FlowState<Vehicle>(null, e, ERROR));
        } catch (IllegalStateException e) {
            flowState.postValue(new FlowState<Vehicle>(null, e, ERROR));
        }
    }

    private boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.US);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isBeforeToday(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        Calendar myDate = Calendar.getInstance();

        myDate.set(year, month - 1, day);
        return today.after(myDate);
    }

    private Boolean processDateData(String date) {
        int day = 0;
        int month = 0;
        int year = 0;
        if (date.length() == DATE_MIN_LENGHT && isDateValid(date)) {
            day = Integer.parseInt(date.split("/")[0]);
            month = Integer.parseInt(date.split("/")[1]);
            year = Integer.parseInt(date.split("/")[2]);
        }
        if (date.isEmpty()) {
            dateState.postValue(EMPTY);
            return false;
        } else if (date.length() != DATE_MIN_LENGHT || !isDateValid(date)) {
            dateState.postValue(INVALID);
            return false;
        } else if (isBeforeToday(year, month, day)) {
            dateState.postValue(INVALID);
            return false;
        } else {
            dateState.postValue(VALID);
            return true;
        }
    }

    public void processOperation(String date, String fromActivity) {
        Boolean validDate = processDateData(date);
        if (!validDate) return;

        if (fromActivity.equals(BUY_VEHICLE)) {
            buyState.postValue(new FlowState<Boolean>(null, null, LOADING));
            vehiclesRepository.buyVehicle(preferencesHelper.getEmail(), selectedVehicle.getModelo()
                    , buyState);
        } else {
            //fluxo da manutenção
        }
    }

    public LiveData<FlowState<Vehicle>> getFlowState() {
        return flowState;
    }

    public LiveData<FlowState<Boolean>> getBuyState() {
        return buyState;
    }

    public LiveData<State> getDateState() {
        return dateState;
    }
}
