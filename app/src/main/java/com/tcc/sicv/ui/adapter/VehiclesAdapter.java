package com.tcc.sicv.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.tcc.sicv.R;
import com.tcc.sicv.presentation.model.Vehicle;
import com.tcc.sicv.ui.viewholder.VehiclesViewHolder;
import java.util.List;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesViewHolder> {
    @SuppressWarnings("WeakerAccess")
    public List<Vehicle> listVehicles;

    public VehiclesAdapter(List<Vehicle> listVehicles) {
        this.listVehicles = listVehicles;
    }

    @NonNull
    @Override
    public VehiclesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new VehiclesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vehicles, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull VehiclesViewHolder holder, int position) {
        holder.bind(listVehicles.get(position));
    }

    @Override
    public int getItemCount() {
        return listVehicles.size();
    }
}
