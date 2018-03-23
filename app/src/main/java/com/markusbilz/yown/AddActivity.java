package com.markusbilz.yown;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;


public class AddActivity extends AppCompatActivity implements EditFragment.OnUuidListener {

    private static final int REQUEST_NFC = 99;
    private NfcAdapter nfcAdapter;
    private String uuid;
    private boolean isWrite = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    /**
     * Implementation of menu, delete button is disabled for AddActivity
     * @param menu menu in action bar
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        // disable delete for add activity
        MenuItem item = menu.findItem(R.id.action_menu_delete);
        item.setVisible(false);
        return true;
    }

    // implementation happens in fragment
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_nfc:
                isWrite = true;
                break;
        }
        return false;
    }

    /**
     * Prepare menu for further usage in fragment. Disable Nfc Fragment, if device doesn't support
     * NFC or if it disabled.
     * @param menu actionbar menu
     * @return boolean
     */
    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem itemNfc = menu.findItem(R.id.action_menu_nfc);
        if (!nfcIsEnabled()) {
            itemNfc.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);

    }

    private boolean nfcIsEnabled() {
        return (nfcAdapter != null && nfcAdapter.isEnabled());
    }


    @Override
    protected void onResume() {
        super.onResume();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_NFC,
                new Intent(this, AddActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0);
        IntentFilter[] filters = new IntentFilter[]{};
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, null);
        } else {
            Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null && isWrite) {
            Ndef ndef = Ndef.get(tag);
            NdefMessage ndefMessage = stringToNdefMessage(uuid);
            writeToNfc(ndef, ndefMessage);
        }

    }

    /**
     * Function to write NdefMessage to nfc tag
     * @param ndef        Ndef to connect to
     * @param ndefMessage NdefMessage to be written to nfc tag
     */
    private void writeToNfc(@Nullable Ndef ndef, NdefMessage ndefMessage) {
        if (ndef != null) {
            try {
                ndef.connect();
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Toast.makeText(this, "Writing tag was successful.", Toast.LENGTH_SHORT).show();
            } catch (@NonNull FormatException | IOException e) {
                e.printStackTrace();
            } finally {
                isWrite = false;
            }

        }
    }

    /**
     * Function turns string into NdefMessage that contains plain text ndefRecord.
     * @param string initial message for writing to ndef message
     * @return NdefMessage containing ndefRecord with string
     */
    private NdefMessage stringToNdefMessage(@NonNull String string) {
        NdefRecord ndefRecords[] = {NdefRecord.createMime("text/plain",
                string.getBytes(Charset.forName("UTF-8"))),
                NdefRecord.createApplicationRecord("com.markusbilz.yown")};
        return new NdefMessage(ndefRecords);
    }

    /**
     * Callback to pass uuid between Fragment and Activity.
     * @param uuid unique identifier to map item to nfc
     */
    @Override
    public void onUuidSet(String uuid) {
        this.uuid = uuid;
    }
}
