package com.tcc.sicv.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.tcc.sicv.R
import com.tcc.sicv.base.BaseActivity
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.Status.*
import com.tcc.sicv.data.model.Vehicle
import com.tcc.sicv.presentation.BuyVehiclesViewModel
import com.tcc.sicv.ui.adapter.VehiclesAdapter
import com.tcc.sicv.utils.Constants.BUY_VEHICLE
import com.tcc.sicv.utils.Constants.FROM_ACTIVITY
import com.tcc.sicv.utils.startActivity
import com.tcc.sicv.utils.toJson
import kotlinx.android.synthetic.main.activity_buy_vehicles.*
import java.util.*

class BuyVehiclesActivity : BaseActivity(){
    private var adapter: VehiclesAdapter = makeAdapter()
    private lateinit var mViewModel: BuyVehiclesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_vehicles)
        mViewModel = BuyVehiclesViewModel()
        setupViews()
        creatingObservers()
    }

    private fun setupViews() {
        val vehiclesRecyclerView = findViewById<RecyclerView>(R.id.buyVehycles_list_vehicles)
        vehiclesRecyclerView.adapter = adapter
        setupToolbar(R.id.main_toolbar, R.string.purchase)
    }

    private fun creatingObservers() {
        buyVehycles_refreshLayout?.setOnRefreshListener {
            adapter.listVehicles.clear()
            adapter.notifyDataSetChanged()
            mViewModel.requestMyVehicles()
        }

        mViewModel.getFlowState().observe(this, Observer { handleWithMainFlow(it) })
    }

    private fun handleWithMainFlow(flowState: FlowState<ArrayList<Vehicle>>?) {
        when (flowState?.status) {
            LOADING -> buyVehycles_refreshLayout?.isRefreshing = true
            ERROR -> {
                buyVehycles_refreshLayout?.isRefreshing = false
                handleErrors(flowState.throwable)
            }
            SUCCESS -> {
                buyVehycles_refreshLayout?.isRefreshing = false
                flowState.data?.let { listVehicle ->
                    adapter.listVehicles.addAll(listVehicle)
                    adapter.notifyDataSetChanged()
                }
            }
            else -> { }
        }
    }

    private fun makeAdapter() = VehiclesAdapter(mutableListOf(), BUY_VEHICLE){item ->
        startActivity<VehicleDetailsActivity>(mapOf(
                BUY_VEHICLE to item.toJson(), FROM_ACTIVITY to BUY_VEHICLE
        ))
    }

}
