package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import com.tcc.sicv.R;
import java.util.ArrayList;
import com.tcc.sicv.base.BaseActivity;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.MyVehiclesViewModel;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.Vehicle;
import com.tcc.sicv.ui.adapter.VehiclesAdapter;

public class MyVehiclesActivity extends BaseActivity {
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
        adapter = new VehiclesAdapter(new ArrayList<Vehicle>());
        vehiclesRecyclerView.setAdapter(adapter);
        refreshLayout = findViewById(R.id.refresh_layout);
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

    private void handleWithMainFlow(FlowState<ArrayList<Vehicle>> flowState){
        switch (flowState.getStatus()){
            case LOADING:
                showLoadingDialog();
                break;
            case ERROR:
                hideLoadingDialog();
                break;
            case SUCCESS:
                hideLoadingDialog();
                if(flowState.getData() != null) {
                    adapter.listVehicles.addAll(flowState.getData());
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }


}
