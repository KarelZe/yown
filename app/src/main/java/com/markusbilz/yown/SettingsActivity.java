package com.markusbilz.yown;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

public class SettingsActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES = "sharedPreferences";
    public static final String ADVANCED_SORTING = "advancedSorting";
    public static final String ENABLE_DEBUGGING = "enableDebugging";
    private SwitchCompat scAdvancedSorting;
    private SwitchCompat scEnableDebugging;
    private boolean advancedSortingState;
    private boolean enableDebuggingState;

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
        scEnableDebugging = findViewById(R.id.sc_enable_debugging);

        Button btnResetAll = findViewById(R.id.btn_reset_all);
        scAdvancedSorting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveSettings();
            }
        });
        scAdvancedSorting.setChecked(advancedSortingState);
        scEnableDebugging.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveSettings();
            }
        });
        scEnableDebugging.setChecked(enableDebuggingState);
        btnResetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteAllTask deleteAllTask = new DeleteAllTask();
                deleteAllTask.execute();
            }
        });
    }

    /**
     * Function to save the state of the switch to Shared Preferences
     */
    private void saveSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ADVANCED_SORTING, scAdvancedSorting.isChecked());
        editor.putBoolean(ENABLE_DEBUGGING, scEnableDebugging.isChecked());
        editor.apply();
    }

    /**
     * Function to retrieve state of the switch from shared preferences. Advanced Sorting is
     * disabled by default and so is enable debugging.
     */
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        advancedSortingState = sharedPreferences.getBoolean(ADVANCED_SORTING, false);
        enableDebuggingState = sharedPreferences.getBoolean(ENABLE_DEBUGGING, false);
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteAllTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ItemDB.getInstance(getApplicationContext()).deleteAll();
            return null;
        }
    }
}
