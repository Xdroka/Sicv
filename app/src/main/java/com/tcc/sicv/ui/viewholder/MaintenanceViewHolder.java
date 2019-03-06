package com.tcc.sicv.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcc.sicv.R;
import com.tcc.sicv.data.model.MaintenanceVehicle;
import com.tcc.sicv.utils.ImageHelper;
import com.tcc.sicv.utils.OnItemClick;

public class MaintenanceViewHolder extends RecyclerView.ViewHolder {
    private TextView codeTextView;
    private TextView modelTextView;
    private ImageView imageView;

    public MaintenanceViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.vehicleMaintenanceImageView);
        codeTextView = itemView.findViewById(R.id.codeMaintenanceTextView);
        modelTextView = itemView.findViewById(R.id.modelMaintenanceTextView);
    }

    public void bind(final MaintenanceVehicle item, final OnItemClick<MaintenanceVehicle> listener) {
        ImageHelper.loadImageUrl(item.getVehiclePhotoUrl(), imageView);
        codeTextView.setText(item.getMaintenanceCode());
        modelTextView.setText(item.getModelVehicle());
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
