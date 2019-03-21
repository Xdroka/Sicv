package com.tcc.sicv.ui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import com.google.gson.Gson
import com.tcc.sicv.R
import com.tcc.sicv.base.BaseActivity
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.Logs
import com.tcc.sicv.data.model.MaintenanceVehicle
import com.tcc.sicv.data.model.Status.*
import com.tcc.sicv.data.preferences.PreferencesHelper
import com.tcc.sicv.presentation.DetailsMaintenanceViewModel
import com.tcc.sicv.ui.adapter.LogsMaintenanceAdapter
import com.tcc.sicv.utils.Constants.MAINTENANCE_KEY
import com.tcc.sicv.utils.Constants.TICKET_KEY
import com.tcc.sicv.utils.hide
import com.tcc.sicv.utils.show
import com.tcc.sicv.utils.startActivity
import com.tcc.sicv.utils.toJson
import kotlinx.android.synthetic.main.activity_details_maintenance.*
import java.util.*

class DetailsMaintenanceActivity : BaseActivity() {
    private var adapter: LogsMaintenanceAdapter = makeAdapter()
    private lateinit var mViewModel: DetailsMaintenanceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_maintenance)
        var maintenance: MaintenanceVehicle? = null
        intent?.extras?.apply {
            maintenance = Gson().fromJson<MaintenanceVehicle>(
                    getString(MAINTENANCE_KEY, ""),
                    MaintenanceVehicle::class.java
            )
        }
        mViewModel = DetailsMaintenanceViewModel(
                PreferencesHelper(application), maintenance
        )
        setupViews()
        creatingObservers()
    }

    private fun setupViews() {
        setupToolbar(R.id.main_toolbar, R.string.details_maintenance)
        logsMDetailsRecyclerView.adapter = adapter
        logsMDetailsRecyclerView.addItemDecoration(DividerItemDecoration(this, requestedOrientation))
        generateTicketButton.hide()
    }

    private fun creatingObservers() {
        mViewModel.getFlowState().observe(this, Observer { handleWithMainFlow(it)} )

        refreshDetailsMaintenanceLayout.setOnRefreshListener {
            adapter.logsList.clear()
            adapter.notifyDataSetChanged()
            mViewModel.requestLogsMaintenance()
        }

        generateTicketButton.setOnClickListener {
            mViewModel.ticket?.let {ticket ->
                startActivity<TicketDetailsActivity>(mapOf(TICKET_KEY to ticket.toJson()))
            }
        }

    }

    private fun handleWithMainFlow(flowState: FlowState<ArrayList<Logs>>?) {
        when (flowState?.status) {
            LOADING -> refreshDetailsMaintenanceLayout.isRefreshing = true
            ERROR -> refreshDetailsMaintenanceLayout.isRefreshing = false
            SUCCESS -> {
                refreshDetailsMaintenanceLayout.isRefreshing = false
                flowState.data?.let {logsList ->
                    adapter.logsList.addAll(logsList)
                    adapter.notifyDataSetChanged()
                    val totalCost = mViewModel.totalCost
                    totalCostMaintenanceTextView.text = String.format(
                            getString(R.string.money_format),
                            totalCost
                    )
                    if (mViewModel.isVehicleFixed == true) {
                        generateTicketButton.show()
                    }
                }
            }
            else -> { }
        }
    }

    private fun makeAdapter() = LogsMaintenanceAdapter(mutableListOf())

}
