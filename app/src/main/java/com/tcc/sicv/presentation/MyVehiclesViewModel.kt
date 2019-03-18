package com.tcc.sicv.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.tcc.sicv.base.Result
import com.tcc.sicv.base.ResultListenerFactory
import com.tcc.sicv.data.firebase.VehiclesRepository
import com.tcc.sicv.data.preferences.PreferencesHelper
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.FlowState.Companion.loading
import com.tcc.sicv.data.model.Vehicle

import com.tcc.sicv.data.model.Status.LOADING
import com.tcc.sicv.data.model.Status.NEUTRAL

class MyVehiclesViewModel(private val preferencesHelper: PreferencesHelper) : ViewModel() {
    private val flowState: MutableLiveData<FlowState<MutableList<Vehicle>>> = MutableLiveData()
    private val vehiclesRepository: VehiclesRepository = VehiclesRepository()
    private val resultListener: Result<MutableList<Vehicle>>

    init {
        resultListener = ResultListenerFactory<MutableList<Vehicle>>().create(flowState)
        flowState.postValue(FlowState(null, null, NEUTRAL))
        requestMyVehicles()
    }

    fun requestMyVehicles() {
        if (flowState.value != null && flowState.value!!.status == LOADING) return
        flowState.postValue(loading())
        vehiclesRepository.getMyVehicles(preferencesHelper.email ?:"", resultListener)
    }

    fun getFlowState(): LiveData<FlowState<MutableList<Vehicle>>> = flowState

}
