package com.tcc.sicv.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tcc.sicv.R;
import com.tcc.sicv.data.model.Ticket;
import com.tcc.sicv.ui.viewholder.TicketListViewHolder;
import com.tcc.sicv.utils.OnItemClick;

import java.util.ArrayList;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListViewHolder> {
    public ArrayList<Ticket> ticketList;
    private OnItemClick<Ticket> clickListener;

    public TicketListAdapter(ArrayList<Ticket> ticketList, OnItemClick<Ticket> clickListener) {
        this.ticketList = ticketList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public TicketListViewHolder onCreateViewHolder(@NonNull ViewGroup root, int type) {
        return new TicketListViewHolder(
                LayoutInflater.from(root.getContext())
                        .inflate(R.layout.item_ticket, root, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TicketListViewHolder holder, int position) {
        holder.bind(ticketList.get(position), clickListener);
    }

    @Override
    public int getItemCount() { return ticketList.size(); }
}
