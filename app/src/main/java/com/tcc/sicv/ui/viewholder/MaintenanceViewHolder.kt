package com.tcc.sicv.ui.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.tcc.sicv.R
import com.tcc.sicv.data.model.MaintenanceVehicle
import com.tcc.sicv.utils.loadImageUrl
import kotlinx.android.synthetic.main.item_maintenance.view.*

class MaintenanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: MaintenanceVehicle, listener: ((item: MaintenanceVehicle) -> Unit)?) {
        itemView.apply{
            vehicleMaintenanceImageView.loadImageUrl(item.imagem)
            codeMaintenanceTextView.text = item.maintenanceCode
            modelMaintenanceTextView.text = item.modelo
            listener?.let { setOnClickListener { it(item) } }
        }
    }
}
