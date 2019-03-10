package com.tcc.sicv.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tcc.sicv.R;
import com.tcc.sicv.data.model.Ticket;
import com.tcc.sicv.utils.OnItemClick;

public class TicketListViewHolder extends RecyclerView.ViewHolder {
    private TextView typeTextView;
    private TextView dateTextView;
    private TextView costTextView;

    public TicketListViewHolder(@NonNull View itemView) {
        super(itemView);
        typeTextView = itemView.findViewById(R.id.typeTicketTextView);
        dateTextView = itemView.findViewById(R.id.dateItemTextView);
        costTextView = itemView.findViewById(R.id.costItemTicketTv);
    }

    public void bind(final Ticket item, final OnItemClick<Ticket> clickListener){
        costTextView.setText(item.getCustoTotal());
        typeTextView.setText(item.getTipo().toUpperCase());
        String data;
        String[] time = item.getTime().split(" ");
        data = time[0];
        dateTextView.setText(data);

        if(clickListener != null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(item);
                }
            });
        }
    }
}
