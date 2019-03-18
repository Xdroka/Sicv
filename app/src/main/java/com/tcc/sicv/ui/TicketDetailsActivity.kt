package com.tcc.sicv.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.tcc.sicv.R
import com.tcc.sicv.base.BaseActivity
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.Status
import com.tcc.sicv.data.preferences.PreferencesHelper
import com.tcc.sicv.presentation.TicketDetailsViewModel
import com.tcc.sicv.utils.Constants.RESULT_TAG
import com.tcc.sicv.utils.Constants.TICKET_KEY
import com.tcc.sicv.utils.show
import kotlinx.android.synthetic.main.activity_ticket_details.*

class TicketDetailsActivity : BaseActivity() {
    private lateinit var mViewModel: TicketDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_details)
        if (intent != null && intent.extras != null) {
            val ticketJson = intent.extras!!.getString(TICKET_KEY, "")
            val loadedTicket = intent.extras!!.getBoolean(RESULT_TAG)
            mViewModel = TicketDetailsViewModel(
                    ticketJson,
                    PreferencesHelper(application),
                    loadedTicket
            )
        }
        setupViews()
        creatingObservers()
    }

    private fun setupViews() {
        setupToolbar(R.id.main_toolbar, R.string.ticket_title, true)
    }

    private fun creatingObservers() {
        mViewModel.getFlowState().observe(this, Observer { handleWithMainFlow(it) })
    }

    private fun handleWithMainFlow(flowState: FlowState<Boolean>?) {
        when (flowState?.status) {
            Status.LOADING -> showLoadingDialog()
            Status.ERROR -> {
                hideLoadingDialog()
                handleErrors(flowState.throwable)
                finish()
            }
            Status.SUCCESS -> {
                hideLoadingDialog()
                flowState.data?.let { handleWithSuccessFlow() }
            }
            else -> { }
        }
    }

    private fun handleWithSuccessFlow() =
        mViewModel.ticket?.apply {
            dateBuyTextView.show()
            dateBuyTextView.text = String.format(getString(R.string.date_buy_type), dataAgendada)
            timeTextView.text = String.format(getString(R.string.date_genereted), time)
            codeTicketTextView.text = String.format(getString(R.string.code_format), ticketId)
            costLabelTextView.text = String.format(getString(R.string.money_format), custoTotal)
            typeLabelTextView.text = String.format(getString(R.string.type_ticket), tipo.toUpperCase())
            val vehicleCodeFormat = getString(R.string.vehicle_code) + " " + codigoVeiculo
            vehicleCodeTicketTv.text = vehicleCodeFormat
        }

}
