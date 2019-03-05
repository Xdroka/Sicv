package com.tcc.sicv.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tcc.sicv.R;
import com.tcc.sicv.presentation.model.MaintenanceVehicle;
import com.tcc.sicv.ui.viewholder.MaintenanceViewHolder;
import com.tcc.sicv.utils.OnItemClick;

import java.util.ArrayList;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceViewHolder> {
    public ArrayList<MaintenanceVehicle> list;
    private OnItemClick<MaintenanceVehicle> listener;

    public MaintenanceAdapter(ArrayList<MaintenanceVehicle> list, OnItemClick<MaintenanceVehicle> listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MaintenanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        return new MaintenanceViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_maintenance, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MaintenanceViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
