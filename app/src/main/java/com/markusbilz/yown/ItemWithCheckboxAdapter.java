package com.markusbilz.yown;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
    private static ArrayList<Item> items;


    private ItemWithCheckboxAdapter() {
    }

    // implementation of singleton pattern to make sure that only one instance of adapter is created.
    static ItemWithCheckboxAdapter getInstance(ArrayList<Item> itemList) {
        if (instance == null) {
            instance = new ItemWithCheckboxAdapter();
        }
        items = itemList;
        return instance;
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
        final Context context;
        Item currentItem;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
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
                UpdateItemTask updateItemTask = new UpdateItemTask();
                updateItemTask.execute(uuid);
            } else {
                Intent intent = new Intent(view.getContext(), EditActivity.class);
                intent.putExtra("id", currentItem.getId());
                view.getContext().startActivity(intent);
            }
        }

        private class UpdateItemTask extends AsyncTask<String, Void, Void> {

            @Override
            protected Void doInBackground(String... uuid) {
                ItemDB.getInstance(context).update(uuid[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
    }

}
