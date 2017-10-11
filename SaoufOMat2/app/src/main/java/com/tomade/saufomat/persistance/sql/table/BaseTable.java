package com.tomade.saufomat.persistance.sql.table;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.CallSuper;
import android.util.Log;

/**
 * Basistabelle
 * Created by woors on 09.10.2017.
 */

public abstract class BaseTable {
    private static final String TAG = BaseTable.class.getSimpleName();
    protected String tableName;

    protected BaseTable(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Löscht die Tabelle
     *
     * @param sqLiteDatabase die Datenbank, aus der die Tabelle gelöscht werden soll
     */
    @CallSuper
    public void deleteTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + this.tableName);
    }

    /**
     * Erstellt die Tabelle
     *
     * @param sqLiteDatabase die Datenbank, in der die Tabelle erstellt wird
     */
    @CallSuper
    public void createTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + this.tableName + "(" + this.getColumnsForCreateStatement() + ")");
        Log.i(TAG, "Table " + this.tableName + " created");
    }

    protected abstract String getColumnsForCreateStatement();
}
