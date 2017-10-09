package com.tomade.saufomat.persistance.sql.table;

import android.content.ContentValues;
import android.database.Cursor;

import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.TaskTarget;
import com.tomade.saufomat.activity.mainGame.task.taskevent.TaskEvent;

/**
 * Created by woors on 05.10.2017.
 */

public abstract class BaseTaskTable<ENTRY extends Task> extends BasePOJOTable<ENTRY> {
    protected static final String COLUMN_NAME_ID = "id";
    protected static final String COLUMN_NAME_TEXT = "text";
    protected static final String COLUMN_NAME_DIFFICULT = "difficult";
    protected static final String COLUMN_NAME_DRINK_COUNT = "drink_count";     //Getränkeanzahl bei Ja
    protected static final String COLUMN_NAME_COST = "cost";           //Getränkeanzahl bei Nein
    protected static final String COLUMN_NAME_TARGET = "target";
    protected static final String COLUMN_NAME_ALREADY_USED = "already_used";

    protected BaseTaskTable(String tableName) {
        super(tableName);
    }

    protected void fillTaskContentValue(ContentValues contentValues, Task task) {
        contentValues.put(COLUMN_NAME_ID, task.getId());
        contentValues.put(COLUMN_NAME_TEXT, task.getText());
        contentValues.put(COLUMN_NAME_DRINK_COUNT, task.getDrinkCount());
        contentValues.put(COLUMN_NAME_COST, task.getCost());
        contentValues.put(COLUMN_NAME_DIFFICULT, task.getDifficult().toString());
        contentValues.put(COLUMN_NAME_TARGET, task.getTaskTarget().toString());
        if (task.isAlreadyUsed()) {
            contentValues.put(COLUMN_NAME_ALREADY_USED, 1);
        } else {
            contentValues.put(COLUMN_NAME_ALREADY_USED, 0);
        }
    }

    protected Task parseTask(Cursor cursor, Task task) {
        task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)));
        task.setText(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TEXT)));
        task.setDrinkCount(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_DRINK_COUNT)));
        task.setCost(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_COST)));
        task.setDifficult(TaskDifficult.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DIFFICULT))));
        task.setTaskTarget(TaskTarget.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TARGET))));
        task.setAlreadyUsed(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ALREADY_USED)) != 0);
        return task;
    }

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

}
