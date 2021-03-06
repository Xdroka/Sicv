package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Ticket;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.TicketDetailsViewModel;

import static com.tcc.sicv.utils.Constants.RESULT_TAG;
import static com.tcc.sicv.utils.Constants.TICKET_KEY;

public class TicketDetailsActivity extends BaseActivity {
    private TextView timeTextView;
    private TextView codeTicketTextView;
    private TextView totalCostTextView;
    private TextView typeTicketTextView;
    private TextView vehicleCodeTextView;
    private TicketDetailsViewModel mViewModel;
    private TextView dateSelectedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);
        if(getIntent() != null && getIntent().getExtras() != null){
            String ticketJson = getIntent().getExtras().getString(TICKET_KEY,"");
            Boolean loadedTicket = getIntent().getExtras().getBoolean(RESULT_TAG);
            mViewModel = new TicketDetailsViewModel(
                    ticketJson,
                    new PreferencesHelper(getApplication()),
                    loadedTicket
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
        dateSelectedTextView = findViewById(R.id.dateBuyTextView);
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
        if(ticket.getDataAgendada() != null){
            dateSelectedTextView.setVisibility(View.VISIBLE);
            dateSelectedTextView.setText(
                    String.format(getString(R.string.date_buy_type), ticket.getDataAgendada())
            );
        }
        timeTextView.setText(String.format(getString(R.string.date_genereted),ticket.getTime()));
        codeTicketTextView.setText(String.format(getString(R.string.code_format), ticket.getTicketId()));
        totalCostTextView.setText(
                String.format(getString(R.string.money_format), ticket.getCustoTotal())
        );
        typeTicketTextView.setText(
                String.format(getString(R.string.type_ticket), ticket.getTipo().toUpperCase())
        );
        String vehicleCodeFormat = getString(R.string.vehicle_code) + " " + ticket.getCodigoVeiculo();
        vehicleCodeTextView.setText(vehicleCodeFormat);
    }
}
