package com.markusbilz.yown;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lucasurbas.listitemview.ListItemView;

import java.util.UUID;

public class EditFragment extends Fragment implements View.OnClickListener, AddDetailsDialog.AddDetailsDialogListener {
    private static final int RESULT_OK = -1;
    private static final int REQUEST_SET_IMAGE = 12;
    private AppCompatActivity activity;
    private View view;
    private Item item;
    private ListItemView editTitle;
    private ListItemView editImage;
    private ListItemView editDescription;
    private ListItemView editCategory;
    private ActionBar actionBar;
    private String uuid;
    private String subtitle = "";
    @Nullable
    private Bitmap thumbnail;
    private OnUuidListener onUuidListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onUuidListener = (OnUuidListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnUuidListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit, container, false);
        activity = (AppCompatActivity) getActivity();
        editTitle = view.findViewById(R.id.lv_edit_title);
        editImage = view.findViewById(R.id.lv_edit_image);
        editCategory = view.findViewById(R.id.lv_edit_category);
        editDescription = view.findViewById(R.id.lv_edit_description);

        int id = getActivity().getIntent().getIntExtra("id", 0);
        LoadItemTask loadItemTask = new LoadItemTask();
        loadItemTask.execute(id);

        uuid = UUID.randomUUID().toString();
        onUuidListener.onUuidSet(uuid);
        editTitle.setOnClickListener(this);
        editDescription.setOnClickListener(this);
        editImage.setOnClickListener(this);
        editCategory.setOnClickListener(this);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_edit_item);
            actionBar.setSubtitle(subtitle);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_done:
                saveItem();
                break;
            case R.id.action_menu_delete:
                deleteItem();
                break;
        }
        return false;
    }


    @Override
    public void onClick(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (view.getId()) {
            case R.id.lv_edit_title:
                openDialog(getString(R.string.title_lv_edit_title), getString(R.string.title_lv_edit_title), view.getId());
                break;
            case R.id.lv_edit_category:
                openDialog(getString(R.string.title_lv_edit_category), getString(R.string.title_lv_edit_category), view.getId());
                break;
            case R.id.lv_edit_image:
                if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_SET_IMAGE);
                }
                break;
            case R.id.lv_edit_description:
                openDialog(getString(R.string.title_lv_edit_description), getString(R.string.title_lv_edit_description), view.getId());
                break;
        }
    }

    /**
     * Open dialog to edit fields e. g. title, description
     *
     * @param title title in actionbar
     * @param hint  hint in EditText field
     * @param id    id of calling ui element
     */
    private void openDialog(String title, String hint, int id) {
        AddDetailsDialog dialog = new AddDetailsDialog();
        dialog.setTargetFragment(this, 1);
        dialog.show(getChildFragmentManager(), "AddDetails");
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("hint", hint);
        bundle.putInt("id", id);
        dialog.setArguments(bundle);
    }

    /**
     * Callback to receive data from dialog.
     * @param details data being edited
     * @param id id of calling ui element
     */
    @Override
    public void getDetails(String details, int id) {
        switch (id) {
            case R.id.lv_edit_title:
                editTitle.setTitle(details);
                break;
            case R.id.lv_edit_category:
                editCategory.setTitle(details);
                break;
            case R.id.lv_edit_description:
                editDescription.setTitle(details);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SET_IMAGE:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        thumbnail = (Bitmap) extras.get("data");
                        editImage.getAvatarView().setImageBitmap(thumbnail);
                    }
                    break;
            }
        }
    }

    private void saveItem() {
        String title = editTitle.getTitle();
        String description = editDescription.getTitle();
        String category = editCategory.getTitle();

        if (item == null) {
            InsertItemTask insertItemTask = new InsertItemTask();
            insertItemTask.execute(title, description, category);
        } else {
            item.setTitle(title);
            item.setDescription(description);
            item.setCategory(category);
            // overwrite thumbnail only if a new image is available
            if (thumbnail != null) {
                item.setThumbnail(BitmapUtility.bitmapToByte(thumbnail));
            }
            UpdateItemTask updateItemTask = new UpdateItemTask();
            updateItemTask.execute();
        }
        activity.finish();
    }


    private void deleteItem() {
        DeleteItemTask deleteItemTask = new DeleteItemTask();
        deleteItemTask.execute();
        activity.finish();
    }

    /**
     * Callback to pass data between Fragment and Activity
     */
    interface OnUuidListener {
        void onUuidSet(String uuid);
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteItemTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ItemDB.getInstance(view.getContext()).delete(item);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateItemTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ItemDB.getInstance(view.getContext()).update(item);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertItemTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... itemData) {
            ItemDB.getInstance(view.getContext()).insert(itemData[0], itemData[1], itemData[2],
                    BitmapUtility.bitmapToByte(thumbnail), uuid);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadItemTask extends AsyncTask<Integer, Void, Item> {

        @Override
        protected Item doInBackground(Integer... integers) {
            return ItemDB.getInstance(view.getContext()).getItem(integers[0]);
        }

        @Override
        protected void onPostExecute(Item itemLoaded) {
            item = itemLoaded;

            // item has been edited before.
            if (item != null) {
                editTitle.setTitle(item.getTitle());
                editCategory.setTitle(item.getCategory());
                editDescription.setTitle(item.getDescription());
                editImage.getAvatarView().setImageBitmap(BitmapUtility.byteToBitmap(item.getThumbnail()));
                subtitle = DateUtility.dateTimeUi(item.getDateOfLastUsage());
            }
            if (actionBar != null) {
                actionBar.setSubtitle(subtitle);
            }
        }
    }
}
