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


public class EditActivity extends AppCompatActivity implements View.OnClickListener {


    public static final int RESULT_OK = -1;
    private static final int REQUEST_SET_TITLE = 10;
    private static final int REQUEST_SET_DESCRIPTION = 11;
    private static final int REQUEST_SET_IMAGE = 12;
    private static final int REQUEST_SET_CATEGORY = 13;
    private Item item;
    private ListItemView editTitle;
    private ListItemView editImage;
    private ListItemView editNote;
    private ListItemView editCategory;
    private Bitmap thumbnail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTitle = findViewById(R.id.lv_edit_title);
        editImage = findViewById(R.id.lv_edit_image);
        editCategory = findViewById(R.id.lv_edit_category);
        editNote = findViewById(R.id.lv_edit_note);

        int id = getIntent().getIntExtra("id", 0);
        item = ItemWithCheckboxAdapter.getSingelton(this).getItem(id);

        editTitle.setTitle(item.getTitle());
        editCategory.setTitle(item.getCategory());
        editNote.setTitle(item.getDescription());
        editImage.getAvatarView().setImageBitmap(BitmapUtility.byte2Bitmap(item.getThumbnail()));

        editTitle.setOnClickListener(this);
        editNote.setOnClickListener(this);
        editImage.setOnClickListener(this);
        editCategory.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_edit_item);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), AddDetailsActivity.class);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // pass text to details activity if altered before
        ListItemView tmp = (ListItemView) view;
        String text = tmp.getTitle();
        if (text != null && text.startsWith("Add "))
            text = "";

        switch (view.getId()) {
            case R.id.lv_edit_title:
                intent.putExtra("title", "Set title...");
                intent.putExtra("text", text);
                startActivityForResult(intent, REQUEST_SET_TITLE);
                break;
            case R.id.lv_edit_category:
                intent.putExtra("title", "Set category...");
                intent.putExtra("text", text);
                startActivityForResult(intent, REQUEST_SET_CATEGORY);
                break;
            case R.id.lv_edit_image:
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_SET_IMAGE);
                }
                break;
            case R.id.lv_edit_note:
                intent.putExtra("title", "Set notes...");
                intent.putExtra("text", text);
                startActivityForResult(intent, REQUEST_SET_DESCRIPTION);
                break;
        }
    }

    private void saveItem(View view) {
        String title = editTitle.getTitle();
        String description = editNote.getTitle();
        String category = editCategory.getTitle();
        item.setTitle(title);
        item.setDescription(description);
        item.setCategory(category);
        item.setThumbnail(BitmapUtility.bitmap2Byte(thumbnail));
        ItemDB.getInstance(view.getContext()).update(item);
        finish();
    }

    private void deleteItem(View view) {
        ItemDB.getInstance(view.getContext()).delete(item);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            String content = data.getStringExtra("data");
            switch (requestCode) {
                case REQUEST_SET_TITLE:
                    editTitle.setTitle(content);
                    break;
                case REQUEST_SET_IMAGE:
                    Bundle extras = data.getExtras();
                    thumbnail = (Bitmap) extras.get("data");
                    editImage.getAvatarView().setImageBitmap(thumbnail);
                    break;
                case REQUEST_SET_DESCRIPTION:
                    editNote.setTitle(content);
                    break;
                case REQUEST_SET_CATEGORY:
                    editCategory.setTitle(content);
                    break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view;
        switch (item.getItemId()) {
            case R.id.action_menu_done:
                view = findViewById(R.id.action_menu_done);
                saveItem(view);
                break;
            case R.id.action_menu_delete:
                view = findViewById(R.id.action_menu_delete);
                deleteItem(view);
                break;
        }
        return false;
    }

}
