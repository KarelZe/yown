package com.markusbilz.yown;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private String uuid;
    private TextView tvDebug;
    private FrameLayout flContent;

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
        tvDebug = findViewById(R.id.tv_debug);
        flContent = findViewById(R.id.fl_content);
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
            case R.id.itemHowTo:
                Intent howToIntent = new Intent(this, HowToActivity.class);
                startActivity(howToIntent);
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

    public void onResume() {
        super.onResume();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0);

        IntentFilter[] filters = new IntentFilter[]{};

        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, null);
        }
        // Add in-app console
        SharedPreferences sharedPreferences = this.getSharedPreferences(SettingsActivity.SHARED_PREFERENCES, MODE_PRIVATE);
        boolean enableDebug = sharedPreferences.getBoolean(SettingsActivity.ENABLE_DEBUGGING, false);
        // swap anchor for align layout above programmatically
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) flContent.getLayoutParams();
        if (enableDebug) {
            params.addRule(RelativeLayout.ABOVE, R.id.tv_debug);
            tvDebug.setVisibility(View.VISIBLE);
            flContent.setLayoutParams(params);
            debugApplication();
        } else {
            params.addRule(RelativeLayout.ABOVE, R.id.bnv_navigator);
            tvDebug.setVisibility(View.INVISIBLE);
        }
    }

    public void onPause() {
        super.onPause();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    public void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag != null) {
                Ndef ndef = Ndef.get(tag);
                readFromNfc(ndef);
            }
        }
    }

    /**
     * Add in-app debug console.
     * Implementation adapted from
     * https://stackoverflow.com/questions/7863841/can-logcat-results-for-log-i-be-viewed-in-our-activity
     */
    private void debugApplication() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            tvDebug.setText(log.toString());
            tvDebug.setMovementMethod(ScrollingMovementMethod.getInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read uuid from ndef message. Try to update usage using Update Item Task.
     *
     * @param ndef Content of Ndef tag
     */
    private void readFromNfc(@NonNull Ndef ndef) {
        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            uuid = ndefMessageToString(ndefMessage);
            ndef.close();
            UpdateItemTask updateItemTask = new UpdateItemTask();
            updateItemTask.execute();
            Toast.makeText(this, "updated item", Toast.LENGTH_SHORT).show();
        } catch (@NonNull IOException | FormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function converts ndefMessage to String by parsing the containing ndef Records.
     * Each record is pared, if Message contains multiple records,
     * only the first record is being read.
     *
     * @param message Message stored on tag
     * @return String representation of message
     */
    private String ndefMessageToString(@Nullable NdefMessage message) {
        StringBuilder ret = new StringBuilder();
        if (message == null) {
            return ret.toString();
        }
        // first record contains uuid, second records contains application record
        NdefRecord[] records = message.getRecords();
        NdefRecord record = records[0];
        byte[] payload = record.getPayload();
        ret.append(new String(payload));
        return ret.toString();
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateItemTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ItemDB.getInstance(getApplicationContext()).update(uuid);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
