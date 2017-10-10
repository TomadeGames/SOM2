package com.tomade.saufomat.persistance.sql.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Basistabelle
 * Created by woors on 09.10.2017.
 */

public abstract class BaseTable {
    private String tableName;

    protected BaseTable(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Löscht die Tabelle
     *
     * @param sqLiteDatabase die Datenbank, aus der die Tabelle gelöscht werden soll
     */
    public void deleteTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + this.tableName);
    }

    /**
     * Erstellt die Tabelle
     *
     * @param sqLiteDatabase die Datenbank, in der die Tabelle erstellt wird
     */
    public abstract void createTable(SQLiteDatabase sqLiteDatabase);
}
