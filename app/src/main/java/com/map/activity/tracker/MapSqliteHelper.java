package com.map.activity.tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MapSqliteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "maps.db";
    private static final int DATABASE_VERSION = 1;
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STARTLAT = "startlat";
    public static final String COLUMN_STARTLNG = "startlng";
    public static final String COLUMN_ENDLAT = "endlat";
    public static final String COLUMN_ENDLNG = "endlng";
    public static final String COLUMN_ROUTENAME = "routename";

    private static final String CREATE_TABLE = "create table " + DATABASE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_ROUTENAME + " text not null "
            + COLUMN_STARTLAT + " double " + COLUMN_STARTLNG + " double " + COLUMN_ENDLAT + " double "
            + COLUMN_ENDLNG + " double);";

    SQLiteDatabase mDB;

    public MapSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mDB.execSQL(CREATE_TABLE);
    }

    public long insert(ContentValues values) {
        return mDB.insert(DATABASE_NAME, null, values);
    }

    public void del() {
        mDB.delete(DATABASE_NAME, null, null);
    }

    public Cursor getAllRouteNames() {
        return mDB.query(DATABASE_NAME, new String[]{COLUMN_ROUTENAME}, null, null, null, null, null);
    }

    public Cursor getRouteVales(String route) {
        String WHERE = COLUMN_ROUTENAME + " =? ";
        return mDB.query(DATABASE_NAME, new String[]{COLUMN_STARTLAT, COLUMN_STARTLNG, COLUMN_ENDLAT, COLUMN_ENDLNG}, WHERE,
                new String[]{route}, null, null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
