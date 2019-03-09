package com.tcc.sicv.ui;

import android.os.Bundle;

import com.google.gson.Gson;
import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.model.Ticket;
import com.tcc.sicv.presentation.TicketDetailsViewModel;

import static com.tcc.sicv.utils.Constants.TICKET_KEY;

public class TicketDetailsActivity extends BaseActivity {
    private TicketDetailsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);
        if(getIntent() != null && getIntent().getExtras() != null){
            String ticketJson = getIntent().getExtras().getString(TICKET_KEY,"");
            mViewModel = new TicketDetailsViewModel(new Gson().fromJson(ticketJson, Ticket.class));
        }
    }
}
