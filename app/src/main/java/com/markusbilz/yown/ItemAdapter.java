package com.markusbilz.yown;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

// implementation adopted from https://youtu.be/Nw9JF55LDzE

class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static ItemAdapter instance;
    private static ArrayList<Item> items;

    // implementation of singleton pattern to make sure that only one instance of adapter is created.
    private ItemAdapter() {
    }

    static ItemAdapter getInstance(ArrayList<Item> itemList) {
        if (instance == null) {
            instance = new ItemAdapter();
        }
        items = itemList;
        return instance;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_default, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.setCurrentItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView itemTitle;
        final TextView itemDescription;
        final ImageView itemPhoto;
        // reference to currentItem is needed to obtain data like id and pass it to other activities
        Item currentItem;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.tv_item_default_title);
            itemDescription = itemView.findViewById(R.id.tv_item_default_description);
            itemPhoto = itemView.findViewById(R.id.iv_item_default_photo);
            itemView.setOnClickListener(this);
        }

        void setCurrentItem(@NonNull Item currentItem) {
            this.currentItem = currentItem;
            itemTitle.setText(currentItem.getTitle());
            itemDescription.setText(currentItem.getDescription());
            itemPhoto.setImageBitmap(BitmapUtility.byteToBitmap(currentItem.getThumbnail()));
        }

        @Override
        public void onClick(@NonNull View view) {
            Intent intent = new Intent(view.getContext(), EditActivity.class);
            intent.putExtra("id", currentItem.getId());
            view.getContext().startActivity(intent);
        }
    }
}
