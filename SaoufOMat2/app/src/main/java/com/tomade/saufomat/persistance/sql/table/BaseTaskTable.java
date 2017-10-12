package com.tomade.saufomat.persistance.sql.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tomade.saufomat.activity.mainGame.task.SimpleTask;
import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.TaskEvent;
import com.tomade.saufomat.activity.mainGame.task.TaskTarget;

import java.util.ArrayList;

/**
 * Basistabelle für alle Task-Tabellen
 * Created by woors on 05.10.2017.
 */

public abstract class BaseTaskTable<ENTRY extends Task> extends SimpleTaskTable<ENTRY> {
    protected static final String COLUMN_NAME_DIFFICULT = "difficult";
    protected static final String COLUMN_NAME_COST = "cost";

    protected BaseTaskTable(String tableName) {
        super(tableName);
    }

    @Override
    protected String getColumnsForCreateStatement() {
        return super.getColumnsForCreateStatement() + ", " +
                COLUMN_NAME_COST + " INTEGER, " +
                COLUMN_NAME_DIFFICULT + " TEXT";
    }

    /**
     * Füllt in ContentValues alle Werte eines Tasks
     *
     * @param contentValues die ContentValues, die gefüllt werden sollen
     * @param task          der Task, der in die ContentValues gefüllt wird
     */
    protected void fillContentValue(ContentValues contentValues, Task task) {
        this.fillSimpleTaskContentValues(contentValues, task);
        contentValues.put(COLUMN_NAME_COST, task.getCost());
        contentValues.put(COLUMN_NAME_DIFFICULT, task.getDifficult().toString());
    }

    /**
     * Füllt in ContentValues alle Werte eines SimpleTasks
     *
     * @param contentValues
     * @param simpleTask
     */
    protected void fillSimpleTaskContentValues(ContentValues contentValues, SimpleTask simpleTask) {
        contentValues.put(COLUMN_NAME_ID, simpleTask.getId());
        contentValues.put(COLUMN_NAME_TEXT, simpleTask.getText());
        contentValues.put(COLUMN_NAME_DRINK_COUNT, simpleTask.getDrinkCount());
        contentValues.put(COLUMN_NAME_TARGET, simpleTask.getTaskTarget().toString());
        if (simpleTask.isAlreadyUsed()) {
            contentValues.put(COLUMN_NAME_ALREADY_USED, 1);
        } else {
            contentValues.put(COLUMN_NAME_ALREADY_USED, 0);
        }
    }

    /**
     * Gibt einen Task aus einer Datenbankabfrage zurück
     *
     * @param cursor das Ergebnis der Datenbankabfrage
     * @param task   der Task, in den das Ergebnis eingetragen wird
     * @return der Task
     */
    protected Task parseTask(Cursor cursor, Task task) {
        this.parseSimpleTask(cursor, task);
        task.setCost(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_COST)));
        task.setDifficult(TaskDifficult.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DIFFICULT))));
        return task;
    }

    protected void parseSimpleTask(Cursor cursor, SimpleTask task) {
        task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)));
        task.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TEXT)));
        task.setDrinkCount(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_DRINK_COUNT)));
        task.setTaskTarget(TaskTarget.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TARGET))));
        task.setAlreadyUsed(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ALREADY_USED)) != 0);
    }

    /**
     * Gibt einen Task aus einer Datenbankabfrage zurück
     *
     * @param cursor das Ergebnis der Datenbankabfrage
     * @return der Task
     */
    protected Task parseTask(Cursor cursor) {
        Task task;
        if (TaskTarget.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TARGET))) ==
                TaskTarget.TASK_EVENT) {
            task = new TaskEvent();
        } else {
            task = new Task();
        }
        return this.parseTask(cursor, task);
    }

    public abstract ArrayList<Task> getUnusedTasks(SQLiteDatabase sqLiteDatabase, TaskDifficult taskDifficult);

    public abstract ArrayList<Task> getAllTasks(SQLiteDatabase sqLiteDatabase, TaskDifficult taskDifficult);
}
