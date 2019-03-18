package com.tcc.sicv.presentation

import java.util.regex.Pattern

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

import com.tcc.sicv.base.Result
import com.tcc.sicv.base.ResultListenerFactory
import com.tcc.sicv.data.model.State

import android.arch.lifecycle.MutableLiveData

import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.firebase.AuthRepository
import com.tcc.sicv.data.preferences.PreferencesHelper

import com.tcc.sicv.data.model.State.EMPTY
import com.tcc.sicv.data.model.State.INVALID
import com.tcc.sicv.data.model.State.VALID
import com.tcc.sicv.data.model.Status.LOADING
import com.tcc.sicv.data.model.Status.NEUTRAL

class LoginViewModel(private val preferencesHelper: PreferencesHelper) : ViewModel() {
    private val authRepository: AuthRepository
    private val flowState: MutableLiveData<FlowState<String>> = MutableLiveData()
    private val emailState: MutableLiveData<State>
    private val passwordState: MutableLiveData<State>
    private val resultListener: Result<String>

    val user: String?
        get() = preferencesHelper.email

    init {
        flowState.value = FlowState<String>(null, null, NEUTRAL)
        resultListener = ResultListenerFactory<String>().create(flowState)
        authRepository = AuthRepository()
        emailState = MutableLiveData()
        passwordState = MutableLiveData()
    }

    fun saveUser(email: String) {
        preferencesHelper.saveUser(email)
    }

    fun signIn(email: String, password: String) {
        if (flowState.value != null && flowState.value!!.status == LOADING) return
        validateEmail(email)
        validatePassword(password)
        if (emailState.value != VALID || passwordState.value != VALID) return

        flowState.postValue(FlowState<String>(null, null, LOADING))
        authRepository.signIn(email, password, resultListener)
    }

    private fun validateEmail(email: String) {
        val regex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$"
        val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        if (pattern.matcher(email).find()) {
            emailState.setValue(VALID)
        } else {
            if (email.isEmpty())
                emailState.setValue(EMPTY)
            else
                emailState.setValue(INVALID)
        }
    }

    private fun validatePassword(password: String) {
        if (password.length >= 6)
            passwordState.setValue(VALID)
        else {
            if (password.isEmpty())
                passwordState.setValue(EMPTY)
            else
                passwordState.setValue(INVALID)
        }
    }

    fun getFlowState(): LiveData<FlowState<String>> {
        return flowState
    }

    fun getEmailState(): LiveData<State> {
        return emailState
    }

    fun getPasswordState(): LiveData<State> {
        return passwordState
    }

}
