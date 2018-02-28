package com.markusbilz.yown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class AddDetailsActivity extends AppCompatActivity implements View.OnKeyListener {

    private EditText etAddDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        etAddDetails = findViewById(R.id.et_add_details);

        // try setting hint and former text from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        if (title == null)
            title = "Set details";

        String text = intent.getStringExtra("text");
        if (text == null)
            text = "";

        etAddDetails.append(text);
        etAddDetails.setOnKeyListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
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

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("data", etAddDetails.getText().toString());
            setResult(EditActivity.RESULT_OK, resultIntent);
            finish();
            return true;
        }
        return false;
    }
}
