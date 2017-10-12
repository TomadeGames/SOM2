package com.tomade.saufomat.persistance.sql.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tomade.saufomat.activity.mainGame.task.SimpleTask;
import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.TimedTask;

import java.util.ArrayList;

/**
 * Tabelle für TimedTasks
 * Created by woors on 11.10.2017.
 */

public class TimedTaskTable extends BaseTaskTable<TimedTask> {
    private static final String TAG = TimedTaskTable.class.getSimpleName();

    private static final String TABLE_NAME = "timed_task";
    private static final String COLUMN_NAME_TIME = "time";
    private static final String COLUMN_NAME_TASK_IF_WON = "task_if_won";
    private static final String COLUMN_NAME_TASK_IF_LOST = "task_if_lost";
    private static final String COLUMN_NAME_TIMER_STOPPABLE = "timer_stoppable";

    private static final String TIMED_TASK_TASK_TABLE_NAME = "timed_task_task";

    public TimedTaskTable() {
        super(TABLE_NAME);
    }

    @Override
    public void insertEntry(SQLiteDatabase sqLiteDatabase, TimedTask timedTask) {
        ContentValues contentValues = new ContentValues();
        this.fillContentValues(contentValues, timedTask);

        if (sqLiteDatabase.insert(TABLE_NAME, null, contentValues) == -1) {
            throw new IllegalStateException("Error at inserting timedTask " + timedTask + " in Table " + TABLE_NAME);
        }

        ContentValues lostValues = new ContentValues();
        this.fillSimpleTaskContentValues(lostValues, timedTask.getTaskIfLost());
        if (sqLiteDatabase.insert(TIMED_TASK_TASK_TABLE_NAME, null, lostValues) == -1) {
            throw new IllegalStateException("Error at inserting lost timedTask_Task in Table " +
                    TIMED_TASK_TASK_TABLE_NAME);
        }

        ContentValues wonValues = new ContentValues();
        this.fillSimpleTaskContentValues(wonValues, timedTask.getTaskIfWon());
        if (sqLiteDatabase.insert(TIMED_TASK_TASK_TABLE_NAME, null, wonValues) == -1) {
            throw new IllegalStateException("Error at inserting won timedTask_Task in Table " +
                    TIMED_TASK_TASK_TABLE_NAME);
        }
    }

    @Override
    public void updateEntry(SQLiteDatabase sqLiteDatabase, TimedTask timedTask) {
        ContentValues contentValues = new ContentValues();
        this.fillContentValues(contentValues, timedTask);

        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_NAME_ID + " = " + timedTask.getId(), null);

        ContentValues lostValues = new ContentValues();
        this.fillSimpleTaskContentValues(contentValues, timedTask.getTaskIfLost());
        sqLiteDatabase.update(TIMED_TASK_TASK_TABLE_NAME, lostValues, COLUMN_NAME_ID + " = "
                + timedTask.getTaskIfLost().getId(), null);

