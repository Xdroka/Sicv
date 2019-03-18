package com.tcc.sicv.ui.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.tcc.sicv.R
import com.tcc.sicv.data.model.Logs
import kotlinx.android.synthetic.main.item_log_maintenance.view.*

class LogsMaintenanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Logs, listener: ((item: Logs) -> Unit)?) {
        itemView.apply {
            dateDetailsTextView.text = String.format(
                    itemView.context.getString(R.string.date_log_format),
                    item.data
            )

            descriptionLogTextView.text = String.format(
                    itemView.context.getString(R.string.description_log_format),
                    item.descricao
            )

            costLogTextView.text = String.format(
                    itemView.context.getString(R.string.cost_log_format),
                    item.gasto
            )
            listener?.let { onClick ->
                setOnClickListener { onClick(item) }
            }
        }
    }
}
