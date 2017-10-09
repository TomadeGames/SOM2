package com.tomade.saufomat.persistance.sql.table;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Basistabelle
 * <p>
 * Created by woors on 05.10.2017.
 */

abstract class BasePOJOTable<ENTRY> extends BaseTable {

    public BasePOJOTable(String tableName) {
        super(tableName);
    }

    public abstract void insertEntry(SQLiteDatabase sqLiteDatabase, ENTRY entry);

    public abstract void updateEntry(SQLiteDatabase sqLiteDatabase, ENTRY entry);

    public abstract ArrayList<ENTRY> getAllEntries(SQLiteDatabase sqLiteDatabase);
}
