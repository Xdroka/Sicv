package com.tcc.sicv.ui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import com.tcc.sicv.R
import com.tcc.sicv.base.BaseActivity
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.State
import com.tcc.sicv.data.model.State.*
import com.tcc.sicv.data.model.Status.*
import com.tcc.sicv.data.preferences.PreferencesHelper
import com.tcc.sicv.presentation.LoginViewModel
import com.tcc.sicv.utils.startActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mViewModel = LoginViewModel(PreferencesHelper(application))
        creatingObservers()
    }


    private fun creatingObservers() {
        mViewModel.user?.let { callHomeActivity() }
        mViewModel.getFlowState().observe(this, Observer { handleWithMainFlow(it) })
        signButton.setOnClickListener { sigInFlow() }
        registerButton.setOnClickListener { startActivity(Intent(this@LoginActivity, SignUpActivity::class.java)) }
        mViewModel.getEmailState().observe(this, Observer { handleWithEmailState(it) })
        mViewModel.getPasswordState().observe(this, Observer { handleWithPasswordState(it) })
    }

    private fun handleWithPasswordState(state: State?) {
        when (state) {
            EMPTY -> passwordEt!!.error = getString(R.string.error_empty_password)
            VALID -> passwordEt!!.error = null
            INVALID -> passwordEt!!.error = getString(R.string.error_invalid_password)
            else -> {}
        }
    }

    private fun handleWithEmailState(state: State?) {
        when (state) {
            EMPTY -> emailEt.error = getString(R.string.error_empty_email)
            VALID -> emailEt.error = null
            INVALID -> emailEt.error = getString(R.string.error_invalid_email)
            else -> {}
        }
    }

    private fun sigInFlow() {
        try {
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()
            mViewModel.signIn(email, password)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    private fun handleWithMainFlow(userFlowState: FlowState<String>?) {
        when (userFlowState?.status) {
            ERROR -> {
                hideLoadingDialog()
                handleErrors(userFlowState.throwable)
            }
            NEUTRAL -> { }
            LOADING -> showLoadingDialog()
            SUCCESS -> {
                hideLoadingDialog()
                val email = userFlowState.data ?: return
                mViewModel.saveUser(email)
                callHomeActivity()
            }
        }
    }

    private fun callHomeActivity() =
        startActivity<HomeActivity>(flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)

}
