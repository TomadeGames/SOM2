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

    public void useTask(SQLiteDatabase database, String task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_TASK, task);
        contentValues.put(COLUMN_NAME_USED, 1);
        database.update(TABLE_NAME, contentValues, COLUMN_NAME_TASK + " = " + task, null);
    }

    public List<String> getUnusedTasks(SQLiteDatabase database) {
        List<String> unusedTasks = new ArrayList<>();
        Cursor result = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_USED + " = 0", null);

        try {
            if (result.moveToFirst()) {
                do {
                    unusedTasks.add(result.getString(result.getColumnIndex(COLUMN_NAME_TASK)));
                } while (result.moveToNext());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error at getting unused Tasks", e);
        } finally {
            result.close();
        }

        return unusedTasks;
    }

    public void resetTasks(SQLiteDatabase database, String[] tasks) {
        for (String task : tasks) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME_USED, 0);
            database.update(TABLE_NAME, contentValues, COLUMN_NAME_TASK + " = " + task, null);
        }
    }

    @Override
    public void createTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_NAME_TASK + " TEXT PRIMARY KEY, " +
                COLUMN_NAME_USED + " INTEGER)");
    }
}
