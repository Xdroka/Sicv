package com.tcc.sicv.ui

import android.arch.lifecycle.Observer
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import com.tcc.sicv.R
import com.tcc.sicv.base.BaseActivity
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.Status
import com.tcc.sicv.data.model.User
import com.tcc.sicv.data.preferences.PreferencesHelper
import com.tcc.sicv.presentation.ProfileViewModel
import com.tcc.sicv.utils.OnAlertButtonClick
import com.tcc.sicv.utils.startActivity
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity() {
    private lateinit var mViewModel: ProfileViewModel

    private val positiveListener = object : OnAlertButtonClick {
        override val text: String
            get() = getString(R.string.positive_button_text)

        override fun onClickButton() {
            mViewModel.logout()
        }
    }

    private val negativeListener = object : OnAlertButtonClick {
        override val text: String
            get() = getString(R.string.negative_button_text)

        override fun onClickButton() {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupToolbar(R.id.main_toolbar, R.string.user_profile, true)
        mViewModel = ProfileViewModel(PreferencesHelper(application))
        creatingObservers()
    }

    private fun creatingObservers() {
        mViewModel.getFlowState().observe(this, Observer { handleWithMainFlow(it) })
        mViewModel.getLogoutState().observe(this, Observer { handleWithLogoutState(it) })
        logoutButton.setOnClickListener {
            createDialog(
                    getString(R.string.logout_message_confirm),
                    positiveListener,
                    negativeListener)
        }

        refreshProfileLayout.setOnRefreshListener { mViewModel.getUserProfile() }
    }

    private fun handleWithLogoutState(logoutState: FlowState<Unit>?) {
        if (logoutState != null) {
            when (logoutState.status) {
                Status.LOADING -> refreshProfileLayout.isRefreshing = true
                Status.SUCCESS -> {
                    refreshProfileLayout.isRefreshing = false
                    startActivity<LoginActivity>(
                            flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                    )
                }
                Status.ERROR -> {
                    refreshProfileLayout.isRefreshing = false
                    handleErrors(logoutState.throwable)
                }
                else -> {}
            }
        }
    }

    private fun handleWithMainFlow(flowState: FlowState<User>?) {
        when (flowState?.status) {
            Status.LOADING -> {
                refreshProfileLayout.isRefreshing = true
                logoutButton.isEnabled = false
            }
            Status.SUCCESS -> {
                refreshProfileLayout.isRefreshing = false
                handleWithSuccessMainFlow(flowState)
            }
            Status.ERROR -> {
                refreshProfileLayout.isRefreshing = false
                handleErrors(flowState.throwable)
            }
            else -> {}
        }
    }

    private fun handleWithSuccessMainFlow(flowState: FlowState<User>) {
        val userProfile = flowState.data ?: return
        logoutButton.isEnabled = true
        nameTv.text = userProfile.name
        cpfTv.text = userProfile.cpf
        emailTv.text = userProfile.email
        dateTv.text = userProfile.date
        telephoneTv.text = userProfile.tel
    }
}
