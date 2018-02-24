package com.markusbilz.yown;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


class ItemWithCheckboxAdapter extends RecyclerView.Adapter<ItemWithCheckboxAdapter.ItemViewHolder> {

    private ArrayList<Item> items;
    @SuppressWarnings("unused")
    private Context context;
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView itemTitle;
        TextView itemDescription;
        ImageView itemUsed;
        ItemViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.tv_item_vote_title);
            itemDescription = itemView.findViewById(R.id.tv_item_vote_description);
            itemUsed = itemView.findViewById(R.id.iv_item_vote_used);
            itemUsed.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.iv_item_vote_used) {
                // Todo: Handle update with database connection
            } else if(view.getId() == R.id.iv_item_vote_photo){
                // Todo: Open activity to edit item
            }
        }
    }

    ItemWithCheckboxAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(context == null)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_vote, parent,false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.cardview_item_vote, parent,false);
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
