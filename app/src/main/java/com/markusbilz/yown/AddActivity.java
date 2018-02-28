package com.markusbilz.yown;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lucasurbas.listitemview.ListItemView;


public class AddActivity extends AppCompatActivity implements View.OnClickListener, AddDetailsDialog.AddDetailsDialogListener {
    private static final int REQUEST_SET_IMAGE = 12;
    private static final int RESULT_OK = -1;
    private ListItemView addTitle;
    private ListItemView addImage;
    private ListItemView addNote;
    private ListItemView addCategory;
    private Bitmap thumbnail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addTitle = findViewById(R.id.lv_add_title);
        addImage = findViewById(R.id.lv_add_image);
        addCategory = findViewById(R.id.lv_add_category);
        addNote = findViewById(R.id.lv_add_note);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            getSupportActionBar().setTitle(R.string.title_add);
        }
        addTitle.setOnClickListener(this);
        addNote.setOnClickListener(this);
        addImage.setOnClickListener(this);
        addCategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (view.getId()) {
            case R.id.lv_add_title:
                openDialog(getString(R.string.title_lv_add_title), getString(R.string.title_lv_add_title), view.getId());
                break;
            case R.id.lv_add_category:
                openDialog(getString(R.string.title_lv_add_category), getString(R.string.title_lv_add_category), view.getId());
                break;
            case R.id.lv_add_image:
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_SET_IMAGE);
                }
                break;
            case R.id.lv_add_note:
                openDialog(getString(R.string.title_lv_add_note), getString(R.string.title_lv_add_note), view.getId());
                break;
        }
    }

    private void saveItem(View view) {
        String title = addTitle.getTitle();
        String description = addNote.getTitle();
        String category = addCategory.getTitle();
        ItemDB.getInstance(view.getContext()).insert(title, description, category, BitmapUtility.bitmap2Byte(thumbnail));
        // close activity and return result to parent activity
        Intent resultIntent = new Intent();
        setResult(ListFragment.RESULT_OK, resultIntent);
        finish();
    }

    public void openDialog(String title, String hint, int id) {
        AddDetailsDialog dialog = new AddDetailsDialog();
        dialog.show(getSupportFragmentManager(), "AddDetailsDialog");
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("hint", hint);
        bundle.putInt("id", id);
        dialog.setArguments(bundle);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SET_IMAGE:
                    Bundle extras = data.getExtras();
                    thumbnail = (Bitmap) extras.get("data");
                    addImage.getAvatarView().setImageBitmap(thumbnail);
                    break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_done:
                View view = findViewById(R.id.action_menu_done);
                saveItem(view);
                break;
        }
        return false;
    }

    @Override
    public void getDetails(String details, int id) {
        switch (id) {
            case R.id.lv_add_title:
                addTitle.setTitle(details);
                break;
            case R.id.lv_add_category:
                addCategory.setTitle(details);
                break;
            case R.id.lv_add_note:
                addNote.setTitle(details);
                break;
        }
    }
}
