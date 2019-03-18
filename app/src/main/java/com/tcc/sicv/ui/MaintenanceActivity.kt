package com.tcc.sicv.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.tcc.sicv.R
import com.tcc.sicv.base.BaseActivity
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.MaintenanceVehicle
import com.tcc.sicv.data.model.Status.*
import com.tcc.sicv.data.preferences.PreferencesHelper
import com.tcc.sicv.presentation.MaintenanceViewModel
import com.tcc.sicv.ui.adapter.MaintenanceAdapter
import com.tcc.sicv.utils.Constants.MAINTENANCE_KEY
import com.tcc.sicv.utils.startActivity
import com.tcc.sicv.utils.startActivityAndFinish
import com.tcc.sicv.utils.toJson
import kotlinx.android.synthetic.main.activity_maintenance.*

class MaintenanceActivity : BaseActivity() {
    private val adapter  = makeAdapter()
    private lateinit var mViewModel: MaintenanceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)
        mViewModel = MaintenanceViewModel(PreferencesHelper(application))
        setupViews()
        creatingObservers()
        setupToolbar(R.id.main_toolbar, R.string.maintenance, true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_maintenance, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_my_vehicles -> startActivityAndFinish<MyVehiclesActivity>()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViews() {
        maintenanceList.adapter = adapter
    }

    private fun creatingObservers() {
        mViewModel.getFlowState().observe(this, Observer { handleWithMainFlow(it) })

        refreshMaintenanceLayout!!.setOnRefreshListener {
            adapter.list.clear()
            adapter.notifyDataSetChanged()
            mViewModel.requestVehiclesInMaintenance()
        }
    }

    private fun handleWithMainFlow(flowState: FlowState<MutableList<MaintenanceVehicle>>?) {
        when (flowState?.status) {
            SUCCESS -> {
                refreshMaintenanceLayout!!.isRefreshing = false
                flowState.data?.let{vehicleList ->
                    adapter.list.addAll(vehicleList)
                    adapter.notifyDataSetChanged()
                    if (adapter.list.isEmpty()) {
                        createDialog(
                                getString(R.string.no_vehicle_found_message), null, null
                        )
                    }
                }
            }
            ERROR -> {
                refreshMaintenanceLayout.isRefreshing = false
                handleErrors(flowState.throwable)
            }
            LOADING -> refreshMaintenanceLayout.isRefreshing = true
            else -> {}
        }
    }

    private fun makeAdapter() = MaintenanceAdapter(mutableListOf()){item ->
        startActivity<DetailsMaintenanceActivity>(mapOf(MAINTENANCE_KEY to item.toJson()))
    }

}
