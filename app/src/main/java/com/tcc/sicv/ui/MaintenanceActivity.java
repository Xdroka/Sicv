package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.MaintenanceViewModel;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.presentation.model.MaintenanceVehicle;
import com.tcc.sicv.ui.adapter.MaintenanceAdapter;
import com.tcc.sicv.utils.OnItemClick;

import java.util.ArrayList;

import static com.tcc.sicv.utils.Constants.MAINTENANCE_KEY;

public class MaintenanceActivity extends BaseActivity implements OnItemClick<MaintenanceVehicle> {
    private MaintenanceAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private MaintenanceViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        mViewModel = new MaintenanceViewModel(new PreferencesHelper(getApplication()));
        setupViews();
        creatingObservers();
        setupToolbar(R.id.maintenanceToolbar, R.string.maintenance, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maintenance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_my_vehicles:
                startActivity(new Intent(this, MyVehiclesActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {
        adapter = new MaintenanceAdapter(new ArrayList<MaintenanceVehicle>(), this);
        RecyclerView recyclerView = findViewById(R.id.maintenanceList);
        refreshLayout = findViewById(R.id.refreshMaintenanceLayout);
        recyclerView.setAdapter(adapter);
    }

    private void creatingObservers() {
        mViewModel.getFlowState().observe(this, new Observer<FlowState<ArrayList<MaintenanceVehicle>>>() {
            @Override
            public void onChanged(@Nullable FlowState<ArrayList<MaintenanceVehicle>> vehicleFlowState) {
                if (vehicleFlowState != null) handleWithMainFlow(vehicleFlowState);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.list.clear();
                adapter.notifyDataSetChanged();
                mViewModel.requestVehiclesInMaintenance();
            }
        });
    }

    private void handleWithMainFlow(FlowState<ArrayList<MaintenanceVehicle>> flowState) {
        switch (flowState.getStatus()) {
            case SUCCESS:
                refreshLayout.setRefreshing(false);
                if (flowState.hasData()) {
                    adapter.list.addAll(flowState.getData());
                    adapter.notifyDataSetChanged();
                }
                break;
            case ERROR:
                refreshLayout.setRefreshing(false);
                handleErrors(flowState.getThrowable());
                break;
            case LOADING:
                refreshLayout.setRefreshing(true);
                break;
        }
    }

    @Override
    public void onClick(MaintenanceVehicle item) {
        Intent intent = new Intent(this, DetailsMaintenanceActivity.class);
        intent.putExtra(MAINTENANCE_KEY, new Gson().toJson(item));
        startActivity(intent);
    }
}
