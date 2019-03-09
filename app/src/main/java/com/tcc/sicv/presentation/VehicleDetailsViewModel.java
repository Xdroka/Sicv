package com.tcc.sicv.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Logs;
import com.tcc.sicv.data.model.State;
import com.tcc.sicv.data.model.Vehicle;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.tcc.sicv.data.model.State.EMPTY;
import static com.tcc.sicv.data.model.State.INVALID;
import static com.tcc.sicv.data.model.State.MIN_AGE;
import static com.tcc.sicv.data.model.State.VALID;
import static com.tcc.sicv.data.model.Status.ERROR;
import static com.tcc.sicv.data.model.Status.SUCCESS;
import static com.tcc.sicv.utils.Constants.DATE_FORMAT;
import static com.tcc.sicv.utils.Constants.DATE_MIN_LENGHT;

public class VehicleDetailsViewModel extends ViewModel {
    private MutableLiveData<FlowState<Vehicle>> flowState;
    private MutableLiveData<State> dateState;

    public VehicleDetailsViewModel() {
        flowState = new MutableLiveData<>();
        dateState = new MutableLiveData<>();
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

    private boolean isDateValid(String date)
    {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.US);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isBeforeToday(int year, int month, int day)
    {
        Calendar today = Calendar.getInstance();
        Calendar myDate = Calendar.getInstance();

        myDate.set(year, month-1, day);
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
        } else if (isBeforeToday(year,month,day)){
            dateState.postValue(INVALID);
            return false;
        } else {
            dateState.postValue(VALID);
            return true;
        }
    }

    public void processBuy(String date){
        Boolean validDate = processDateData(date);
        if (!validDate) return;
        Log.i("TODO","Fazer fluxo de compra do veículo");
    }

    public LiveData<FlowState<Vehicle>> getFlowState() {
        return flowState;
    }

    public LiveData<State> getDateState() { return dateState; }
}
