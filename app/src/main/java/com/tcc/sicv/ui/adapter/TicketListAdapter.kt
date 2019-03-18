package com.tcc.sicv.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.tcc.sicv.R
import com.tcc.sicv.data.model.Ticket
import com.tcc.sicv.ui.viewholder.TicketListViewHolder

import java.util.ArrayList

class TicketListAdapter(
        var ticketList: ArrayList<Ticket>,
        private val clickListener: ((item: Ticket) -> Unit)? = null
) : RecyclerView.Adapter<TicketListViewHolder>() {

    override fun onCreateViewHolder(root: ViewGroup, type: Int): TicketListViewHolder =
        TicketListViewHolder(
                LayoutInflater.from(root.context)
                        .inflate(R.layout.item_ticket, root, false)
        )


    override fun onBindViewHolder(holder: TicketListViewHolder, position: Int) {
        holder.bind(ticketList[position], clickListener)
    }

    override fun getItemCount(): Int = ticketList.size
}
