package com.markusbilz.yown;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<Item> items;
    @SuppressWarnings("unused")
    private Context context;
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView itemTitle;
        TextView itemDescription;

        ItemViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.tv_item_default_title);
            itemDescription = itemView.findViewById(R.id.tv_item_default_description);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), EditActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    ItemAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(context == null)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_default, parent,false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.cardview_item_default, parent,false);
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
