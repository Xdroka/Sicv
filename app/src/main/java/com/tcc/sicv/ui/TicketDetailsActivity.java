package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Ticket;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.TicketDetailsViewModel;

import static com.tcc.sicv.utils.Constants.TICKET_KEY;

public class TicketDetailsActivity extends BaseActivity {
    private TextView timeTextView;
    private TextView codeTicketTextView;
    private TextView totalCostTextView;
    private TextView typeTicketTextView;
    private TextView vehicleCodeTextView;
    private TicketDetailsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);
        if(getIntent() != null && getIntent().getExtras() != null){
            String ticketJson = getIntent().getExtras().getString(TICKET_KEY,"");
            mViewModel = new TicketDetailsViewModel(
                    new Gson().fromJson(ticketJson, Ticket.class),
                    new PreferencesHelper(getApplication())
            );
        }
        setupViews();
        creatingObservers();
    }

    private void setupViews(){
        setupToolbar(R.id.main_toolbar, R.string.ticket_title, true);
        timeTextView = findViewById(R.id.timeTextView);
        codeTicketTextView = findViewById(R.id.codeTicketTextView);
        totalCostTextView = findViewById(R.id.costLabelTextView);
        typeTicketTextView = findViewById(R.id.typeLabelTextView);
        vehicleCodeTextView = findViewById(R.id.vehicleCodeTicketTv);
    }

    private void creatingObservers(){
        mViewModel.getFlowState().observe(this, new Observer<FlowState<Boolean>>() {
            @Override
            public void onChanged(@Nullable FlowState<Boolean> flowState) {
                if(flowState != null ) handleWithMainFlow(flowState);
            }
        });
    }

    private void handleWithMainFlow(FlowState<Boolean> flowState) {
        switch (flowState.getStatus()){
            case LOADING:
                showLoadingDialog();
                break;
            case ERROR:
                hideLoadingDialog();
                handleErrors(flowState.getThrowable());
                finish();
                break;
            case SUCCESS:
                hideLoadingDialog();
                if(flowState.hasData()) handleWithSuccessFlow();
                break;
        }
    }

    private void handleWithSuccessFlow() {
        Ticket ticket = mViewModel.getTicket();
        timeTextView.setText(ticket.getTime());
        codeTicketTextView.setText(String.format(getString(R.string.code_format), ticket.getTicketId()));
        totalCostTextView.setText(
                String.format(getString(R.string.money_format), ticket.getCustoTotal())
        );
        typeTicketTextView.setText(
                String.format(getString(R.string.type_ticket), ticket.getTipo())
        );
        String vehicleCodeFormat = getString(R.string.vehicle_code) + " " + ticket.getCodigoVeiculo();
        vehicleCodeTextView.setText(vehicleCodeFormat);
    }
}
