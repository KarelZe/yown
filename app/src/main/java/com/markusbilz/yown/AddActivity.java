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
import java.util.UUID;


public class AddActivity extends AppCompatActivity {

    private static final int REQUEST_NFC = 99;
    private NfcAdapter nfcAdapter;
    private String uuid;
    private boolean isWrite = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // generate uuid to pair nfc tag with database. Writing the id of the database entry to
        // the nfc tag is however not possible as it is generated later.
        uuid = UUID.randomUUID().toString();
    }

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
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null && !isWrite) {
            Ndef ndef = Ndef.get(tag);
            readFromNfc(ndef);
        } else if (tag != null) {
            Ndef ndef = Ndef.get(tag);
            NdefMessage ndefMessage = stringToNdefMessage(uuid);
            writeToNfc(ndef, ndefMessage);
        }

    }

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

    private void readFromNfc(@NonNull Ndef ndef) {
        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            String uuidFromTag = ndefMessageToString(ndefMessage);
            ndef.close();
            ItemDB.getInstance(this).update(uuidFromTag);
            Toast.makeText(this, uuidFromTag, Toast.LENGTH_SHORT).show();
        } catch (@NonNull IOException | FormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function converts ndefMessage to String by parsing the containing ndef Records.
     * Each record is pared and embraced with brackets, if Message contains multiple records,
     * records are separated by comma as following: [record1],[...]
     *
     * @param message Message stored on tag
     * @return String representation of message
     */

    private String ndefMessageToString(@Nullable NdefMessage message) {
        StringBuilder ret = new StringBuilder();
        if (message == null) {
            ret.append("[]");
            return ret.toString();
        }
        // first record contains uuid, second records contains application record
        NdefRecord[] records = message.getRecords();
        NdefRecord record = records[0];
        byte[] payload = record.getPayload();
        ret.append(new String(payload));
        return ret.toString();
    }

    /**
     * Function turns string into NdefMessage that contains plain text ndefRecord.
     *
     * @param string initial message for
     * @return NdefMessage containing ndefRecord with string
     */
    private NdefMessage stringToNdefMessage(@NonNull String string) {
        NdefRecord ndefRecords[] = {NdefRecord.createMime("text/plain",
                string.getBytes(Charset.forName("UTF-8"))),
                NdefRecord.createApplicationRecord("com.markusbilz.yown")};
        return new NdefMessage(ndefRecords);
    }
}
