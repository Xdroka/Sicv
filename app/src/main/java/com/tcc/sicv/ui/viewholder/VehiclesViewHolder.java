package com.tcc.sicv.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcc.sicv.ImageHelper;
import com.tcc.sicv.R;
import com.tcc.sicv.presentation.model.Vehicle;

public class VehiclesViewHolder extends RecyclerView.ViewHolder {
    private ImageView vehicleImageView;
    private TextView typeModelTextView;
    private TextView vehicleCodTextView;

    public VehiclesViewHolder(@NonNull View itemView) {
        super(itemView);
        vehicleCodTextView = itemView.findViewById(R.id.cod_vehicle);
        typeModelTextView = itemView.findViewById(R.id.tv_type_model);
        vehicleImageView = itemView.findViewById(R.id.iv_photo);
    }

    public void bind(Vehicle item){
        typeModelTextView.setText(item.getModelo());
        vehicleCodTextView.setText(item.getCodigo());
        ImageHelper.loadImageUrl(item.getImagem(), vehicleImageView);
    }
}
