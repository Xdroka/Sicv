package com.tcc.sicv.ui

import android.arch.lifecycle.Observer
import android.content.DialogInterface
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import com.tcc.sicv.R
import com.tcc.sicv.base.BaseActivity
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.State
import com.tcc.sicv.data.model.Status
import com.tcc.sicv.data.preferences.PreferencesHelper
import com.tcc.sicv.presentation.SignUpViewModel
import com.tcc.sicv.utils.startActivity
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    private lateinit var mViewModel: SignUpViewModel

    private val dismissListener = DialogInterface.OnDismissListener {
        startActivity<HomeActivity>(flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mViewModel = SignUpViewModel(PreferencesHelper(application))
        creatingObservers()
    }

    private fun creatingObservers() {
        mViewModel.getFlowState().observe(this, Observer { flowState ->
            if (flowState != null) {
                handleWithMainFlow(flowState)
            }
        })
        signUpButton.setOnClickListener {
            try {
                val email = emailEt.text.toString()
                val password = passwordEt.text.toString()
                val name = nameEt.text.toString()
                val cpf = cpfEt.text.toString()
                val tel = telephoneEt.text.toString()
                val date = dateEt.text.toString()
                val confirmPass = confirmPasswordEt.text.toString()
                mViewModel.processAllValidation(email, password, name, cpf, tel, date, confirmPass)
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
        mViewModel.getEmailState().observe(this, Observer { handleWithEmailState(it) })
        mViewModel.getPasswordState().observe(this, Observer { handleWithPasswordState(it) })
        mViewModel.getNameState().observe(this, Observer { handleWithNameState(it) })
        mViewModel.getCpfState().observe(this, Observer { handleWithCpfState(it) })
        mViewModel.getTelState().observe(this, Observer { handleWithTelState(it) })
        mViewModel.getDateState().observe(this, Observer { handleWithDateState(it) })
        mViewModel.getConfirmPassState().observe(this, Observer {
            handleWithConfirmPassState(it)
        })
    }

    private fun handleWithPasswordState(state: State?) {
        when (state) {
            State.EMPTY -> passwordEt.error = getString(R.string.error_empty_password)
            State.VALID -> passwordEt.error = null
            State.INVALID -> passwordEt.error = getString(R.string.error_invalid_password)
            else -> {}
        }
    }

    private fun handleWithDateState(state: State?) {
        when (state) {
            State.EMPTY -> dateEt.error = getString(R.string.empty_date)
            State.VALID -> dateEt.error = null
            State.INVALID -> dateEt.error = getString(R.string.invalid_date)
            State.MIN_AGE -> dateEt.error = getString(R.string.invalid_age_date)
        }
    }

    private fun handleWithTelState(state: State?) {
        when (state) {
            State.EMPTY -> telephoneEt.error = getString(R.string.empty_telephone)
            State.VALID -> telephoneEt.error = null
            State.INVALID -> telephoneEt.error = getString(R.string.invalid_telephone)
            else -> {}
        }
    }

    private fun handleWithCpfState(state: State?) {
        when (state) {
            State.EMPTY -> cpfEt.error = getString(R.string.empty_cpf)
            State.VALID -> cpfEt.error = null
            State.INVALID -> cpfEt.error = getString(R.string.invalid_cpf)
            else -> {}
        }
    }

    private fun handleWithNameState(state: State?) {
        when (state) {
            State.EMPTY -> nameEt.error = getString(R.string.empty_name)
            State.VALID -> nameEt.error = null
            else -> {}
        }
    }

    private fun handleWithConfirmPassState(state: State?) {
        when (state) {
            State.VALID -> confirmPasswordEt.error = null
            State.INVALID -> confirmPasswordEt.error = getString(R.string.error_confirm_password)
            else -> {}
        }
    }

    private fun handleWithEmailState(state: State?) {
        when (state) {
            State.EMPTY -> emailEt.error = getString(R.string.error_empty_email)
            State.VALID -> emailEt.error = null
            State.INVALID -> emailEt.error = getString(R.string.error_invalid_email)
            else -> {}
        }
    }

    private fun handleWithMainFlow(flowState: FlowState<String>?) {
        when (flowState?.status) {
            Status.LOADING -> showLoadingDialog()
            Status.SUCCESS -> {
                hideLoadingDialog()
                val email = flowState.data ?: return
                mViewModel.saveUser(email)
                createConfirmAndExitDialog(getString(R.string.successfulSignUp), dismissListener)
            }
            Status.ERROR -> {
                hideLoadingDialog()
                handleErrors(flowState.throwable)
            }
            else -> {}
        }
    }
}