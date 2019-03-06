package com.tcc.sicv.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tcc.sicv.R;
import com.tcc.sicv.data.model.Logs;
import com.tcc.sicv.ui.viewholder.LogsMaintenanceViewHolder;
import com.tcc.sicv.utils.OnItemClick;

import java.util.ArrayList;

public class LogsMaintenanceAdapter extends RecyclerView.Adapter<LogsMaintenanceViewHolder> {
    public ArrayList<Logs> logsList;
    private OnItemClick<Logs> listener;

    public LogsMaintenanceAdapter(ArrayList<Logs> logsList, OnItemClick<Logs> listener) {
        this.logsList = logsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LogsMaintenanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        return new LogsMaintenanceViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_log_maintenance, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull LogsMaintenanceViewHolder holder, int position) {
        holder.bind(logsList.get(position), listener);
    }

    @Override
    public int getItemCount() { return logsList.size(); }
}
