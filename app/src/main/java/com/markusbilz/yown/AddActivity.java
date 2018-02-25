package com.markusbilz.yown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lucasurbas.listitemview.ListItemView;


public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_SET_TITLE = 10;
    private static final int REQUEST_SET_DESCRIPTION = 11;
    private static final int REQUEST_SET_IMAGE = 12;
    private static final int REQUEST_SET_CATEGORY = 13;
    private static final int RESULT_OK = 200;
    private ListItemView addTitle;
    private ListItemView addImage;
    private ListItemView addNote;
    private ListItemView addCategory;


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
        Intent intent = new Intent(view.getContext(), AddDetailsActivity.class);

        switch (view.getId()) {
            case R.id.lv_add_title:
                intent.putExtra("title", "Set title...");
                startActivityForResult(intent, REQUEST_SET_TITLE);
                break;
            case R.id.lv_add_category:
                intent.putExtra("title", "Set category...");
                startActivityForResult(intent, REQUEST_SET_CATEGORY);
                break;
            case R.id.lv_add_image:
                intent.putExtra("title", "Set image...");
                startActivityForResult(intent, REQUEST_SET_IMAGE);
                break;
            case R.id.lv_add_note:
                intent.putExtra("title", "Set notes...");
                startActivityForResult(intent, REQUEST_SET_DESCRIPTION);
                break;
        }
    }

    private void saveItem(View view) {
        String title = addTitle.getTitle();
        String description = addNote.getTitle();
        ItemDB.getInstance(view.getContext()).insert(title, description);
        // close activity and return result to parent activity
        Intent resultIntent = new Intent();
        setResult(ListFragment.RESULT_OK, resultIntent);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String content = data.getStringExtra("data");
            switch (requestCode) {
                case REQUEST_SET_TITLE:
                    addTitle.setTitle(content);
                    break;
                case REQUEST_SET_IMAGE:
                    addImage.setTitle(content);
                    break;
                case REQUEST_SET_DESCRIPTION:
                    addNote.setTitle(content);
                    break;
                case REQUEST_SET_CATEGORY:
                    addCategory.setTitle(content);
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

}
