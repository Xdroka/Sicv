package com.tcc.sicv.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Patterns

import com.tcc.sicv.base.Result
import com.tcc.sicv.base.ResultListenerFactory
import com.tcc.sicv.data.firebase.AuthRepository
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.State
import com.tcc.sicv.data.model.Status
import com.tcc.sicv.data.model.User
import com.tcc.sicv.data.preferences.PreferencesHelper

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import com.tcc.sicv.data.model.State.EMPTY
import com.tcc.sicv.data.model.State.INVALID
import com.tcc.sicv.data.model.State.MIN_AGE
import com.tcc.sicv.data.model.State.VALID
import com.tcc.sicv.utils.Constants.DATE_FORMAT
import com.tcc.sicv.utils.Constants.DATE_MIN_LENGHT
import com.tcc.sicv.utils.Constants.USER_MIN_AGE

class SignUpViewModel(private val preferencesHelper: PreferencesHelper) : ViewModel() {
    private val authRepository: AuthRepository
    private val flowState: MutableLiveData<FlowState<String>>
    private val nameState: MutableLiveData<State>
    private val cpfState: MutableLiveData<State>
    private val telState: MutableLiveData<State>
    private val dateState: MutableLiveData<State>
    private val emailState: MutableLiveData<State>
    private val passwordState: MutableLiveData<State>
    private val confirmPassState: MutableLiveData<State>
    private val authListener: Result<String>

    init {
        authRepository = AuthRepository()
        flowState = MutableLiveData()
        authListener = ResultListenerFactory<String>().create(flowState)
        nameState = MutableLiveData()
        cpfState = MutableLiveData()
        telState = MutableLiveData()
        dateState = MutableLiveData()
        emailState = MutableLiveData()
        passwordState = MutableLiveData()
        confirmPassState = MutableLiveData()
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

    private fun isEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun saveUser(email: String) {
        preferencesHelper.saveUser(email)
    }

    private fun processEmailData(email: String): Boolean {
        if (email.isEmpty()) {
            emailState.postValue(EMPTY)
            return false
        } else if (isEmail(email)) {
            emailState.postValue(VALID)
            return true
        } else {
            emailState.postValue(INVALID)
            return false
        }
    }

    private fun processNameData(name: String): Boolean {
        if (name.isEmpty()) {
            nameState.postValue(EMPTY)
            return false
        } else {
            nameState.postValue(VALID)
            return true
        }
    }

    private fun processPasswordData(password: String): Boolean {
        val PASSWORD_MIN_LENGHT = 6
        if (password.isEmpty()) {
            passwordState.postValue(EMPTY)
            return false
        } else if (password.length < PASSWORD_MIN_LENGHT) {
            passwordState.postValue(INVALID)
            return false
        } else {
            passwordState.postValue(VALID)
            return true
        }
    }

    private fun processConfirmPasswordData(confirmPassword: String, password: String): Boolean {
        if (confirmPassword != password) {
            confirmPassState.postValue(INVALID)
            return false
        } else {
            confirmPassState.postValue(VALID)
            return true
        }
    }

    private fun processTelData(telephone: String): Boolean {
        val TELEPHONE_MIN_LENGHT = 14
        if (telephone.isEmpty()) {
            telState.postValue(EMPTY)
            return false
        } else if (telephone.length < TELEPHONE_MIN_LENGHT) {
            telState.postValue(INVALID)
            return false
        } else {
            telState.postValue(VALID)
            return true
        }
    }

    private fun processCpfData(cpf: String): Boolean {
        val CPF_MIN_LENGHT = 14
        if (cpf.isEmpty()) {
            cpfState.postValue(EMPTY)
            return false
        } else if (cpf.length != CPF_MIN_LENGHT) {
            cpfState.postValue(INVALID)
            return false
        } else {
            cpfState.postValue(VALID)
            return true
        }
    }

    private fun getAge(year: Int, month: Int, day: Int): Int {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob.set(year, month - 1, day)
        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }

    private fun processDateData(date: String): Boolean {
        var userAge = 0
        if (date.length == DATE_MIN_LENGHT && isDateValid(date)) {
            val day = Integer.parseInt(date.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0])
            val month = Integer.parseInt(date.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1])
            val year = Integer.parseInt(date.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2])
            userAge = getAge(year, month, day)
        }
        if (date.isEmpty()) {
            dateState.postValue(EMPTY)
            return false
        } else if (date.length != DATE_MIN_LENGHT || !isDateValid(date)) {
            dateState.postValue(INVALID)
            return false
        } else if (userAge < USER_MIN_AGE) {
            dateState.postValue(MIN_AGE)
            return false
        } else {
            dateState.postValue(VALID)
            return true
        }
    }

    fun processAllValidation(
            email: String,
            password: String,
            name: String,
            cpf: String,
            tel: String,
            date: String,
            confirmPassword: String
    ) {
        val validEmail = processEmailData(email)
        val validPassword = processPasswordData(password)
        val validName = processNameData(name)
        val validCpf = processCpfData(cpf)
        val validTelephone = processTelData(tel)
        val validDate = processDateData(date)
        val validConfirmPassword = processConfirmPasswordData(confirmPassword, password)

        if (validEmail && validName && validPassword && validConfirmPassword &&
                validTelephone && validCpf && validDate) {
            flowState.postValue(FlowState<String>(null, null, Status.LOADING))

            authRepository.checkAccountExistAndCreateUser(
                    User(
                            email,
                            password,
                            name,
                            cpf,
                            tel,
                            date
                    ),
                    authListener
            )
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

    fun getNameState(): LiveData<State> {
        return nameState
    }

    fun getTelState(): LiveData<State> {
        return telState
    }

    fun getCpfState(): LiveData<State> {
        return cpfState
    }

    fun getDateState(): LiveData<State> {
        return dateState
    }

    fun getConfirmPassState(): LiveData<State> {
        return confirmPassState
    }
}
