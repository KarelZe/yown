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
import android.widget.ImageView;

import com.lucasurbas.listitemview.ListItemView;

import java.util.UUID;

public class EditFragment extends Fragment implements View.OnClickListener, AddDetailsDialog.AddDetailsDialogListener, AddCategoriesDialog.AddCategoriesDialogListener {
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
        // only load item if it hasn't been loaded yet
        if (item == null) {
            LoadItemTask loadItemTask = new LoadItemTask();
            loadItemTask.execute(id);
        }
        // if uuid could not be loaded from saved instance, then create a new one
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
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
        // keep title, description etc. after rotation
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_menu_done) {
            saveItem();
        } else if (item.getItemId() == R.id.item_menu_delete) {
            deleteItem();
        }
        return false;
    }


    /* function saves the state of the thumbnail to bundle to preserve it when rotating the phone.
        It is generally called when an activity might get killed.
         */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putByteArray("thumbnail", BitmapUtility.bitmapToByte(thumbnail));
    }

    /* restore thumbnail from bundle to preserve it when rotating the phone. On activity is
        the fragments version of onRestoreInstanceState()
        */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            byte[] bitmap = savedInstanceState.getByteArray("thumbnail");
            if (bitmap != null) {
                thumbnail = BitmapUtility.byteToBitmap(bitmap);
                setImage();
            }
            String uuid = savedInstanceState.getString("uuid");
            if (uuid != null) {
                this.uuid = uuid;
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (view.getId()) {
            case R.id.lv_edit_title:
                openDialog(getString(R.string.title_lv_edit_title), getString(R.string.title_lv_edit_title), view.getId());
                break;
            case R.id.lv_edit_category:
                openCategoryDialog(getString(R.string.title_lv_edit_category), view.getId());
                break;
            case R.id.lv_edit_image:
                if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_SET_IMAGE);
                }
                break;
            case R.id.lv_edit_description:
                openDialog(getString(R.string.title_lv_edit_description), getString(R.string.title_lv_edit_description), view.getId());
                break;
            default:
                break;
        }
    }

    /**
     * Open dialog to edit fields e. g. title, description
     * Using FragmentManager to check if (dynamic) DialogFragment is still in memory, otherwise
     * creates a new dialog. This prevents crashes when rotating the app with open dialogs.
     * Implementation according to:
     * https://medium.com/@android2ee/dialogfragment-coding-rules-93837b9c918
     *
     * @param title title in actionbar
     * @param hint  hint in EditText field
     * @param id    id of calling ui element
     */
    private void openDialog(String title, String hint, int id) {
        AddDetailsDialog dialog = (AddDetailsDialog) getFragmentManager().findFragmentByTag("detailsDialog");
        if (dialog == null) {
            dialog = new AddDetailsDialog();
            dialog.setTargetFragment(this, 1);
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("hint", hint);
            bundle.putInt("id", id);
            dialog.setArguments(bundle);
            getFragmentManager().beginTransaction().add(dialog, "detailsDialog").commit();
        } else {
            getFragmentManager().beginTransaction().show(dialog).commit();
        }
    }

    /**
     * Open dialog to choose proper category. Using FragmentManager to check if (dynamic) DialogFragment
     * is still in memory, otherwise creates a new dialog. This prevents crashes when rotating the app
     * with open dialogs. Implementation according to:
     * https://medium.com/@android2ee/dialogfragment-coding-rules-93837b9c918
     *
     * @param title title in actionbar
     * @param id    id of calling ui element
     */
    private void openCategoryDialog(String title, int id) {
        AddCategoriesDialog dialog = (AddCategoriesDialog) getFragmentManager().findFragmentByTag("categoriesDialog");
        if (dialog == null) {
            dialog = new AddCategoriesDialog();
            dialog.setTargetFragment(this, 1);
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putInt("id", id);
            dialog.setArguments(bundle);
            getFragmentManager().beginTransaction().add(dialog, "categoriesDialog").commit();
        } else {
            getFragmentManager().beginTransaction().show(dialog).commit();
        }

    }

    /**
     * Callback to receive data from dialog.
     *
     * @param details data being edited
     * @param id      id of calling ui element
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
            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_SET_IMAGE) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                thumbnail = (Bitmap) extras.get("data");
                // set image and crop to quad
                setImage();
            }
        }
    }

    /**
     * function to set thumbnail as cropped quad
     */
    private void setImage() {
        editImage.getAvatarView().setAdjustViewBounds(true);
        editImage.getAvatarView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        editImage.getAvatarView().setImageBitmap(thumbnail);
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
                // set image and crop to quad
                thumbnail = BitmapUtility.byteToBitmap(item.getThumbnail());
                setImage();
                subtitle = DateUtility.dateTimeUi(item.getDateOfLastUsage());
            }
            if (actionBar != null) {
                actionBar.setSubtitle(subtitle);
            }
        }
    }
}
