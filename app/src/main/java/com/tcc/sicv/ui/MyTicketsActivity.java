package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Ticket;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.MyTicketsViewModel;
import com.tcc.sicv.ui.adapter.TicketListAdapter;
import com.tcc.sicv.utils.OnItemClick;

import java.util.ArrayList;

import static com.tcc.sicv.utils.Constants.RESULT_TAG;
import static com.tcc.sicv.utils.Constants.TICKET_KEY;

public class MyTicketsActivity extends BaseActivity implements OnItemClick<Ticket> {
    private MyTicketsViewModel mViewModel;
    private TicketListAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);
        mViewModel = new MyTicketsViewModel(new PreferencesHelper(getApplication()));
        setupViews();
        setupToolbar(R.id.main_toolbar, R.string.my_tickets_title, true);
        creatingObservers();
    }

    public void setupViews(){
        refreshLayout = findViewById(R.id.refreshMyTicketLayout);
        RecyclerView recyclerView = findViewById(R.id.ticketListRecylerView);
        adapter = new TicketListAdapter(new ArrayList<Ticket>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, getRequestedOrientation())
        );
    }

    public void creatingObservers(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.ticketList.clear();
                adapter.notifyDataSetChanged();
                mViewModel.requestMyTickets();
            }
        });

        mViewModel.getFlowState().observe(this, new Observer<FlowState<ArrayList<Ticket>>>() {
            @Override
            public void onChanged(@Nullable FlowState<ArrayList<Ticket>> flowState) {
                if(flowState != null) handleWithMainFlow(flowState);
            }
        });
    }

    private void handleWithMainFlow(FlowState<ArrayList<Ticket>> flowState) {
        switch (flowState.getStatus()){
            case LOADING:
                refreshLayout.setRefreshing(true);
                break;
            case ERROR:
                refreshLayout.setRefreshing(false);
                handleErrors(flowState.getThrowable());
                break;
            case SUCCESS:
                refreshLayout.setRefreshing(false);
                if(flowState.hasData()){
                    adapter.ticketList.addAll(flowState.getData());
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onClick(Ticket item) {
        Intent intent = new Intent(this, TicketDetailsActivity.class);
        intent.putExtra(TICKET_KEY, new Gson().toJson(item));
        intent.putExtra(RESULT_TAG, true);
        startActivity(intent);
    }
}
