package com.markusbilz.yown;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


class ItemWithCheckboxAdapter extends RecyclerView.Adapter<ItemWithCheckboxAdapter.ItemViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static ItemWithCheckboxAdapter instance;
    private final Activity activity;
    private ArrayList<Item> items;


    private ItemWithCheckboxAdapter(Activity activity) {
        this.activity = activity;
        items = new ArrayList<>();
        reload();
    }

    // implementation of singleton pattern to make sure that only one instance of adapter is created.
    static ItemWithCheckboxAdapter getInstance(Activity activity) {
        if (instance == null) {
            instance = new ItemWithCheckboxAdapter(activity);
        }
        return instance;
    }

    void reload() {
        ItemDB itemDB = ItemDB.getInstance(activity.getApplicationContext());
        items = (ArrayList<Item>) itemDB.getAll();
    }

    @Nullable
    Item getItem(int id) {
        // force refresh as items can be updated in db, but not in Array list e.g. last usage
        reload();
        for (Item item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_vote, parent, false);
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
        final ImageView itemUsed;
        final ImageView itemPhoto;
        Item currentItem;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.tv_item_vote_title);
            itemDescription = itemView.findViewById(R.id.tv_item_vote_description);
            itemUsed = itemView.findViewById(R.id.iv_item_vote_used);
            itemPhoto = itemView.findViewById(R.id.iv_item_vote_photo);
            itemUsed.setOnClickListener(this);
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
            if (view.getId() == R.id.iv_item_vote_used) {
                String uuid = currentItem.getUuidNfc();
                ItemDB.getInstance(view.getContext()).update(uuid);
            } else {
                Intent intent = new Intent(view.getContext(), EditActivity.class);
                intent.putExtra("id", currentItem.getId());
                view.getContext().startActivity(intent);
            }
        }
    }
}
