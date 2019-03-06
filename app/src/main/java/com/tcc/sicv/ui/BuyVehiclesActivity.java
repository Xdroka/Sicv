package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Vehicle;
import com.tcc.sicv.presentation.BuyVehiclesViewModel;
import com.tcc.sicv.ui.adapter.VehiclesAdapter;
import com.tcc.sicv.utils.OnItemClick;

import java.util.ArrayList;

import static com.tcc.sicv.data.model.VehicleOption.PRICE;

public class BuyVehiclesActivity extends BaseActivity implements OnItemClick<Vehicle> {
    private VehiclesAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private BuyVehiclesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_vehicles);
        mViewModel = new BuyVehiclesViewModel();
        setupViews();
        creatingObservers();
    }

    private void setupViews() {
        RecyclerView vehiclesRecyclerView = findViewById(R.id.buyVehycles_list_vehicles);
        adapter = new VehiclesAdapter(new ArrayList<Vehicle>(), this, PRICE);
        vehiclesRecyclerView.setAdapter(adapter);
        refreshLayout = findViewById(R.id.buyVehycles_refreshLayout);
        setupToolbar(R.id.main_toolbar, R.string.purchase, true);
    }

    private void creatingObservers() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.listVehicles.clear();
                adapter.notifyDataSetChanged();
                mViewModel.requestMyVehicles();
            }
        });

        mViewModel.getFlowState().observe(this, new Observer<FlowState<ArrayList<Vehicle>>>() {
            @Override
            public void onChanged(@Nullable FlowState<ArrayList<Vehicle>> flowState) {
                if (flowState != null) {
                    handleWithMainFlow(flowState);
                }
            }
        });
    }

    private void handleWithMainFlow(FlowState<ArrayList<Vehicle>> flowState) {
        switch (flowState.getStatus()) {
            case LOADING:
                refreshLayout.setRefreshing(true);
                break;
            case ERROR:
                refreshLayout.setRefreshing(false);
                handleErrors(flowState.getThrowable());
                break;
            case SUCCESS:
                refreshLayout.setRefreshing(false);
                if (flowState.getData() != null) {
                    adapter.listVehicles.addAll(flowState.getData());
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onClick(Vehicle item) {
        startActivity(new Intent(this, VehicleDetailsActivity.class));
    }
}
