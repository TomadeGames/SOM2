package com.tomade.saufomat.persistance.sql.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Tabelle für bereits erledigte Sätze in IchHabNochNie
 * Created by woors on 09.10.2017.
 */

public class IchHabNochNieTable extends BaseTable {

    private static final String TAG = IchHabNochNieTable.class.getSimpleName();

    private static final String TABLE_NAME = "ich_hab_noch_nie";
    private static final String COLUMN_NAME_TASK = "task";
    private static final String COLUMN_NAME_USED = "used";

    public IchHabNochNieTable() {
        super(TABLE_NAME);
    }

    /**
     * Markiert eine Frage als bereits gesehen
     *
     * @param database die Datenbank in der die Tabelle steht in Schreibmodus
     * @param task     die Frage, die als gesehen markiert wird
     */
    public void useTask(SQLiteDatabase database, String task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_TASK, task);
        contentValues.put(COLUMN_NAME_USED, 1);
        database.update(TABLE_NAME, contentValues, COLUMN_NAME_TASK + " = \"" + this
                .convertQuoationMarksToPlaceholder(task) + "\"", null);
    }

    /**
     * Gibt alle noch nicht gesehenen Fragen zurück
     *
     * @param database die Datenbank, in der die Tabelle steht in Lesemodus
     * @return alle ungeshenen Fragen
     */
    public List<String> getUnusedTasks(SQLiteDatabase database) {
        List<String> unusedTasks = new ArrayList<>();
        Cursor result = database.rawQuery("SELECT " + COLUMN_NAME_TASK + " FROM " + TABLE_NAME + " WHERE " +
                COLUMN_NAME_USED + " = 0", null);

        try {
            if (result.moveToFirst()) {
                do {
                    unusedTasks.add(this.convertPlaceholderToQuoationMarks(result.getString(result.getColumnIndex
                            (COLUMN_NAME_TASK))));
                } while (result.moveToNext());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error at getting unused Tasks", e);
        } finally {
            result.close();
        }

        return unusedTasks;
    }

    /**
     * Setzt alle Fragen auf noch nicht gelesen
     *
     * @param database die Datenbank, in der die Tabelle steht in Schreibmodus
     * @param tasks    die Fragen, die auf noch nicht gelesen gesetzt werden sollen
     */
    public void resetTasks(SQLiteDatabase database, String[] tasks) {
        for (String task : tasks) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME_USED, 0);
            contentValues.put(COLUMN_NAME_TASK, this.convertQuoationMarksToPlaceholder(task));
            database.replace(TABLE_NAME, null, contentValues);
        }
    }

    @Override
    protected String getColumnsForCreateStatement() {
        return COLUMN_NAME_TASK + " TEXT PRIMARY KEY, " +
                COLUMN_NAME_USED + " INTEGER";
    }

    private String convertQuoationMarksToPlaceholder(String task) {
        return task.replace('"', '\'');
    }

    private String convertPlaceholderToQuoationMarks(String convertedTask) {
        return convertedTask.replace('\'', '"');
    }
}
