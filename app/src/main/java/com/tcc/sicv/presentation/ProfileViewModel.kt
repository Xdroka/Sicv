package com.tcc.sicv.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import com.tcc.sicv.base.Result
import com.tcc.sicv.base.ResultListenerFactory
import com.tcc.sicv.data.firebase.AuthRepository
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.User
import com.tcc.sicv.data.preferences.PreferencesHelper

import com.tcc.sicv.data.model.Status.LOADING

class ProfileViewModel(private val preferencesHelper: PreferencesHelper) : ViewModel() {
    private val authRepository: AuthRepository = AuthRepository()
    private val flowState: MutableLiveData<FlowState<User>>
    private val logoutState: MutableLiveData<FlowState<Unit>> = MutableLiveData()
    private val mainResultListener: Result<User>
    private val logoutListener: Result<Unit>

    init {
        logoutListener = ResultListenerFactory<Unit>().create(logoutState)
        flowState = MutableLiveData()
        mainResultListener = ResultListenerFactory<User>().create(flowState)
        getUserProfile()
    }

    fun logout() {
        logoutState.postValue(FlowState(null, null, LOADING))
        preferencesHelper.logout()
        authRepository.logout(logoutListener)
    }

    fun getUserProfile() {
        flowState.postValue(FlowState<User>(null, null, LOADING))
        authRepository.getUserProfile(preferencesHelper.email ?:"", mainResultListener)
    }

    fun getFlowState(): LiveData<FlowState<User>> {
        return flowState
    }

    fun getLogoutState(): LiveData<FlowState<Unit>> {
        return logoutState
    }
}
