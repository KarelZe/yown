package com.markusbilz.yown;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set first item as default selection and replace fragment
        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv_navigator);
        bottomNavigationView.setSelectedItemId(R.id.list);
        MenuItem selectedItem = bottomNavigationView.getMenu().getItem(0);
        switchFragment(selectedItem);

        // activate item in BottomNavigationView replace fragment with corresponding fragment
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switchFragment(item);
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSettings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
        return true;
    }

    private void switchFragment(@NonNull MenuItem menuItem) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (menuItem.getItemId()) {
            case R.id.list:
                fragmentTransaction.replace(R.id.fl_content, new ListFragment()).commit();
                break;
            case R.id.dispose:
                fragmentTransaction.replace(R.id.fl_content, new DisposeFragment()).commit();
                break;
        }
    }
}
