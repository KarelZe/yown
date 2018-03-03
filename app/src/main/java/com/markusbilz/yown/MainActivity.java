package com.markusbilz.yown;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv_navigator);
        bottomNavigationView.setSelectedItemId(R.id.list);

        // set default tab
        MenuItem selectedItem = bottomNavigationView.getMenu().getItem(0);
        selectFragment(selectedItem);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                selectFragment(item);
                return false;
            }
        });

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NFC not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSettings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.itemAddItem:
                Intent addItemIntent = new Intent(this, AddActivity.class);
                startActivity(addItemIntent);
                break;
        }
        return true;
    }

    private void selectFragment(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.list:
                fragmentTransaction.replace(R.id.fl_content, new ListFragment()).commit();
                break;
            case R.id.dispose:
                fragmentTransaction.replace(R.id.fl_content, new DisposeFragment()).commit();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
