package com.tcc.sicv.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tcc.sicv.R;
import com.tcc.sicv.data.model.Logs;
import com.tcc.sicv.utils.OnItemClick;

public class LogsMaintenanceViewHolder extends RecyclerView.ViewHolder {
    private TextView dateDetailsTextView;
    private TextView descriptionLogTextView;
    private TextView costLogTextView;

    public LogsMaintenanceViewHolder(@NonNull View itemView) {
        super(itemView);
        dateDetailsTextView = itemView.findViewById(R.id.dateDetailsTextView);
        descriptionLogTextView = itemView.findViewById(R.id.descriptionLogTextView);
        costLogTextView = itemView.findViewById(R.id.costLogTextView);
    }

    public void bind(final Logs item, final OnItemClick<Logs> listener) {
        dateDetailsTextView.setText(String.format(
                itemView.getContext().getString(R.string.date_log_format),
                item.getData()
        ));

        descriptionLogTextView.setText(String.format(
                itemView.getContext().getString(R.string.description_log_format),
                item.getDescricao()
        ));

        costLogTextView.setText(String.format(
                itemView.getContext().getString(R.string.cost_log_format),
                item.getGasto()
        ));
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
