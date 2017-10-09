package com.tomade.saufomat.persistance.sql.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tomade.saufomat.constant.MiniGame;

import java.util.ArrayList;

/**
 * Tabelle für Minispiele
 * Created by woors on 05.10.2017.
 */

public class MiniGameTable {
    private static final String TAG = MiniGameTable.class.getSimpleName();

    private static final String TABLE_NAME = "mini_game";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_ALREADY_USED = "already_used";

    public void createTable(SQLiteDatabase sqLiteDatabase) {
        String miniGameStatement = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_NAME_NAME + " TEXT PRIMARY KEY, " +
                COLUMN_NAME_ALREADY_USED + " INTEGER) ";

        sqLiteDatabase.execSQL(miniGameStatement);
        Log.i(TAG, "Table " + TABLE_NAME + " created");
    }

    public void insertEntry(SQLiteDatabase sqLiteDatabase, MiniGame miniGame) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_NAME, miniGame.toString());
        contentValues.put(COLUMN_NAME_ALREADY_USED, 0);

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        Log.i(TAG, "Minigame [" + miniGame + "] added in Table");
    }

    public void setMiniGameUsed(SQLiteDatabase sqLiteDatabase, MiniGame miniGame, boolean used) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_NAME, miniGame.toString());
        if (used) {
            contentValues.put(COLUMN_NAME_ALREADY_USED, 1);
        } else {
            contentValues.put(COLUMN_NAME_ALREADY_USED, 0);
        }

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        Log.i(TAG, "Minigame [" + miniGame + "] added in Table");
    }

    public ArrayList<MiniGame> getUnusedMiniGames(SQLiteDatabase sqLiteDatabase) {
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_NAME_ALREADY_USED + " = 0 ", null);
        ArrayList<MiniGame> unusedMiniGames = new ArrayList<>();
        try {
            if (result.moveToFirst()) {
                do {
                    unusedMiniGames.add(MiniGame.valueOf(result.getString(result.getColumnIndex(COLUMN_NAME_NAME))));
                } while (result.moveToNext());
            }
        } finally {
            result.close();
        }
        Log.i(TAG, unusedMiniGames.size() + " unused Minigames loaded from Database");
        return unusedMiniGames;
    }

    public void deleteTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
