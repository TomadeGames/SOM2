package com.tomade.saufomat.persistance.sql.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;

import java.util.ArrayList;

/**
 * Tabelle für Aufgaben des Hauptspiels
 * Created by woors on 05.10.2017.
 */

public class TaskTable extends BaseTaskTable<Task> {
    private static final String TAG = TaskTable.class.getSimpleName();

    private static final String TABLE_NAME = "task";

    public TaskTable() {
        super(TABLE_NAME);
    }

    @Override
    public void insertEntry(SQLiteDatabase sqLiteDatabase, Task task) {
        ContentValues contentValues = new ContentValues();
        this.fillContentValue(contentValues, task);
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    @Override
    public void updateEntry(SQLiteDatabase sqLiteDatabase, Task task) {
        ContentValues contentValues = new ContentValues();
        this.fillContentValue(contentValues, task);

        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_NAME_ID + " = ? ", new String[]{Integer.toString(task
                .getId())});

        Log.i(TAG, "Task [" + task + "] updated");
    }

    @Override
    public ArrayList<Task> getAllEntries(SQLiteDatabase sqLiteDatabase) {
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<Task> taskList = new ArrayList<>();

        try {
            if (result.moveToFirst()) {
                do {
                    Task task = this.parseTask(result);
                    taskList.add(task);
                } while (result.moveToNext());
            }
        } finally {
            result.close();
        }
        return taskList;
    }

    /**
     * Gibt alle Aufgaben einer bestimmten Schwierigkeitsstufe zurück
     *
     * @param sqLiteDatabase die Datenbank, in der die Tabelle setht in Lesemodus
     * @param difficult      die Schwierigkeitsstufe
     * @return alle Aufgaben einer bestimmten Schwierigkeitsstufe
     */
    public ArrayList<Task> getAllTasks(SQLiteDatabase sqLiteDatabase, TaskDifficult difficult) {
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_DIFFICULT +
                " = \"" + difficult.toString() + "\"", null);

        ArrayList<Task> taskList = new ArrayList<>();

        try {
            if (result.moveToFirst()) {
                do {
                    Task task = this.parseTask(result);
                    taskList.add(task);
                } while (result.moveToNext());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error at getting all Task", e);
        } finally {
            result.close();
        }
        return taskList;
    }

    /**
     * Gibt alle ungenutzen Aufgaben einer bestimmten Schwierigkeitsstufe zurück
     *
     * @param sqLiteDatabase die Datenbank, in der die Tabelle setht in Lesemodus
     * @param difficult      die Schwierigkeitsstufe
     * @return alle ungenuzten Aufgaben einer bestimmten Schwierigkeitsstufe
     */
    public ArrayList<Task> getUnusedTasks(SQLiteDatabase sqLiteDatabase, TaskDifficult difficult) {
        ArrayList<Task> unusedTasks = new ArrayList<>();

        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_ALREADY_USED + " = 0 AND " +
                COLUMN_NAME_DIFFICULT + " = '" + difficult.toString() + "'";
        Cursor result = sqLiteDatabase.rawQuery(query, null);

        try {
            if (result.moveToFirst()) {
                do {
                    unusedTasks.add(this.parseTask(result));
                } while (result.moveToNext());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error at reading unused Tasks", e);
        } finally {
            result.close();
        }

        Log.i(TAG, unusedTasks.size() + " unused Tasks with Difficult " + difficult + " loaded from Database");
        return unusedTasks;
    }

}
