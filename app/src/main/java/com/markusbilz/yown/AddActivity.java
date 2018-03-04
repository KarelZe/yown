package com.markusbilz.yown;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lucasurbas.listitemview.ListItemView;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;


public class AddActivity extends AppCompatActivity implements View.OnClickListener, AddDetailsDialog.AddDetailsDialogListener {
    private static final int REQUEST_SET_IMAGE = 12;
    private static final int REQUEST_NFC = 0;
    private static boolean isWrite = false;
    private ListItemView addTitle;
    private ListItemView addImage;
    private ListItemView addNote;
    private ListItemView addCategory;
    private String uuid;
    @Nullable
    private Bitmap thumbnail;
    private NfcAdapter nfcAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addTitle = findViewById(R.id.lv_add_title);
        addImage = findViewById(R.id.lv_add_image);
        addCategory = findViewById(R.id.lv_add_category);
        addNote = findViewById(R.id.lv_add_note);

        addTitle.setOnClickListener(this);
        addNote.setOnClickListener(this);
        addImage.setOnClickListener(this);
        addCategory.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            getSupportActionBar().setTitle(R.string.title_add);
        }
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // generate uuid to pair nfc tag with database. Writing the id of the database entry to
        // the nfc tag is however not possible as it is generated later.
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public void onClick(@NonNull View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (view.getId()) {
            case R.id.lv_add_title:
                openDialog(getString(R.string.title_lv_add_title), getString(R.string.title_lv_add_title), view.getId());
                break;
            case R.id.lv_add_category:
                openDialog(getString(R.string.title_lv_add_category), getString(R.string.title_lv_add_category), view.getId());
                break;
            case R.id.lv_add_image:
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_SET_IMAGE);
                }
                break;
            case R.id.lv_add_note:
                openDialog(getString(R.string.title_lv_add_note), getString(R.string.title_lv_add_note), view.getId());
                break;
        }
    }

    private void saveItem(@NonNull View view) {
        String title = addTitle.getTitle();
        String description = addNote.getTitle();
        String category = addCategory.getTitle();
        ItemDB.getInstance(view.getContext()).insert(title, description, category, BitmapUtility.bitmapToByte(thumbnail), uuid);
        // close activity and return result to parent activity
        Intent resultIntent = new Intent();
        setResult(ListFragment.RESULT_OK, resultIntent);
        finish();
    }

    private void openDialog(String title, String hint, int id) {
        AddDetailsDialog dialog = new AddDetailsDialog();
        dialog.show(getSupportFragmentManager(), "AddDetailsDialog");
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("hint", hint);
        bundle.putInt("id", id);
        dialog.setArguments(bundle);
    }

    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        Log.d("img", requestCode + ">" + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SET_IMAGE:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        thumbnail = (Bitmap) extras.get("data");
                        addImage.getAvatarView().setImageBitmap(thumbnail);
                    }
                    break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem itemNfc = menu.findItem(R.id.action_menu_nfc);
        if (!nfcIsEnabled()) {
            itemNfc.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_done:
                View view = findViewById(R.id.action_menu_done);
                saveItem(view);
                break;
            case R.id.action_menu_nfc:
                isWrite = true;
                break;
        }
        return false;
    }

    private boolean nfcIsEnabled() {
        return (nfcAdapter != null && nfcAdapter.isEnabled());
    }

    @Override
    public void getDetails(String details, int id) {
        switch (id) {
            case R.id.lv_add_title:
                addTitle.setTitle(details);
                break;
            case R.id.lv_add_category:
                addCategory.setTitle(details);
                break;
            case R.id.lv_add_note:
                addNote.setTitle(details);
                break;
        }
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
