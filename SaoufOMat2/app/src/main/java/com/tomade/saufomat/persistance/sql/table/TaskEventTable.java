package com.tomade.saufomat.persistance.sql.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.TaskEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Tabelle für TaskEvents
 * Created by woors on 05.10.2017.
 */

public class TaskEventTable extends BaseTaskTable<TaskEvent> {
    private static final String TAG = TaskEventTable.class.getSimpleName();
    private static final String TABLE_NAME = "task_event";
    private static final String COLUMN_NAME_MAX_TURN_TIME = "max_turn_time";
    private static final String COLUMN_NAME_MIN_TURN_TIME = "min_turn_time";
    private static final String COLUMN_NAME_CURRENT_TASK_LIMIT = "current_task_limit";
    private static final String COLUMN_NAME_TASKS_TO_EVENT_COUNTER = "tasks_to_event_counter";
    private static final String COLUMN_NAME_EVENT_COUNTER = "event_counter";
    private static final String COLUMN_NAME_ACTIVE = "active";


    private static final String TASK_EVENT_TAKS_TABLE_NAME = "task_event_task";
    private static final String COLUMN_NAME_TASK_EVENT_ID = "task_event_id";

    public TaskEventTable() {
        super(TABLE_NAME);
    }

    @Override
    public void createTable(SQLiteDatabase sqLiteDatabase) {
        super.createTable(sqLiteDatabase);

        String taskEventTaskStatement = "CREATE TABLE " + TASK_EVENT_TAKS_TABLE_NAME + "("
                + super.getColumnsForCreateStatement() + ", "
                + COLUMN_NAME_TASK_EVENT_ID + " INTEGER)";

        sqLiteDatabase.execSQL(taskEventTaskStatement);
        Log.i(TAG, "Table " + TABLE_NAME + " created");
    }

    @Override
    protected String getColumnsForCreateStatement() {
        return super.getColumnsForCreateStatement() + ", " +
                COLUMN_NAME_EVENT_COUNTER + " INTEGER, " +
                COLUMN_NAME_TASKS_TO_EVENT_COUNTER + " INTEGER, " +
                COLUMN_NAME_CURRENT_TASK_LIMIT + " INTEGER, " +
                COLUMN_NAME_MAX_TURN_TIME + " INTEGER, " +
                COLUMN_NAME_ACTIVE + " INTEGER, " +
                COLUMN_NAME_MIN_TURN_TIME + " INTEGER";
    }

    @Override
    public void deleteTable(SQLiteDatabase sqLiteDatabase) {
        super.deleteTable(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TASK_EVENT_TAKS_TABLE_NAME);
    }

    @Override
    public void insertEntry(SQLiteDatabase sqLiteDatabase, TaskEvent entry) {
        ContentValues contentValues = new ContentValues();
        this.fillTaskEventContentValues(contentValues, entry);

        sqLiteDatabase.replace(TABLE_NAME, null, contentValues);
        this.insertTasksFromTaskEvent(sqLiteDatabase, entry);
    }

    @Override
    public void updateEntry(SQLiteDatabase sqLiteDatabase, TaskEvent entry) {

    }

    @Override
    public ArrayList<TaskEvent> getAllEntries(SQLiteDatabase sqLiteDatabase) {
        ArrayList<TaskEvent> taskList = new ArrayList<>();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        try {
            if (result.moveToFirst()) {
                do {
                    TaskEvent taskEvent = this.parseTaskEvent(sqLiteDatabase, result);
                    taskList.add(taskEvent);
                } while (result.moveToNext());
            }
        } finally {
            result.close();
        }

        Log.i(TAG, taskList.size() + " Tasks loaded from Database");
        return taskList;
    }

    /**
     * Gibt alle TaskEvents zurück, die einen bestimmten Schwierigkeitsgrad haben
     *
     * @param sqLiteDatabase die Datenbank, in der die Tabelle steht in Lesemodus
     * @param taskDifficult  die Schwierigketisstufe der Aufgaben
     * @return alle TaskEvents einer bestimmten Schweirigkeitsstufe
     */
    public ArrayList<Task> getAllTasks(SQLiteDatabase sqLiteDatabase, TaskDifficult taskDifficult) {
        ArrayList<Task> taskList = new ArrayList<>();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_DIFFICULT +
                " = \"" + taskDifficult.toString() + "\"", null);

        try {
            if (result.moveToFirst()) {
                do {
                    TaskEvent taskEvent = this.parseTaskEvent(sqLiteDatabase, result);
                    taskList.add(taskEvent);
                } while (result.moveToNext());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error at getting all TaskEvents", e);
        } finally {
            result.close();
        }
        Log.i(TAG, taskList.size() + " Tasks with Difficult " + taskDifficult + " loaded from Database");
        return taskList;
    }

