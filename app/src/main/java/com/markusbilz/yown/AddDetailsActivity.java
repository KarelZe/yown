package com.markusbilz.yown;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

public class AddDetailsActivity extends AppCompatActivity {

    EditText addDetailsEditText;
    public static final int RESULT_SUCCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addDetailsEditText = findViewById(R.id.et_add_details);
        setContentView(R.layout.activity_add_details);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_set_details);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    // go back to fragment
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle button click here
        if (item.getItemId() == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED);
            finish(); // close this activity and return to preview activity
        }
        return super.onOptionsItemSelected(item);
    }
}
