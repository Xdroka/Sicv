package com.tcc.sicv.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.tcc.sicv.R;
import com.tcc.sicv.data.model.Vehicle;
import com.tcc.sicv.ui.viewholder.VehiclesViewHolder;
import com.tcc.sicv.utils.OnItemClick;

import java.util.List;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesViewHolder> {
    public List<Vehicle> listVehicles;
    private String vehicleOption;
    private OnItemClick<Vehicle> listener;

    public VehiclesAdapter(List<Vehicle> listVehicles, OnItemClick<Vehicle> listener,
                           String vehicleOption) {
        this.listVehicles = listVehicles;
        this.listener = listener;
        this.vehicleOption = vehicleOption;
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
        holder.bind(listVehicles.get(position), listener, vehicleOption);
    }

    @Override
    public int getItemCount() {
        return listVehicles.size();
    }
}