    /**
     * Gibt alle ungenutzen TaskEvents zurück, die einen bestimmten Schwierigkeitsgrad haben
     *
     * @param sqLiteDatabase die Datenbank, in der die Tabelle steht in Lesemodus
     * @param taskDifficult  die Schwierigketisstufe der Aufgaben
     * @return alle ungenutzen TaskEvents einer bestimmten Schweirigkeitsstufe
     */
    public ArrayList<Task> getUnusedTasks(SQLiteDatabase sqLiteDatabase, TaskDifficult taskDifficult) {
        ArrayList<Task> unusedTasks = new ArrayList<>();
        String taskEventQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_ALREADY_USED + " = 0 AND " +
                COLUMN_NAME_DIFFICULT + " = '" +
                taskDifficult.toString() + "'";
        Cursor taskEventResult = sqLiteDatabase.rawQuery(taskEventQuery, null);

        try {
            if (taskEventResult.moveToFirst()) {
                do {
                    unusedTasks.add(this.parseTaskEvent(sqLiteDatabase, taskEventResult));
                } while (taskEventResult.moveToNext());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error at reading unused Tasks", e);
        } finally {
            taskEventResult.close();
        }

        return unusedTasks;
    }

    /**
     * Deaktiviert ein TaskEvent
     *
     * @param sqLiteDatabase die Datenbank, in der die Tabelle steht in Schreibmodus
     * @param taskEvent      das TaskEvent
     */
    public void deactivateTaskEvent(SQLiteDatabase sqLiteDatabase, TaskEvent taskEvent) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_ACTIVE, 0);
        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_NAME_ID + " = ? ", new String[]{Integer.toString
                (taskEvent.getId())});
    }

    /**
     * Aktiviert ein TaskEvent
     *
     * @param sqLiteDatabase die Datenbank, in der die Tabelle steht in Schreibmodus
     * @param taskEvent      das TaskEvent
     */
    public void activateTaskEvent(SQLiteDatabase sqLiteDatabase, TaskEvent taskEvent) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_ACTIVE, 1);
        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_NAME_ID + " = ? ", new String[]{Integer.toString
                (taskEvent.getId())});
    }

    /**
     * Gibt alle aktiven TaskEvents zurück
     *
     * @param sqLiteDatabase die Datenbank, in der die Tabelle steht in Lesemodus
     * @return alle aktiven TaskEvents
     */
    public ArrayList<TaskEvent> getActiveTaskEvents(SQLiteDatabase sqLiteDatabase) {
        ArrayList<TaskEvent> activeTaskEvents = new ArrayList<>();
        ArrayList<TaskEvent> allTaskEvents = this.getAllEntries(sqLiteDatabase);

        for (TaskEvent taskEvent : allTaskEvents) {
            if (taskEvent.isActive()) {
                activeTaskEvents.add(taskEvent);
            }
        }

        return activeTaskEvents;
    }

    private void fillTaskEventContentValues(ContentValues contentValues, TaskEvent taskEvent) {
        this.fillContentValue(contentValues, taskEvent);
        contentValues.put(COLUMN_NAME_MAX_TURN_TIME, taskEvent.getMaxTurnTime());
        contentValues.put(COLUMN_NAME_MIN_TURN_TIME, taskEvent.getMinTurnTime());
        contentValues.put(COLUMN_NAME_CURRENT_TASK_LIMIT, taskEvent.getCurrentTaskLimit());
        contentValues.put(COLUMN_NAME_TASKS_TO_EVENT_COUNTER, taskEvent
                .getTasksToEventCounter());
        contentValues.put(COLUMN_NAME_EVENT_COUNTER, taskEvent.getEventCounter());
        if (taskEvent.isActive()) {
            contentValues.put(COLUMN_NAME_ACTIVE, 1);
        } else {
            contentValues.put(COLUMN_NAME_ACTIVE, 0);
        }
    }


    private TaskEvent parseTaskEvent(SQLiteDatabase sqLiteDatabase, Cursor cursor) {
        TaskEvent taskEvent = new TaskEvent();
        taskEvent.setMaxTurnTime(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_MAX_TURN_TIME)));
        taskEvent.setMinTurnTime(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_MIN_TURN_TIME)));
        taskEvent.setCurrentTaskLimit(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_CURRENT_TASK_LIMIT)));
        taskEvent.setTasksToEventCounter(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_TASKS_TO_EVENT_COUNTER)));
        taskEvent.setEventCounter(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_EVENT_COUNTER)));
        taskEvent.setActive(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ACTIVE)) != 0);
        this.parseTask(cursor, taskEvent);
        this.findTasksToTaskEvent(sqLiteDatabase, taskEvent);

        return taskEvent;
    }


    private void findTasksToTaskEvent(SQLiteDatabase sqLiteDatabase, TaskEvent taskEvent) {
        String query = "Select * FROM " + TASK_EVENT_TAKS_TABLE_NAME + " WHERE " + COLUMN_NAME_TASK_EVENT_ID + " = "
                + taskEvent.getId();
        Cursor result = sqLiteDatabase.rawQuery(query, null);

        List<Task> taskList = new ArrayList<>();

        try {
            if (result.moveToFirst()) {
                do {
                    taskList.add(this.parseTask(result));
                } while (result.moveToNext());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error at getting Tasks for TaskEvent", e);
        } finally {
            result.close();
        }

        taskEvent.setTasks(taskList.toArray(new Task[0]));
    }

    private void insertTasksFromTaskEvent(SQLiteDatabase sqLiteDatabase, TaskEvent taskEvent) {
        for (Task task : taskEvent.getTasks()) {
            ContentValues contentValues = new ContentValues();
            this.fillContentValue(contentValues, task);
            contentValues.put(COLUMN_NAME_TASK_EVENT_ID, taskEvent.getId());

            if (sqLiteDatabase.replace(TASK_EVENT_TAKS_TABLE_NAME, null, contentValues) == -1) {
                Log.e(TAG, "Error at inserting Task from TaskEvent");
            }
        }
    }
}
