package com.tcc.sicv.ui.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tcc.sicv.data.model.Vehicle
import com.tcc.sicv.utils.Constants.BUY_VEHICLE
import com.tcc.sicv.utils.Constants.MY_VEHICLES
import com.tcc.sicv.utils.loadImageUrl
import kotlinx.android.synthetic.main.item_vehicles.view.*

class VehiclesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Vehicle, listener: ((item: Vehicle) -> Unit)?, vehicleOption: String) {
        itemView.apply{
            tv_type_model.text = item.modelo
            when (vehicleOption) {
                MY_VEHICLES -> option_vehicle.text = item.codigo
                BUY_VEHICLE -> {
                    labelCode.text = ""
                    labelModel.text = ""
                    option_vehicle.text = item.preco
                }
            }
            iv_photo.loadImageUrl(item.imagem)
            listener?.let { onClick -> itemView.setOnClickListener { onClick(item) } }
        }
    }
}
