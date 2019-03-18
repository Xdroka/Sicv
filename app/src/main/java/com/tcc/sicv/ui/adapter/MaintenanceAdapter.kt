package com.tcc.sicv.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.tcc.sicv.R
import com.tcc.sicv.data.model.MaintenanceVehicle
import com.tcc.sicv.ui.viewholder.MaintenanceViewHolder

class MaintenanceAdapter(
        var list: MutableList<MaintenanceVehicle>,
        private val onClick: ((item : MaintenanceVehicle) -> Unit)? = null
) : RecyclerView.Adapter<MaintenanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): MaintenanceViewHolder {
        return MaintenanceViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_maintenance, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        holder.bind(list[position], onClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
