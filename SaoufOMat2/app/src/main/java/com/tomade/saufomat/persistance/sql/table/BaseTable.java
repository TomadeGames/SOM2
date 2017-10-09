package com.tomade.saufomat.persistance.sql.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by woors on 09.10.2017.
 */

public abstract class BaseTable {
    private String tableName;

    public BaseTable(String tableName) {
        this.tableName = tableName;
    }


    public void deleteTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + this.tableName);
    }

    public abstract void createTable(SQLiteDatabase sqLiteDatabase);
}
