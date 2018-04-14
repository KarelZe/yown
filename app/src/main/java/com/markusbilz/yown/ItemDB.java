package com.markusbilz.yown;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

class ItemDB {

    static final int FILTER_KEEP = 1;
    static final int FILTER_LET_GO = 0;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
                    ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ItemEntry.COLNAME_THUMBNAIL + " BLOB, " +
                    ItemEntry.COLNAME_TITLE + " TEXT, " +
                    ItemEntry.COLNAME_DESCRIPTION + " TEXT," +
                    ItemEntry.COLNAME_UUID_NFC + " TEXT, " +
                    ItemEntry.COLNAME_DATE_OF_CREATION + " TEXT," +
                    ItemEntry.COLNAME_DATE_OF_LAST_USAGE + " TEXT," +
                    ItemEntry.COLNAME_CATEGORY + " TEXT " +
                    ")";
    @SuppressLint("StaticFieldLeak")
    private static ItemDB instance;
    private final Context context;

    private ItemDB(Context context) {
        this.context = context;
    }

    // implementation of singelton to make sure there is only one instance
    static ItemDB getInstance(Context context) {
        if (instance == null) {
            instance = new ItemDB(context);
        }
        return instance;
    }

    Item getItem(int id) {
        ItemDbHelper helper = new ItemDbHelper(context);
        try (SQLiteDatabase db = helper.getReadableDatabase()) {
            ArrayList<Item> result = new ArrayList<>();
            String selectionClause = ItemEntry.COLNAME_ID + " LIKE ?";
            String[] selectionArgs = {"" + id};
            try (Cursor cursor = db.query(ItemEntry.TABLE_NAME,
                    new String[]{ItemEntry.COLNAME_ID, ItemEntry.COLNAME_THUMBNAIL, ItemEntry.COLNAME_TITLE, ItemEntry.COLNAME_DESCRIPTION, ItemEntry.COLNAME_CATEGORY, ItemEntry.COLNAME_UUID_NFC, ItemEntry.COLNAME_DATE_OF_CREATION, ItemEntry.COLNAME_DATE_OF_LAST_USAGE},
                    selectionClause, selectionArgs, null, null, null)) {
                while (cursor.moveToNext()) {
                    Item item = new Item(cursor.getInt(0), cursor.getBlob(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                    result.add(item);
                }
                return result.size() >= 1 ? result.get(0) : null;
            }
        }
    }

    /**
     * Function to get all items from database. If no entries are found, it returns empty array list.
     *
     * @return List with all items
     */
    @NonNull
    List<Item> getAll() {

        ItemDbHelper helper = new ItemDbHelper(context);
        try (SQLiteDatabase db = helper.getReadableDatabase()) {
            ArrayList<Item> result = new ArrayList<>();
            try (Cursor cursor = db.query(ItemEntry.TABLE_NAME,
                    new String[]{ItemEntry.COLNAME_ID, ItemEntry.COLNAME_THUMBNAIL, ItemEntry.COLNAME_TITLE, ItemEntry.COLNAME_DESCRIPTION, ItemEntry.COLNAME_CATEGORY, ItemEntry.COLNAME_UUID_NFC, ItemEntry.COLNAME_DATE_OF_CREATION, ItemEntry.COLNAME_DATE_OF_LAST_USAGE},
                    null, null, null, null, null)) {
                while (cursor.moveToNext()) {
                    Item item = new Item(cursor.getInt(0), cursor.getBlob(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                    result.add(item);
                }
                return result;
            }
        }
    }

    /**
     * Function to get all items from database, that match certain filters. See Filter constants.
     * If no entries are found, it returns empty array list.
     *
     * @return List with all items
     */
    @NonNull
    List<Item> getAllFiltered(int filterClause) {

        ItemDbHelper helper = new ItemDbHelper(context);
        try (SQLiteDatabase db = helper.getReadableDatabase()) {
            ArrayList<Item> result = new ArrayList<>();
            String selectionClause = getSelectionClause(filterClause);
            try (Cursor cursor = db.query(ItemEntry.TABLE_NAME,
                    new String[]{ItemEntry.COLNAME_ID, ItemEntry.COLNAME_THUMBNAIL, ItemEntry.COLNAME_TITLE, ItemEntry.COLNAME_DESCRIPTION, ItemEntry.COLNAME_CATEGORY, ItemEntry.COLNAME_UUID_NFC, ItemEntry.COLNAME_DATE_OF_CREATION, ItemEntry.COLNAME_DATE_OF_LAST_USAGE},
                    selectionClause, null, null, null, null)) {
                while (cursor.moveToNext()) {
                    Item item = new Item(cursor.getInt(0), cursor.getBlob(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                    result.add(item);
                }
                return result;
            }
        }
    }

    /**
     * Insert new row into database
     *
     * @param title       title of item
     * @param description description of item
     * @param category    category of item
     * @param thumbnail   thumbnail of item as byte array
     * @param uuidNfc     uuid stored on nfc chip
     */
    void insert(String title, String description, String category, byte[] thumbnail, String uuidNfc) {
        ItemDbHelper helper = new ItemDbHelper(context);
        try (SQLiteDatabase db = helper.getReadableDatabase()) {

            String date = DateUtility.nowSql();
            ContentValues values = new ContentValues();
            values.put(ItemEntry.COLNAME_THUMBNAIL, thumbnail);
            values.put(ItemEntry.COLNAME_TITLE, title);
            values.put(ItemEntry.COLNAME_DESCRIPTION, description);
            values.put(ItemEntry.COLNAME_CATEGORY, category);
            values.put(ItemEntry.COLNAME_UUID_NFC, uuidNfc);
            values.put(ItemEntry.COLNAME_DATE_OF_CREATION, date);
            values.put(ItemEntry.COLNAME_DATE_OF_LAST_USAGE, date);
            db.insert(ItemEntry.TABLE_NAME, ItemEntry.COLNAME_TITLE, values);
        }
    }

    /**
     * Function to update date of last usage for database entry.
     *
     * @param uuid Uuid that is e. g. stored on nfc tags.
     * @return amount Number of items updated e.g 0 or 1
     */
    int update(String uuid) {
        ItemDbHelper helper = new ItemDbHelper(context);
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            String date = DateUtility.nowSql();
            ContentValues values = new ContentValues();
            values.put(ItemEntry.COLNAME_DATE_OF_LAST_USAGE, date);
            String selection = ItemEntry.COLNAME_UUID_NFC + " LIKE ?";
            String[] selectionArgs = {uuid};
            return db.update(ItemEntry.TABLE_NAME, values, selection, selectionArgs);
        }
    }

    void update(@NonNull Item item) {
        ItemDbHelper helper = new ItemDbHelper(context);
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ItemEntry.COLNAME_TITLE, item.getTitle());
            values.put(ItemEntry.COLNAME_DESCRIPTION, item.getDescription());
            values.put(ItemEntry.COLNAME_THUMBNAIL, item.getThumbnail());
            values.put(ItemEntry.COLNAME_CATEGORY, item.getCategory());
            String selection = ItemEntry.COLNAME_ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(item.getId())};
            db.update(ItemEntry.TABLE_NAME, values, selection, selectionArgs);
        }
    }

    /**
     * Remove item from database, if id item and id of db entry match.
     *
     * @param item Item to be deleted.
     */
    void delete(@NonNull Item item) {
        ItemDbHelper helper = new ItemDbHelper(context);
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            String whereClause = ItemEntry.COLNAME_ID + " = ?";
            String[] whereArgs = {String.valueOf(item.getId())};
            db.delete(ItemEntry.TABLE_NAME, whereClause, whereArgs);
        }
    }

    /**
     * Remove all item from database
     */
    void deleteAll() {
        ItemDbHelper helper = new ItemDbHelper(context);
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.delete(ItemEntry.TABLE_NAME, null, null);
        }
    }

