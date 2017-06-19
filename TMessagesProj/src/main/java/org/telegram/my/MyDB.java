package org.telegram.my;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mostafa on 26/01/2016.
 */
public class MyDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "MyDB_1";

    public static final String TABLE_FAVOURITES = "favourites";
    public static final String COLUMN_FAVOURITES_ID = "id";
    public static final String COLUMN_FAVOURITES_USERID = "userid";
    public static final String COLUMN_FAVOURITES_FAVOURITE_USERID = "favourite_userid";

    private static final String TABLE_FAVOURITES_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_FAVOURITES + " (" +
                    COLUMN_FAVOURITES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FAVOURITES_USERID + " INTEGER, " +
                    COLUMN_FAVOURITES_FAVOURITE_USERID + " INTEGER);";

    public static final String TABLE_PERMISSION_CHECKS = "permission_checks";
    public static final String COLUMN_PERMISSION_CHECKS_ID = "id";
    public static final String COLUMN_PERMISSION_CHECKS_VALID_IS = "valid_is";
    private static final String TABLE_PERMISSION_CHECKS_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_PERMISSION_CHECKS + " (" +
                    COLUMN_PERMISSION_CHECKS_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_PERMISSION_CHECKS_VALID_IS + " INTEGER);";

    public MyDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_FAVOURITES_CREATE);
        db.execSQL(TABLE_PERMISSION_CHECKS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > 1) {
            db.execSQL(TABLE_PERMISSION_CHECKS_CREATE);
        }
    }

    private static SQLiteDatabase mDB_Read;

    public static synchronized SQLiteDatabase DB_Read(Context context) {
        if (mDB_Read == null) {
            mDB_Read = new MyDB(context).getReadableDatabase();
        }
        return mDB_Read;
    }

    private static SQLiteDatabase mDB_Write;

    public static synchronized SQLiteDatabase DB_Write(Context context) {
        if (mDB_Write == null) {
            mDB_Write = new MyDB(context).getWritableDatabase();
        }
        return mDB_Write;
    }
}
