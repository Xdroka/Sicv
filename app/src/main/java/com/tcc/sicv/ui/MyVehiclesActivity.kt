package com.tcc.sicv.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.tcc.sicv.R
import com.tcc.sicv.base.BaseActivity
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.Status.*
import com.tcc.sicv.data.model.Vehicle
import com.tcc.sicv.data.preferences.PreferencesHelper
import com.tcc.sicv.presentation.MyVehiclesViewModel
import com.tcc.sicv.ui.adapter.VehiclesAdapter
import com.tcc.sicv.utils.Constants.FROM_ACTIVITY
import com.tcc.sicv.utils.Constants.MY_VEHICLES
import com.tcc.sicv.utils.startActivity
import com.tcc.sicv.utils.toJson
import kotlinx.android.synthetic.main.activity_my_vehicles.*
import java.util.*

class MyVehiclesActivity : BaseActivity(){
    private var adapter = makeAdapter()
    private lateinit var mViewModel: MyVehiclesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vehicles)
        mViewModel = MyVehiclesViewModel(PreferencesHelper(application))
        setupViews()
        creatingObservers()
    }

    private fun setupViews() {
        list_vehicles.adapter = adapter
        setupToolbar(R.id.main_toolbar, R.string.my_vehicles, true)
    }

    private fun creatingObservers() {
        refresh_layout.setOnRefreshListener {
            adapter.listVehicles.clear()
            adapter.notifyDataSetChanged()
            mViewModel.requestMyVehicles()
        }

        mViewModel.getFlowState().observe(this, Observer { handleWithMainFlow(it) })
    }

    private fun handleWithMainFlow(flowState: FlowState<MutableList<Vehicle>>?) {
        when (flowState?.status) {
            LOADING -> refresh_layout.isRefreshing = true
            ERROR -> {
                refresh_layout.isRefreshing = false
                handleErrors(flowState.throwable)
            }
            SUCCESS -> {
                refresh_layout.isRefreshing = false
                flowState.data?.let{
                    adapter.listVehicles.addAll(it)
                    adapter.notifyDataSetChanged()
                    if (adapter.listVehicles.isEmpty()) {
                        createDialog(
                                getString(R.string.no_vehicle_found_message), null, null
                        )
                    }
                }
            }
            NEUTRAL -> { }
        }
    }

    private fun makeAdapter() = VehiclesAdapter(ArrayList(), MY_VEHICLES) { item ->
        startActivity<VehicleDetailsActivity>(
                mapOf(MY_VEHICLES to item.toJson(), FROM_ACTIVITY to MY_VEHICLES)
        )
    }
}
