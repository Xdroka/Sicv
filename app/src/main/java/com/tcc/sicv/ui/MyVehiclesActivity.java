package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.MyVehiclesViewModel;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Vehicle;
import com.tcc.sicv.ui.adapter.VehiclesAdapter;
import com.tcc.sicv.utils.OnItemClick;

import java.util.ArrayList;

import static com.tcc.sicv.data.model.VehicleOption.CODE;

public class MyVehiclesActivity extends BaseActivity implements OnItemClick<Vehicle> {
    private VehiclesAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private MyVehiclesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicles);
        mViewModel = new MyVehiclesViewModel(new PreferencesHelper(getApplication()));
        setupViews();
        creatingObservers();
    }

    private void setupViews() {
        RecyclerView vehiclesRecyclerView = findViewById(R.id.list_vehicles);
        adapter = new VehiclesAdapter(new ArrayList<Vehicle>(), this, CODE);
        vehiclesRecyclerView.setAdapter(adapter);
        refreshLayout = findViewById(R.id.refresh_layout);
        setupToolbar(R.id.main_toolbar, R.string.my_vehicles, true);
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
                    if(adapter.listVehicles.size() == 0){
                        createConfirmLogoutDialog(
                                getString(R.string.no_vehicle_found_message),
                                null,
                                null
                        );
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(Vehicle item) {
        startActivity(new Intent(this, VehicleDetailsActivity.class));
    }
}
