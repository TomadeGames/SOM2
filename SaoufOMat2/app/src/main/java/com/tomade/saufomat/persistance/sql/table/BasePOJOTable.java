package com.tomade.saufomat.persistance.sql.table;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Basistabelle für POJO-Objekte
 * <p>
 * Created by woors on 05.10.2017.
 */

abstract class BasePOJOTable<ENTRY> extends BaseTable {

    protected BasePOJOTable(String tableName) {
        super(tableName);
    }

    /**
     * Fügt einen neuen Eintrag in die Tabelle ein
     *
     * @param sqLiteDatabase die Datenbank in der die Tabelle steht in Schreibmodus
     * @param entry          der neue Eintrag
     */
    public abstract void insertEntry(SQLiteDatabase sqLiteDatabase, ENTRY entry);

    /**
     * Aktuallisiert einen Eintrag in der Tabelle
     *
     * @param sqLiteDatabase die Datenbank in der die Tabelle steht in Schreibmodus
     * @param entry          der Eintrag, der aktualisiert werden soll
     */
    public abstract void updateEntry(SQLiteDatabase sqLiteDatabase, ENTRY entry);

    /**
     * Gibt alle Einträge zurück
     *
     * @param sqLiteDatabase die Datenbank, in der die Tabelle steht in Lesemodus
     * @return alle Einträge in der Tabelle
     */
    public abstract ArrayList<ENTRY> getAllEntries(SQLiteDatabase sqLiteDatabase);
}
