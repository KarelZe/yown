package com.markusbilz.yown;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class EditActivity extends AppCompatActivity implements EditFragment.OnUuidListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }

    /**
     * Implementation of menu, nfc button is disabled for EditActivity
     *
     * @param menu menu in actionbar
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        MenuItem item = menu.findItem(R.id.action_menu_nfc);
        item.setVisible(false);
        return true;
    }

    // implementation happens in fragment
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return false;
    }

    /**
     * Callback to pass uuid between Fragment and Activity.
     *
     * @param uuid unique identifier to map item to nfc tag
     */
    @Override
    public void onUuidSet(String uuid) {

    }
}