        ContentValues wonValues = new ContentValues();
        this.fillSimpleTaskContentValues(contentValues, timedTask.getTaskIfWon());
        sqLiteDatabase.update(TIMED_TASK_TASK_TABLE_NAME, wonValues, COLUMN_NAME_ID + " = "
                + timedTask.getTaskIfWon().getId(), null);
    }

    @Override
    public ArrayList<TimedTask> getAllEntries(SQLiteDatabase sqLiteDatabase) {
        ArrayList<TimedTask> entities = new ArrayList<>();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (result.moveToFirst()) {
            do {
                entities.add(this.parseTimedTask(result, sqLiteDatabase));
            } while (result.moveToNext());
        }

        return entities;
    }

    private TimedTask parseTimedTask(Cursor result, SQLiteDatabase database) {
        TimedTask timedTask = new TimedTask();
        this.parseTask(result, timedTask);
        timedTask.setTime(result.getLong(result.getColumnIndex(COLUMN_NAME_TIME)));
        timedTask.setTimerStoppable(result.getInt(result.getColumnIndex(COLUMN_NAME_TIMER_STOPPABLE)) != 0);
        int wonId = result.getInt(result.getColumnIndex(COLUMN_NAME_TASK_IF_WON));
        int lostId = result.getInt(result.getColumnIndex(COLUMN_NAME_TASK_IF_LOST));

        timedTask.setTaskIfWon(this.getSimpleTask(database, wonId));
        timedTask.setTaskIfLost(this.getSimpleTask(database, lostId));

        return timedTask;
    }

    private SimpleTask getSimpleTask(SQLiteDatabase database, int id) {
        Cursor result = database.rawQuery("SELECT * FROM " + TIMED_TASK_TASK_TABLE_NAME + " WHERE " + COLUMN_NAME_ID
                + " = " + id, null);
        if (result.moveToFirst()) {
            SimpleTask simpleTask = new SimpleTask();
            this.parseSimpleTask(result, simpleTask);
            return simpleTask;
        } else {
            throw new IllegalStateException("Won-/ Lost-Task not found with Id " + id);
        }
    }

    @Override
    public void createTable(SQLiteDatabase sqLiteDatabase) {
        super.createTable(sqLiteDatabase);

        sqLiteDatabase.execSQL("CREATE TABLE " + TIMED_TASK_TASK_TABLE_NAME + "("
                + this.getColumnsForSimpleTaskCreateStatement() + ")");
    }

    @Override
    public void deleteTable(SQLiteDatabase sqLiteDatabase) {
        super.deleteTable(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TIMED_TASK_TASK_TABLE_NAME);
    }

    @Override
    protected String getColumnsForCreateStatement() {
        return super.getColumnsForCreateStatement() + ", "
                + COLUMN_NAME_TIME + " LONG, "
                + COLUMN_NAME_TASK_IF_WON + " INTEGER, "
                + COLUMN_NAME_TASK_IF_LOST + " INTEGER, "
                + COLUMN_NAME_TIMER_STOPPABLE + " INTEGER";
    }

    @Override
    public ArrayList<Task> getUnusedTasks(SQLiteDatabase sqLiteDatabase, TaskDifficult taskDifficult) {
        ArrayList<Task> unusedTasks = new ArrayList<>();
        String timedTaskQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_ALREADY_USED + " = 0 AND " +
                COLUMN_NAME_DIFFICULT + " = '" + taskDifficult.toString() + "'";
        Cursor timedTaskResult = sqLiteDatabase.rawQuery(timedTaskQuery, null);

        try {
            if (timedTaskResult.moveToFirst()) {
                do {
                    unusedTasks.add(this.parseTimedTask(timedTaskResult, sqLiteDatabase));
                } while (timedTaskResult.moveToNext());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error at reading unused Tasks", e);
        } finally {
            timedTaskResult.close();
        }

        return unusedTasks;
    }

    @Override
    public ArrayList<Task> getAllTasks(SQLiteDatabase sqLiteDatabase, TaskDifficult taskDifficult) {
        ArrayList<Task> taskList = new ArrayList<>();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_DIFFICULT +
                " = \"" + taskDifficult.toString() + "\"", null);

        try {
            if (result.moveToFirst()) {
                do {
                    TimedTask taskEvent = this.parseTimedTask(result, sqLiteDatabase);
                    taskList.add(taskEvent);
                } while (result.moveToNext());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error at getting all TaskEvents", e);
        } finally {
            result.close();
        }
        Log.i(TAG, taskList.size() + " Tasks with Difficult " + taskDifficult + " loaded from " + TABLE_NAME);
        return taskList;
    }

    private void fillContentValues(ContentValues contentValues, TimedTask timedTask) {
        this.fillContentValue(contentValues, timedTask);
        contentValues.put(COLUMN_NAME_TIME, timedTask.getTime());
        contentValues.put(COLUMN_NAME_TASK_IF_WON, timedTask.getTaskIfWon().getId());
        contentValues.put(COLUMN_NAME_TASK_IF_LOST, timedTask.getTaskIfLost().getId());
        if (timedTask.isTimerStoppable()) {
            contentValues.put(COLUMN_NAME_TIMER_STOPPABLE, 1);
        } else {
            contentValues.put(COLUMN_NAME_TIMER_STOPPABLE, 0);
        }
    }
}
