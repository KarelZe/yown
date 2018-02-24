package com.markusbilz.yown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.lucasurbas.listitemview.ListItemView;


public class AddActivity extends AppCompatActivity implements View.OnClickListener {


    private final int ADDTITLEREQUEST = 1;
    ListItemView addTitle;
    ListItemView addImage;
    ListItemView addNote;
    ListItemView addCategory;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addTitle = findViewById(R.id.lv_add_title);
        addImage = findViewById(R.id.lv_add_image);
        addCategory = findViewById(R.id.lv_add_category);
        addNote = findViewById(R.id.lv_add_note);

        if(getSupportActionBar() != null) {
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

        switch (view.getId()) {
            case R.id.lv_add_title:
            case R.id.lv_add_image:
            case R.id.lv_add_category:
            case R.id.lv_add_note:
                Intent intent = new Intent(view.getContext(), AddDetailsActivity.class);
                startActivityForResult(intent, ADDTITLEREQUEST);
                break;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                // Todo: Add saving functionality here...
                break;
        }
        return false;
    }

}
