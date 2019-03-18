package com.tcc.sicv.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tcc.sicv.R
import com.tcc.sicv.data.model.Vehicle
import com.tcc.sicv.ui.viewholder.VehiclesViewHolder

class VehiclesAdapter(
        var listVehicles: MutableList<Vehicle>,
        private val vehicleOption: String,
        private val listener: ((item: Vehicle) -> Unit )? = null
) : RecyclerView.Adapter<VehiclesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): VehiclesViewHolder {
        return VehiclesViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_vehicles, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VehiclesViewHolder, position: Int) {
        holder.bind(listVehicles[position], listener, vehicleOption)
    }

    override fun getItemCount(): Int {
        return listVehicles.size
    }
}
