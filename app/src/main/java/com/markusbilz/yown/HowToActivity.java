package com.markusbilz.yown;


import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class HowToActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set and  replace default home up button (<-) with x
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            actionBar.setTitle(R.string.title_how_to);
        }
        // load content from html file. File is located in assets folder
        setContentView(R.layout.activity_how_to);
        WebView wvHowTo = findViewById(R.id.wv_how_to);
        wvHowTo.loadUrl("file:///android_asset/index.html");
    }
}
