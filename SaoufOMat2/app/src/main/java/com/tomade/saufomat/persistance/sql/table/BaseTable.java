package com.tomade.saufomat.persistance.sql.table;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Basistabelle
 * <p>
 * Created by woors on 05.10.2017.
 */

abstract class BaseTable<ENTRY> {
    private String tableName;

    public BaseTable(String tableName) {
        this.tableName = tableName;
    }

    public abstract void createTable(SQLiteDatabase sqLiteDatabase);

    public void deleteTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + this.tableName);
    }

    public abstract void insertEntry(SQLiteDatabase sqLiteDatabase, ENTRY entry);

    public abstract void updateEntry(SQLiteDatabase sqLiteDatabase, ENTRY entry);

    public abstract ArrayList<ENTRY> getAllEntries(SQLiteDatabase sqLiteDatabase);
}
