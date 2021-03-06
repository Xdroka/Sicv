package com.tcc.sicv.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcc.sicv.R;
import com.tcc.sicv.data.model.Vehicle;
import com.tcc.sicv.utils.ImageHelper;
import com.tcc.sicv.utils.OnItemClick;

import static com.tcc.sicv.utils.Constants.BUY_VEHICLE;
import static com.tcc.sicv.utils.Constants.MY_VEHICLES;

public class VehiclesViewHolder extends RecyclerView.ViewHolder {
    private ImageView vehicleImageView;
    private TextView typeModelTextView;
    private TextView vehicleOptionTextView;
    private TextView labelOptionTextView;
    private TextView labelModelTextView;

    public VehiclesViewHolder(@NonNull View itemView) {
        super(itemView);
        vehicleOptionTextView = itemView.findViewById(R.id.option_vehicle);
        typeModelTextView = itemView.findViewById(R.id.tv_type_model);
        vehicleImageView = itemView.findViewById(R.id.iv_photo);
        labelOptionTextView = itemView.findViewById(R.id.labelCode);
        labelModelTextView = itemView.findViewById(R.id.labelModel);
    }

    public void bind(final Vehicle item, final OnItemClick<Vehicle> listener, final String vehicleOption) {
        typeModelTextView.setText(item.getModelo());
        switch (vehicleOption) {
            case MY_VEHICLES:
                vehicleOptionTextView.setText(item.getCodigo());
                break;
            case BUY_VEHICLE:
                labelOptionTextView.setText("");
                labelModelTextView.setText("");
                vehicleOptionTextView.setText(item.getPreco());
                break;
        }

        ImageHelper.loadImageUrl(item.getImagem(), vehicleImageView);

        if (listener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(item);
                }
            });
        }
    }
}
