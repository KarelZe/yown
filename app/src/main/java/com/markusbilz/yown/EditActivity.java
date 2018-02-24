package com.markusbilz.yown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lucasurbas.listitemview.ListItemView;


public class EditActivity extends AppCompatActivity implements View.OnClickListener {


    private final int EDITTITLEREQUEST = 1;
    ListItemView editTitle;
    ListItemView editImage;
    ListItemView editNote;
    ListItemView editCategory;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTitle = findViewById(R.id.lv_edit_title);
        editImage = findViewById(R.id.lv_edit_image);
        editCategory = findViewById(R.id.lv_edit_category);
        editNote = findViewById(R.id.lv_edit_note);


        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(R.string.title_edit_item);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
        editTitle.setOnClickListener(this);
        editNote.setOnClickListener(this);
        editImage.setOnClickListener(this);
        editCategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.lv_edit_title:
            case R.id.lv_edit_category:
            case R.id.lv_edit_image:
            case R.id.lv_edit_note:
                Intent intent = new Intent(view.getContext(), AddDetailsActivity.class);
                startActivityForResult(intent, EDITTITLEREQUEST);
                break;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == EDITTITLEREQUEST) {
            if (resultCode == AddDetailsActivity.RESULT_SUCCESS) {
                // Todo: Handle parsing of data between two activities
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
                // Todo: Implement with database connection
                break;
        }
        return false;
    }

}
