package com.markusbilz.yown;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemDB {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
                    ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ItemEntry.COLNAME_THUMBNAIL + " BLOB, "+
                    ItemEntry.COLNAME_TITLE + " TEXT, " +
                    ItemEntry.COLNAME_DESCRIPTION + " TEXT," +
                    ItemEntry.COLNAME_IS_NEEDED + " INTEGER, " +
                    ItemEntry.COLNAME_DATE_OF_CREATION + " TEXT," +
                    ItemEntry.COLNAME_DATE_OF_LAST_USAGE + " TEXT," +
                    ItemEntry.COLNAME_CATEGORY + " TEXT " +
                    ")";

    private static ItemDB myInstance;
    private Context context;

    private ItemDB(Context context) {
        this.context = context;
    }

    // implementation of singelton to make sure there is only one instance
    public static ItemDB getInstance(Context context) {
        if (myInstance == null)
            myInstance = new ItemDB(context);
        return myInstance;
    }

    public List<Item> getAll(){

        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            ArrayList<Item> result = new ArrayList<>();
            Cursor cursor = db.query(ItemEntry.TABLE_NAME,
                    new String[]{ItemEntry.COLNAME_ID, ItemEntry.COLNAME_THUMBNAIL, ItemEntry.COLNAME_TITLE, ItemEntry.COLNAME_DESCRIPTION,ItemEntry.COLNAME_CATEGORY, ItemEntry.COLNAME_IS_NEEDED,ItemEntry.COLNAME_DATE_OF_CREATION,ItemEntry.COLNAME_DATE_OF_LAST_USAGE},
                    null, null, null, null, null);
            try {
                while (cursor.moveToNext()) {
                    Item item = new Item(cursor.getInt(0), cursor.getBlob(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7));
                    result.add(item);
                }
                return result;
            } finally {
                cursor.close();
            }
        } finally {
            db.close();
        }
    }

    public List<Item> getAllFiltered(int filterClause)

    {

        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            ArrayList<Item> result = new ArrayList<>();
            String selectionClause = ItemEntry.COLNAME_IS_NEEDED + " = ?";
            String[] selectionArgs = {String.valueOf(filterClause)};
            Cursor cursor = db.query(ItemEntry.TABLE_NAME,
                    new String[]{ItemEntry.COLNAME_ID, ItemEntry.COLNAME_THUMBNAIL, ItemEntry.COLNAME_TITLE, ItemEntry.COLNAME_DESCRIPTION,ItemEntry.COLNAME_CATEGORY, ItemEntry.COLNAME_IS_NEEDED,ItemEntry.COLNAME_DATE_OF_CREATION,ItemEntry.COLNAME_DATE_OF_LAST_USAGE},
                    selectionClause, selectionArgs, null, null, null);
            try {
                while (cursor.moveToNext()) {
                    Item item = new Item(cursor.getInt(0), cursor.getBlob(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(4), cursor.getString(5), cursor.getString(6));
                    result.add(item);
                }
                return result;
            } finally {
                cursor.close();
            }
        } finally {
            db.close();
        }
    }

    public void insert(String title, String description, String category,byte[]thumbnail) {
        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            // generate date in ISO 8601 format, as per sqlite spec
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK);
            String date = sdf.format(new Date());
            int isNeeded = (int) (Math.random()+0.5);

            ContentValues values = new ContentValues();
            values.put(ItemEntry.COLNAME_THUMBNAIL,thumbnail);
            values.put(ItemEntry.COLNAME_TITLE, title);
            values.put(ItemEntry.COLNAME_DESCRIPTION, description);
            values.put(ItemEntry.COLNAME_CATEGORY,category);
            values.put(ItemEntry.COLNAME_IS_NEEDED,isNeeded);
            values.put(ItemEntry.COLNAME_DATE_OF_CREATION,date);
            values.put(ItemEntry.COLNAME_DATE_OF_LAST_USAGE,date);
            db.insert(ItemEntry.TABLE_NAME, ItemEntry.COLNAME_TITLE, values);
        } finally {
            db.close();
        }
    }

    public void update(Item item) {
        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(ItemEntry.COLNAME_TITLE, item.getTitle());
            values.put(ItemEntry.COLNAME_DESCRIPTION, item.getDescription());
            values.put(ItemEntry.COLNAME_THUMBNAIL,item.getThumbnail());
            values.put(ItemEntry.COLNAME_CATEGORY,item.getCategory());
            String selection = ItemEntry.COLNAME_ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(item.getId())};
            db.update(ItemEntry.TABLE_NAME, values, selection, selectionArgs);
        } finally {
            db.close();
        }
    }

    public void delete(Item item) {
        ItemDbHelper helper = new ItemDbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String whereClause = ItemEntry.COLNAME_ID + " = ?";
            String[] whereArgs = {String.valueOf(item.getId())};
            db.delete(ItemEntry.TABLE_NAME, whereClause, whereArgs);
        } finally {
            db.close();
        }
    }

    public static abstract class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String COLNAME_ID = "_id";
        public static final String COLNAME_THUMBNAIL = "thumbnail";
        public static final String COLNAME_TITLE = "title";
        public static final String COLNAME_DESCRIPTION = "description";
        public static final String COLNAME_IS_NEEDED = "isNeeded";
        public static final String COLNAME_DATE_OF_CREATION = "dateOfCreation";
        public static final String COLNAME_DATE_OF_LAST_USAGE = "dateOfLastUsage";
        public static final String COLNAME_CATEGORY = "category";
        public static final int FILTER_KEEP = 1;
        public static final int FILTER_LET_GO = 0;
    }

    public class ItemDbHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Item.db";

        public ItemDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}