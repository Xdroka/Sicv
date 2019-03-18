package com.tcc.sicv.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.tcc.sicv.base.Result
import com.tcc.sicv.base.ResultListenerFactory
import com.tcc.sicv.data.firebase.MaintenanceRepository
import com.tcc.sicv.data.preferences.PreferencesHelper
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.FlowState.Companion.loading
import com.tcc.sicv.data.model.MaintenanceVehicle

class MaintenanceViewModel(private val preferencesHelper: PreferencesHelper) : ViewModel() {
    private val flowState: MutableLiveData<FlowState<MutableList<MaintenanceVehicle>>> = MutableLiveData()
    private val maintenanceRepository: MaintenanceRepository
    private val resultListener: Result<MutableList<MaintenanceVehicle>>

    init {
        resultListener = ResultListenerFactory<MutableList<MaintenanceVehicle>>().create(flowState)
        maintenanceRepository = MaintenanceRepository()
        requestVehiclesInMaintenance()
    }

    fun requestVehiclesInMaintenance() {
        if (flowState.value?.isLoading == true) return

        flowState.postValue(loading())
        maintenanceRepository.getVehiclesInMaintenance(preferencesHelper.email ?:"", resultListener)
    }

    fun getFlowState(): LiveData<FlowState<MutableList<MaintenanceVehicle>>> = flowState
}