    /**
     * Function that generates SQL selection clauses based on chosen filter
     *
     * @param filter Filter condition
     * @return SQL selection clause as string
     */
    private String getSelectionClause(int filter) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SettingsActivity.SHARED_PREFERENCES, MODE_PRIVATE);
        boolean advancedSortingState = sharedPreferences.getBoolean(SettingsActivity.ADVANCED_SORTING, false);

        if (filter == FILTER_KEEP) {
            if (advancedSortingState)
            // item has been used in the last 2 days on rolling basis and has been used before
            {
                return "julianday('now') - julianday(" + ItemEntry.COLNAME_DATE_OF_LAST_USAGE + ") <= 2" +
                        " AND " +
                        ItemEntry.COLNAME_DATE_OF_CREATION + "<>" + ItemEntry.COLNAME_DATE_OF_LAST_USAGE;
            } else
            // item has been used in the last 2 days on a rolling basis
            {
                return "julianday('now') - julianday(" + ItemEntry.COLNAME_DATE_OF_LAST_USAGE + ")<= 2";
            }
        } else {
            if (advancedSortingState)
            // item has been used in the last 2 days on rolling basis or has not been used before
            {
                return "julianday('now') - julianday(" + ItemEntry.COLNAME_DATE_OF_LAST_USAGE + ") > 2" +
                        " OR " +
                        ItemEntry.COLNAME_DATE_OF_CREATION + "==" + ItemEntry.COLNAME_DATE_OF_LAST_USAGE;
            } else
            // item has been used in the last 2 days on a rolling basis
            {
                return "julianday('now') - julianday(" + ItemEntry.COLNAME_DATE_OF_LAST_USAGE + ") > 2";
            }
        }

    }

    static abstract class ItemEntry implements BaseColumns {
        static final String TABLE_NAME = "item";
        static final String COLNAME_ID = "_id";
        static final String COLNAME_THUMBNAIL = "thumbnail";
        static final String COLNAME_TITLE = "title";
        static final String COLNAME_DESCRIPTION = "description";
        static final String COLNAME_UUID_NFC = "uuidNfc";
        static final String COLNAME_DATE_OF_CREATION = "dateOfCreation";
        static final String COLNAME_DATE_OF_LAST_USAGE = "dateOfLastUsage";
        static final String COLNAME_CATEGORY = "category";
    }

    class ItemDbHelper extends SQLiteOpenHelper {

        static final int DATABASE_VERSION = 1;
        static final String DATABASE_NAME = "Item.db";

        ItemDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(@NonNull SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            // empty, needed for upgrading the database
        }
    }
}