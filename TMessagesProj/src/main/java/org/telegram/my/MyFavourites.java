package org.telegram.my;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Mostafa on 26/01/2016.
 */
public class MyFavourites {

    public static boolean Favourite_Is(Context context, int forUserId, int targetUserId) {
        boolean r = false;

        SQLiteDatabase db = MyDB.DB_Read(context);
        Cursor cursor = db.query(MyDB.TABLE_FAVOURITES
                , new String[]{
                MyDB.COLUMN_FAVOURITES_ID,
                MyDB.COLUMN_FAVOURITES_USERID,
                MyDB.COLUMN_FAVOURITES_FAVOURITE_USERID
        }, String.format("%s=? and %s=?",
                MyDB.COLUMN_FAVOURITES_USERID,
                MyDB.COLUMN_FAVOURITES_FAVOURITE_USERID)
                , new String[]{String.valueOf(forUserId), String.valueOf(targetUserId)}, null, null, null);
        if (cursor.getCount() > 0)
            r = true;
        cursor.close();

        return r;
    }

    public static ArrayList<Integer> Favourite_GetList(Context context, int forUserId) {
        ArrayList<Integer> r = new ArrayList<>();

        SQLiteDatabase db = MyDB.DB_Read(context);
        Cursor cursor = db.query(MyDB.TABLE_FAVOURITES
                , new String[]{
                MyDB.COLUMN_FAVOURITES_FAVOURITE_USERID
        }, String.format("%s=?",
                MyDB.COLUMN_FAVOURITES_USERID)
                , new String[]{String.valueOf(forUserId)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                r.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return r;
    }

    public static void Favourite_Set(Context context, int forUserId, int targetUserId) {
        if (Favourite_Is(context, forUserId, targetUserId))
            return;

        ContentValues CVs = new ContentValues();
        CVs.put(MyDB.COLUMN_FAVOURITES_USERID, forUserId);
        CVs.put(MyDB.COLUMN_FAVOURITES_FAVOURITE_USERID, targetUserId);

        SQLiteDatabase db = MyDB.DB_Write(context);
        db.insert(MyDB.TABLE_FAVOURITES, null, CVs);
    }

    public static void Favourite_UnSet(Context context, int forUserId, int targetUserId) {
        if (!Favourite_Is(context, forUserId, targetUserId))
            return;

        SQLiteDatabase db = MyDB.DB_Write(context);
        db.delete(MyDB.TABLE_FAVOURITES, String.format("%s=? and %s=?", MyDB.COLUMN_FAVOURITES_USERID, MyDB.COLUMN_FAVOURITES_FAVOURITE_USERID), new String[]{String.valueOf(forUserId), String.valueOf(targetUserId)});
    }
}
