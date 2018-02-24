package com.markusbilz.yown;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


class ItemWithButtonAdapter extends RecyclerView.Adapter<ItemWithButtonAdapter.ItemViewHolder> {

    private ArrayList<Item> items;
    @SuppressWarnings("unused")
    private Context context;
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView itemTitle;
        TextView itemDescription;

        ItemViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.tv_item_dispose_title);
            itemDescription = itemView.findViewById(R.id.tv_item_dispose_description);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(),itemTitle.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    ItemWithButtonAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(context == null)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_dispose, parent,false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.cardview_item_dispose, parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.itemTitle.setText(item.getTitle());
        holder.itemDescription.setText(item.getDescription());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
