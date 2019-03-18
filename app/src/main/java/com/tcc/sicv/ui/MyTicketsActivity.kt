package com.tcc.sicv.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import com.tcc.sicv.R
import com.tcc.sicv.base.BaseActivity
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.Status.*
import com.tcc.sicv.data.model.Ticket
import com.tcc.sicv.data.preferences.PreferencesHelper
import com.tcc.sicv.presentation.MyTicketsViewModel
import com.tcc.sicv.ui.adapter.TicketListAdapter
import com.tcc.sicv.utils.Constants.RESULT_TAG
import com.tcc.sicv.utils.Constants.TICKET_KEY
import com.tcc.sicv.utils.startActivity
import com.tcc.sicv.utils.toJson
import kotlinx.android.synthetic.main.activity_my_tickets.*
import java.util.*

class MyTicketsActivity : BaseActivity() {
    private lateinit var mViewModel: MyTicketsViewModel
    private var adapter = makeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_tickets)
        mViewModel = MyTicketsViewModel(PreferencesHelper(application))
        setupViews()
        setupToolbar(R.id.main_toolbar, R.string.my_tickets_title, true)
        creatingObservers()
    }

    private fun setupViews() {
        ticketListRecylerView.apply{
            adapter = adapter
            addItemDecoration(DividerItemDecoration(context, requestedOrientation))
        }
    }

    private fun creatingObservers() {
        refreshMyTicketLayout.setOnRefreshListener {
            adapter.ticketList.clear()
            adapter.notifyDataSetChanged()
            mViewModel.requestMyTickets()
        }

        mViewModel.getFlowState().observe(this, Observer { handleWithMainFlow(it) })
    }

    private fun handleWithMainFlow(flowState: FlowState<MutableList<Ticket>>?) {
        when (flowState?.status) {
            LOADING -> refreshMyTicketLayout.isRefreshing = true
            ERROR -> {
                refreshMyTicketLayout.isRefreshing = false
                handleErrors(flowState.throwable)
            }
            SUCCESS -> {
                refreshMyTicketLayout!!.isRefreshing = false

                flowState.data?.let {
                    adapter.ticketList.addAll(it)
                    adapter.notifyDataSetChanged()
                }

            }
            else -> {}
        }
    }

    private fun makeAdapter() = TicketListAdapter(ArrayList()){
        startActivity<TicketDetailsActivity>(mapOf(TICKET_KEY to it.toJson(), RESULT_TAG to true))
    }

}
