package com.markusbilz.yown;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

public class SettingsActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES = "sharedPreferences";
    public static final String ADVANCED_SORTING = "advancedSorting";
    private SwitchCompat scAdvancedSorting;
    private boolean advancedSortingState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            getSupportActionBar().setTitle(R.string.title_settings);
        }

        loadData();

        scAdvancedSorting = findViewById(R.id.sc_advanced_sorting);
        scAdvancedSorting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveSettings();
            }
        });
        scAdvancedSorting.setChecked(advancedSortingState);
    }

    /**
     * Function to save the state of the switch to Shared Preferences
     */
    private void saveSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ADVANCED_SORTING, scAdvancedSorting.isChecked());
        editor.apply();
    }

    /**
     * Function to retrieve state of the switch from shared preferences. Advanced Sorting is
     * disabled by default.
     */
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        advancedSortingState = sharedPreferences.getBoolean(ADVANCED_SORTING, false);
    }
}
