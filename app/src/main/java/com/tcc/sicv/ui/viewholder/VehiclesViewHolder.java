package com.tcc.sicv.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcc.sicv.utils.ImageHelper;
import com.tcc.sicv.R;
import com.tcc.sicv.presentation.model.Vehicle;
import com.tcc.sicv.utils.OnItemClick;

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

    public void bind(final Vehicle item, final OnItemClick<Vehicle> listener){
        typeModelTextView.setText(item.getModelo());
        vehicleCodTextView.setText(
                String.format(itemView.getContext().getString(R.string.code_format),
                        item.getCodigo())
        );
        ImageHelper.loadImageUrl(item.getImagem(), vehicleImageView);

        if(listener != null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(item);
                }
            });
        }
    }
}
