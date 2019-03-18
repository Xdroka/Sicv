package com.tcc.sicv.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tcc.sicv.R
import com.tcc.sicv.data.model.Logs
import com.tcc.sicv.ui.viewholder.LogsMaintenanceViewHolder

class LogsMaintenanceAdapter(
        var logsList: MutableList<Logs>,
        private val onItemClick: ((item: Logs) -> Unit)? = null
) : RecyclerView.Adapter<LogsMaintenanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): LogsMaintenanceViewHolder {
        return LogsMaintenanceViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_log_maintenance, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LogsMaintenanceViewHolder, position: Int) {
        holder.bind(logsList[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return logsList.size
    }
}
