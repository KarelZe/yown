package com.markusbilz.yown;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
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
    private String uuid;
    @Nullable
    private Bitmap thumbnail;

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
        item = ItemWithCheckboxAdapter.getInstance(activity).getItem(id);
        String subtitle = "";
        uuid = UUID.randomUUID().toString();
        // item has been edited before.
        if (item != null) {
            editTitle.setTitle(item.getTitle());
            editCategory.setTitle(item.getCategory());
            editDescription.setTitle(item.getDescription());
            editImage.getAvatarView().setImageBitmap(BitmapUtility.byteToBitmap(item.getThumbnail()));
            subtitle = DateUtility.dateTimeUi(item.getDateOfLastUsage());
        }
        editTitle.setOnClickListener(this);
        editDescription.setOnClickListener(this);
        editImage.setOnClickListener(this);
        editCategory.setOnClickListener(this);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
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
                saveItem(view);
                break;
            case R.id.action_menu_delete:
                deleteItem(view);
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

    private void saveItem(@NonNull View view) {
        String title = editTitle.getTitle();
        String description = editDescription.getTitle();
        String category = editCategory.getTitle();

        if (item == null) {
            ItemDB.getInstance(view.getContext()).insert(title, description, category, BitmapUtility.bitmapToByte(thumbnail), uuid);
        } else {
            item.setTitle(title);
            item.setDescription(description);
            item.setCategory(category);
            // overwrite thumbnail only if a new image is available
            if (thumbnail != null) {
                item.setThumbnail(BitmapUtility.bitmapToByte(thumbnail));
            }
            ItemDB.getInstance(view.getContext()).update(item);
        }
        activity.finish();
    }

    private void deleteItem(@NonNull View view) {
        ItemDB.getInstance(view.getContext()).delete(item);
        activity.finish();
    }
}
