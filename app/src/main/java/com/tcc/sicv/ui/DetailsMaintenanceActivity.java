package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Logs;
import com.tcc.sicv.data.model.MaintenanceVehicle;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.DetailsMaintenanceViewModel;
import com.tcc.sicv.ui.adapter.LogsMaintenanceAdapter;

import java.util.ArrayList;

import static com.tcc.sicv.utils.Constants.MAINTENANCE_KEY;
import static com.tcc.sicv.utils.Constants.TICKET_KEY;

public class DetailsMaintenanceActivity extends BaseActivity {
    private LogsMaintenanceAdapter adapter;
    private DetailsMaintenanceViewModel mViewModel;
    private Button generateTicketButton;
    private SwipeRefreshLayout refreshLayout;
    private TextView totalCostTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_maintenance);
        MaintenanceVehicle maintenance = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            maintenance = new Gson().fromJson(getIntent().getExtras()
                                    .getString(MAINTENANCE_KEY, ""),
                            MaintenanceVehicle.class
                    );
        }
        mViewModel = new DetailsMaintenanceViewModel(
                new PreferencesHelper(getApplication()), maintenance
        );
        setupViews();
        creatingObservers();
    }

    private void setupViews() {
        setupToolbar(R.id.main_toolbar, R.string.details_maintenance, true);
        RecyclerView recyclerView = findViewById(R.id.logsMDetailsRecyclerView);
        adapter = new LogsMaintenanceAdapter(new ArrayList<Logs>(), null);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, getRequestedOrientation())
        );
        generateTicketButton = findViewById(R.id.generateTicketButton);
        generateTicketButton.setVisibility(View.GONE);
        refreshLayout = findViewById(R.id.refreshDetailsMaintenanceLayout);
        totalCostTextView = findViewById(R.id.totalCostMaintenanceTextView);
    }

    private void creatingObservers() {
        mViewModel.getFlowState().observe(this, new Observer<FlowState<ArrayList<Logs>>>() {
            @Override
            public void onChanged(@Nullable FlowState<ArrayList<Logs>> flowState) {
                if (flowState != null) handleWithMainFlow(flowState);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (adapter.logsList.size() > 0) {
                    adapter.logsList.clear();
                    adapter.notifyDataSetChanged();
                }
                mViewModel.requestLogsMaintenance();
            }
        });

        generateTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewModel.getTicket() == null) return;
                Intent intent = new Intent(
                        DetailsMaintenanceActivity.this,
                        TicketDetailsActivity.class
                );
                intent.putExtra(TICKET_KEY, new Gson().toJson(mViewModel.getTicket()));
                startActivity(intent);
            }
        });
    }

    private void handleWithMainFlow(FlowState<ArrayList<Logs>> flowState) {
        switch (flowState.getStatus()) {
            case LOADING:
                refreshLayout.setRefreshing(true);
                break;
            case ERROR:
                refreshLayout.setRefreshing(false);
                break;
            case SUCCESS:
                refreshLayout.setRefreshing(false);
                if (flowState.hasData()) {
                    adapter.logsList.addAll(flowState.getData());
                    adapter.notifyDataSetChanged();
                    String totalCost = mViewModel.getTotalCost();
                    totalCostTextView.setText(
                            String.format(getString(R.string.money_format), totalCost)
                    );
                    if(mViewModel.isVehicleFixed()){
                        generateTicketButton.setVisibility(View.VISIBLE);
                    }

                }
                break;
        }
    }

}
