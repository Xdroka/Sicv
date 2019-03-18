package com.tcc.sicv.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.tcc.sicv.base.Result
import com.tcc.sicv.base.ResultListenerFactory
import com.tcc.sicv.data.firebase.MaintenanceRepository
import com.tcc.sicv.data.firebase.VehiclesRepository
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.FlowState.Companion.failure
import com.tcc.sicv.data.model.FlowState.Companion.success
import com.tcc.sicv.data.model.MaintenanceVehicle
import com.tcc.sicv.data.model.State
import com.tcc.sicv.data.model.State.*
import com.tcc.sicv.data.model.Status.LOADING
import com.tcc.sicv.data.model.Vehicle
import com.tcc.sicv.data.preferences.PreferencesHelper
import com.tcc.sicv.utils.Constants.BUY_VEHICLE
import com.tcc.sicv.utils.Constants.DATE_FORMAT
import com.tcc.sicv.utils.Constants.DATE_MIN_LENGHT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class VehicleDetailsViewModel(private val preferencesHelper: PreferencesHelper) : ViewModel() {
    private val flowState: MutableLiveData<FlowState<Vehicle>> = MutableLiveData()
    private val buyFlow: MutableLiveData<FlowState<Vehicle>> = MutableLiveData()
    private val maintenanceFlow: MutableLiveData<FlowState<MaintenanceVehicle>>
    private val buyResult: Result<Vehicle>
    private val maintenanceResult: Result<MaintenanceVehicle>
    private val dateState: MutableLiveData<State> = MutableLiveData()
    private val vehiclesRepository: VehiclesRepository
    private val maintenanceRepository: MaintenanceRepository
    private var selectedVehicle: Vehicle? = null
    var selectedDate: String? = null
        private set

    init {
        buyResult = ResultListenerFactory<Vehicle>().create(buyFlow)
        vehiclesRepository = VehiclesRepository()
        maintenanceRepository = MaintenanceRepository()
        maintenanceFlow = MutableLiveData()
        maintenanceResult = ResultListenerFactory<MaintenanceVehicle>().create(maintenanceFlow)
    }

    fun getVehicle(gson: String) {
        try {
            selectedVehicle = Gson().fromJson<Vehicle>(gson, Vehicle::class.java)
            flowState.postValue(success(selectedVehicle!!))
        } catch (e: JsonParseException) {
            flowState.postValue(failure(e))
        } catch (e: IllegalStateException) {
            flowState.postValue(failure(e))
        }

    }

    private fun isDateValid(date: String): Boolean {
        try {
            val df = SimpleDateFormat(DATE_FORMAT, Locale.US)
            df.isLenient = false
            df.parse(date)
            return true
        } catch (e: ParseException) {
            return false
        }

    }

    private fun isBeforeToday(year: Int, month: Int, day: Int): Boolean {
        val today = Calendar.getInstance()
        val myDate = Calendar.getInstance()

        myDate.set(year, month - 1, day)
        return today.after(myDate)
    }

    private fun processDateData(date: String): Boolean {
        var day = 0
        var month = 0
        var year = 0
        if (date.length == DATE_MIN_LENGHT && isDateValid(date)) {
            day = Integer.parseInt(date.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
            month = Integer.parseInt(date.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
            year = Integer.parseInt(date.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2])
        }
        if (date.isEmpty()) {
            dateState.postValue(EMPTY)
            return false
        } else if (date.length != DATE_MIN_LENGHT || !isDateValid(date)) {
            dateState.postValue(INVALID)
            return false
        } else if (isBeforeToday(year, month, day)) {
            dateState.postValue(INVALID)
            return false
        } else {
            dateState.postValue(VALID)
            return true
        }
    }

    fun processOperation(date: String, fromActivity: String) {
        val validDate = processDateData(date)
        if (!validDate) return

        if (fromActivity == BUY_VEHICLE) {
            this.selectedDate = date
            buyFlow.postValue(FlowState<Vehicle>(null, null, LOADING))
            vehiclesRepository.buyVehicle(
                    preferencesHelper.email ?: "",
                    selectedVehicle?.modelo ?:"",
                    buyResult
            )
        } else {
            maintenanceFlow.postValue(
                    FlowState<MaintenanceVehicle>(null, null, LOADING)
            )
            selectedVehicle?.let {
                maintenanceRepository.setVehicleInMaintenance(
                        preferencesHelper.email?:"", it, date, maintenanceResult
                )
            }
        }
    }

    fun getFlowState(): LiveData<FlowState<Vehicle>> = flowState
    fun getBuyFlow(): LiveData<FlowState<Vehicle>> = buyFlow
    fun getDateState(): LiveData<State> = dateState
    fun getMaintenanceFlow(): LiveData<FlowState<MaintenanceVehicle>> = maintenanceFlow
}
