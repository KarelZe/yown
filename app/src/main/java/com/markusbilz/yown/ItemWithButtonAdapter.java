package com.markusbilz.yown;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


class ItemWithButtonAdapter extends RecyclerView.Adapter<ItemWithButtonAdapter.ItemViewHolder> {

    private static ItemWithButtonAdapter mySingelton;
    private ArrayList<Item> items;
    private Activity activity;


    private ItemWithButtonAdapter(Activity activity) {
        this.activity = activity;
        items = new ArrayList<>();
        reload();
    }

    static ItemWithButtonAdapter getSingelton(Activity activity) {
        if (mySingelton == null)
            mySingelton = new ItemWithButtonAdapter(activity);
        return mySingelton;
    }

    void reload() {
        ItemDB itemDB = ItemDB.getInstance(activity.getApplicationContext());
        items = (ArrayList<Item>) itemDB.getAll();
    }

    void reloadFiltered(int filterClause) {
        ItemDB itemDB = ItemDB.getInstance(activity.getApplicationContext());
        items = (ArrayList<Item>) itemDB.getAllFiltered(filterClause);
    }

    Item getItem(int id) {
        for (Item item : items) {
            if (item.getId() == id)
                return item;
        }
        return null;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item_dispose, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.setCurrentItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Item currentItem;
        TextView itemTitle;
        TextView itemDescription;
        ImageView itemPhoto;
        Button itemSell;

        ItemViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.tv_item_dispose_title);
            itemDescription = itemView.findViewById(R.id.tv_item_dispose_description);
            itemPhoto = itemView.findViewById(R.id.iv_item_dispose_photo);
            itemSell = itemView.findViewById(R.id.btn_item_dispose_sell);
            itemSell.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void setCurrentItem(Item currentItem) {
            this.currentItem = currentItem;
            itemTitle.setText(currentItem.getTitle());
            itemDescription.setText(currentItem.getDescription());
            itemPhoto.setImageBitmap(BitmapUtility.byte2Bitmap(currentItem.getThumbnail()));
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_item_dispose_sell) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ebay.com"));
                view.getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(view.getContext(), EditActivity.class);
                intent.putExtra("id", currentItem.getId());
                view.getContext().startActivity(intent);
            }
        }
    }
}
