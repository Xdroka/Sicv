package com.tcc.sicv.ui.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tcc.sicv.data.model.Ticket
import kotlinx.android.synthetic.main.item_ticket.view.*

class TicketListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Ticket, clickListener: ((item: Ticket) -> Unit)?) {
        itemView.apply{
            costItemTicketTv.text = item.custoTotal
            typeTicketTextView.text = item.tipo.toUpperCase()
            val data: String
            val time = item.time!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            data = time[0]
            dateItemTextView.text = data

            setOnClickListener { clickListener?.invoke(item) }
        }
    }
}
