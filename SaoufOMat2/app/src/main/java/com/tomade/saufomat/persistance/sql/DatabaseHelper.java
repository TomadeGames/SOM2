package com.tomade.saufomat.persistance.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseArray;

import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDefinitions;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.TaskTarget;
import com.tomade.saufomat.activity.mainGame.task.taskevent.TaskEvent;
import com.tomade.saufomat.constant.MiniGame;
import com.tomade.saufomat.model.player.Player;
import com.tomade.saufomat.model.player.Statistic;
import com.tomade.saufomat.persistance.GameValueHelper;
import com.tomade.saufomat.persistance.sql.contract.MiniGameContract;
import com.tomade.saufomat.persistance.sql.contract.PlayerContract;
import com.tomade.saufomat.persistance.sql.contract.TaskContract;
import com.tomade.saufomat.persistance.sql.contract.TaskEventContract;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Verwaltet Datenbankoperationen
 * Created by woors on 01.04.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "saufomat_database";

    //TODO wenn die Datenbank geändert wird muss dieser Wert inkrementiert werden
    private static final int DATABASE_VERSION = 33;

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String playerStatement = "CREATE TABLE " + PlayerContract.Player.TABLE_NAME + "(" +
                PlayerContract.Player.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                PlayerContract.Player.COLUMN_NAME_NAME + " TEXT, " +
                PlayerContract.Player.COLUMN_NAME_WEIGHT + " INTEGER, " +
                PlayerContract.Player.COLUMN_NAME_IS_MAN + " INTEGER, " +
                PlayerContract.Player.COLUMN_NAME_DRINKS + " INTEGER, " +
                PlayerContract.Player.COLUMN_NAME_EASY_WINS + " INTEGER, " +
                PlayerContract.Player.COLUMN_NAME_MEDIUM_WINS + " INTEGER, " +
                PlayerContract.Player.COLUMN_NAME_HARD_WINS + " INTEGER, " +
                PlayerContract.Player.COLUMN_NAME_NEXT_PLAYER + " INTEGER, " +
                PlayerContract.Player.COLUMN_NAME_LAST_PLAYER + " INTEGER)";

        String taskStatement = "CREATE TABLE " + TaskContract.Task.TABLE_NAME + "(" +
                TaskContract.Task.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                TaskContract.Task.COLUMN_NAME_TEXT + " TEXT, " +
                TaskContract.Task.COLUMN_NAME_DRINK_COUNT + " INTEGER, " +
                TaskContract.Task.COLUMN_NAME_COST + " INTEGER, " +
                TaskContract.Task.COLUMN_NAME_DIFFICULT + " TEXT, " +
                TaskContract.Task.COLUMN_NAME_TARGET + " TEXT, " +
                TaskContract.Task.COLUMN_NAME_ALREADY_USED + " INTEGER)";

        String miniGameStatement = "CREATE TABLE " + MiniGameContract.MiniGame.TABLE_NAME + "(" +
                MiniGameContract.MiniGame.COLUMN_NAME_NAME + " TEXT PRIMARY KEY, " +
                MiniGameContract.MiniGame.COLUMN_NAME_ALREADY_USED + " INTEGER) ";

        String taskEventStatement = "CREATE TABLE " + TaskEventContract.TaskEvent.TABLE_NAME + "(" +
                TaskEventContract.TaskEvent.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                TaskEventContract.TaskEvent.COLUMN_NAME_TEXT + " TEXT, " +
                TaskEventContract.TaskEvent.COLUMN_NAME_DRINK_COUNT + " INTEGER, " +
                TaskEventContract.TaskEvent.COLUMN_NAME_COST + " INTEGER, " +
                TaskEventContract.TaskEvent.COLUMN_NAME_DIFFICULT + " TEXT, " +
                TaskEventContract.TaskEvent.COLUMN_NAME_TARGET + " TEXT, " +
                TaskEventContract.TaskEvent.COLUMN_NAME_ALREADY_USED + " INTEGER, " +
                TaskEventContract.TaskEvent.COLUMN_NAME_EVENT_COUNTER + " INTEGER, " +
                TaskEventContract.TaskEvent.COLUMN_NAME_TASKS_TO_EVENT_COUNTER + " INTEGER, " +
                TaskEventContract.TaskEvent.COLUMN_NAME_CURRENT_TASK_LIMIT + " INTEGER, " +
                TaskEventContract.TaskEvent.COLUMN_NAME_MAX_TURN_TIME + " INTEGER, " +
                TaskEventContract.TaskEvent.COLUMN_NAME_ACTIVE + " INTEGER, " +
                TaskEventContract.TaskEvent.COLUMN_NAME_MIN_TURN_TIME + " INTEGER)";

        String taskEventTaskStatement = "CREATE TABLE " + TaskEventContract.TaskEventTask.TABLE_NAME + "(" +
                TaskEventContract.TaskEventTask.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                TaskEventContract.TaskEventTask.COLUMN_NAME_TASK_EVENT_ID + " INTEGER, " +
                TaskEventContract.TaskEventTask.COLUMN_NAME_TEXT + " TEXT, " +
                TaskEventContract.TaskEventTask.COLUMN_NAME_DRINK_COUNT + " INTEGER, " +
                TaskEventContract.TaskEventTask.COLUMN_NAME_COST + " INTEGER, " +
                TaskEventContract.TaskEventTask.COLUMN_NAME_DIFFICULT + " TEXT, " +
                TaskEventContract.TaskEventTask.COLUMN_NAME_TARGET + " TEXT, " +
                TaskEventContract.TaskEventTask.COLUMN_NAME_ALREADY_USED + " INTEGER)";

        sqLiteDatabase.execSQL(playerStatement);
        Log.i(TAG, "Table " + PlayerContract.Player.TABLE_NAME + " created");
        sqLiteDatabase.execSQL(taskStatement);
        Log.i(TAG, "Table " + TaskContract.Task.TABLE_NAME + " created");
        sqLiteDatabase.execSQL(miniGameStatement);
        Log.i(TAG, "Table " + MiniGameContract.MiniGame.TABLE_NAME + " created");
        sqLiteDatabase.execSQL(taskEventStatement);
        Log.i(TAG, "Table " + TaskEventContract.TaskEvent.TABLE_NAME + " created");
        sqLiteDatabase.execSQL(taskEventTaskStatement);
        Log.i(TAG, "Table " + TaskEventContract.TaskEventTask.TABLE_NAME + " created");
        new GameValueHelper(this.context).saveDatabaseVersion(DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("DROP TABLE IF EXISTS " + PlayerContract.Player.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TaskContract.Task.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + MiniGameContract.MiniGame.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TaskEventContract.TaskEvent.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TaskEventContract.TaskEventTask.TABLE_NAME);
        Log.i(TAG, "onUpgrade: Tables dropped");
        this.onCreate(database);
    }

    private boolean insertMiniGame(MiniGame miniGame) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MiniGameContract.MiniGame.COLUMN_NAME_NAME, miniGame.toString());
        contentValues.put(MiniGameContract.MiniGame.COLUMN_NAME_ALREADY_USED, 0);

        database.insert(MiniGameContract.MiniGame.TABLE_NAME, null, contentValues);
        Log.i(TAG, "Minigame [" + miniGame + "] added in Table");
        return true;
    }

    public boolean insertPlayer(Player player) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlayerContract.Player.COLUMN_NAME_ID, player.getId());
        contentValues.put(PlayerContract.Player.COLUMN_NAME_NAME, player.getName());
        contentValues.put(PlayerContract.Player.COLUMN_NAME_WEIGHT, player.getWeight());
        if (player.getIsMan()) {
            contentValues.put(PlayerContract.Player.COLUMN_NAME_IS_MAN, 1);
        } else {
            contentValues.put(PlayerContract.Player.COLUMN_NAME_IS_MAN, 0);
        }
        contentValues.put(PlayerContract.Player.COLUMN_NAME_DRINKS, player.getDrinks());
        contentValues.put(PlayerContract.Player.COLUMN_NAME_NEXT_PLAYER, player.getNextPlayer().getId());
        contentValues.put(PlayerContract.Player.COLUMN_NAME_LAST_PLAYER, player.getLastPlayer().getId());

        contentValues.put(PlayerContract.Player.COLUMN_NAME_EASY_WINS, player.getStatistic().getEasyWins());
        contentValues.put(PlayerContract.Player.COLUMN_NAME_MEDIUM_WINS, player.getStatistic().getMediumWins());
        contentValues.put(PlayerContract.Player.COLUMN_NAME_HARD_WINS, player.getStatistic().getHardWins());

        database.insert(PlayerContract.Player.TABLE_NAME, null, contentValues);

        Log.i(TAG, "Player [" + player + "] added in Table");
        return true;
    }

    private boolean insertTask(Task task) {
        if (task instanceof TaskEvent) {
            return this.insertTaskEvent((TaskEvent) task);
        }
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        this.getTaskContentValue(contentValues, task);
        return database.insert(TaskContract.Task.TABLE_NAME, null, contentValues) != -1;

    }

    public boolean miniGameUsed(MiniGame miniGame) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MiniGameContract.MiniGame.COLUMN_NAME_NAME, miniGame.toString());
        contentValues.put(MiniGameContract.MiniGame.COLUMN_NAME_ALREADY_USED, 1);

        database.update(MiniGameContract.MiniGame.TABLE_NAME, contentValues, MiniGameContract.MiniGame
                .COLUMN_NAME_NAME + " =? ", new String[]{miniGame.toString()});

        Log.i(TAG, "Minigame [" + miniGame + "] is now used");
        return true;
    }

    public boolean resetMiniGame(MiniGame miniGame) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MiniGameContract.MiniGame.COLUMN_NAME_NAME, miniGame.toString());
        contentValues.put(MiniGameContract.MiniGame.COLUMN_NAME_ALREADY_USED, 0);

        database.update(MiniGameContract.MiniGame.TABLE_NAME, contentValues, MiniGameContract.MiniGame
                .COLUMN_NAME_NAME + "=? ", new String[]{miniGame.toString()});

        Log.i(TAG, "Minigame [" + miniGame + "] is no longer used");
        return true;
    }

    public boolean updatePlayer(Player player) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlayerContract.Player.COLUMN_NAME_NAME, player.getName());
        contentValues.put(PlayerContract.Player.COLUMN_NAME_WEIGHT, player.getWeight());
        if (player.getIsMan()) {
            contentValues.put(PlayerContract.Player.COLUMN_NAME_IS_MAN, 1);
        } else {
            contentValues.put(PlayerContract.Player.COLUMN_NAME_IS_MAN, 0);
        }
        contentValues.put(PlayerContract.Player.COLUMN_NAME_DRINKS, player.getDrinks());
        contentValues.put(PlayerContract.Player.COLUMN_NAME_NEXT_PLAYER, player.getNextPlayer().getId());
        contentValues.put(PlayerContract.Player.COLUMN_NAME_LAST_PLAYER, player.getLastPlayer().getId());

        contentValues.put(PlayerContract.Player.COLUMN_NAME_EASY_WINS, player.getStatistic().getEasyWins());
        contentValues.put(PlayerContract.Player.COLUMN_NAME_MEDIUM_WINS, player.getStatistic().getMediumWins());
        contentValues.put(PlayerContract.Player.COLUMN_NAME_HARD_WINS, player.getStatistic().getHardWins());

        database.update(PlayerContract.Player.TABLE_NAME, contentValues,
                PlayerContract.Player.COLUMN_NAME_ID + " = ? ", new String[]{Integer.toString(player.getId())});

        Log.i(TAG, "Player [" + player + "] updated");
        return true;
    }

    public boolean updateTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        database.update(TaskContract.Task.TABLE_NAME, this.getTaskContentValue(contentValues, task),
                TaskContract.Task.COLUMN_NAME_ID + " = ? ", new String[]{Integer.toString(task.getId())});

        Log.i(TAG, "Task [" + task + "] updated");
        return true;
    }

    private ContentValues getTaskContentValue(ContentValues contentValues, Task task) {
        contentValues.put(TaskContract.Task.COLUMN_NAME_ID, task.getId());
        contentValues.put(TaskContract.Task.COLUMN_NAME_TEXT, task.getText());
        contentValues.put(TaskContract.Task.COLUMN_NAME_DRINK_COUNT, task.getDrinkCount());
        contentValues.put(TaskContract.Task.COLUMN_NAME_COST, task.getCost());
        contentValues.put(TaskContract.Task.COLUMN_NAME_DIFFICULT, task.getDifficult().toString());
        contentValues.put(TaskContract.Task.COLUMN_NAME_TARGET, task.getTaskTarget().toString());
        if (task.isAlreadyUsed()) {
            contentValues.put(TaskContract.Task.COLUMN_NAME_ALREADY_USED, 1);
        } else {
            contentValues.put(TaskContract.Task.COLUMN_NAME_ALREADY_USED, 0);
        }

        return contentValues;
    }

    public ArrayList<MiniGame> getUnusedMiniGames() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor result = database.rawQuery("SELECT * FROM " + MiniGameContract.MiniGame.TABLE_NAME + " WHERE " +
                MiniGameContract.MiniGame.COLUMN_NAME_ALREADY_USED + " = 0 ", null);
        ArrayList<MiniGame> unusedMiniGames = new ArrayList<>();
        try {
            if (result.moveToFirst()) {
                do {
                    unusedMiniGames.add(MiniGame.valueOf(result.getString(result.getColumnIndex(MiniGameContract
                            .MiniGame
                            .COLUMN_NAME_NAME))));
                } while (result.moveToNext());
            }
        } finally {
            result.close();
        }
        Log.i(TAG, unusedMiniGames.size() + " unused Minigames loaded from Database");
        return unusedMiniGames;
    }

    public ArrayList<Player> getAllPlayer() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor result = database.rawQuery("SELECT * FROM " + PlayerContract.Player.TABLE_NAME, null);
        SparseArray<Player> playerList = new SparseArray<>();
        Map<Player, Integer> nextPlayer = new HashMap<>();
        Map<Player, Integer> lastPlayer = new HashMap<>();
        try {
            if (result.moveToFirst()) {
                do {
                    Player player = new Player();
                    player.setId(result.getInt(result.getColumnIndex(PlayerContract.Player.COLUMN_NAME_ID)));
                    player.setName(result.getString(result.getColumnIndex(PlayerContract.Player.COLUMN_NAME_NAME)));
                    player.setWeight(result.getInt(result.getColumnIndex(PlayerContract.Player.COLUMN_NAME_WEIGHT)));
                    player.setDrinks(result.getInt(result.getColumnIndex(PlayerContract.Player.COLUMN_NAME_DRINKS)));
                    player.setIsMan(result.getInt(result.getColumnIndex(PlayerContract.Player.COLUMN_NAME_IS_MAN)) !=
                            0);
                    nextPlayer.put(player, result.getInt(result.getColumnIndex(PlayerContract.Player
                            .COLUMN_NAME_NEXT_PLAYER)));
                    lastPlayer.put(player, result.getInt(result.getColumnIndex(PlayerContract.Player
                            .COLUMN_NAME_LAST_PLAYER)));

                    Statistic statistic = new Statistic();
                    statistic.setEasyWins(result.getInt(result.getColumnIndex(PlayerContract.Player
                            .COLUMN_NAME_EASY_WINS)));
                    statistic.setMediumWins(result.getInt(result.getColumnIndex(PlayerContract.Player
                            .COLUMN_NAME_MEDIUM_WINS)));
                    statistic.setHardWins(result.getInt(result.getColumnIndex(PlayerContract.Player
                            .COLUMN_NAME_HARD_WINS)));

                    playerList.put(player.getId(), player);
                } while (result.moveToNext());

                for (Map.Entry<Player, Integer> playerEntry : nextPlayer.entrySet()) {
                    playerEntry.getKey().setNextPlayer(playerList.get(playerEntry.getValue()));
                }

                for (Map.Entry<Player, Integer> playerEntry : lastPlayer.entrySet()) {
                    playerEntry.getKey().setLastPlayer(playerList.get(playerEntry.getValue()));
                }
            }
        } finally {
            result.close();
        }
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < playerList.size(); i++) {
            players.add(playerList.get(i));
        }
        Log.i(TAG, playerList.size() + " Players loaded from Databse");
        return players;
    }

    public ArrayList<Task> getAllTasks(TaskDifficult difficult) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor result = database.rawQuery("SELECT * FROM " + TaskContract.Task.TABLE_NAME + " WHERE " + TaskContract
                .Task.COLUMN_NAME_DIFFICULT + " =?", new String[]{difficult.toString()});

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

        result = database.rawQuery("SELECT * FROM " + TaskEventContract.TaskEvent.TABLE_NAME + " WHERE " +
                TaskEventContract.TaskEvent.COLUMN_NAME_DIFFICULT + " =?", new String[]{difficult.toString()});

        try {
            if (result.moveToFirst()) {
                do {
                    TaskEvent taskEvent = this.parseTaskEvent(result);
                    taskList.add(taskEvent);
                } while (result.moveToNext());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error at getting all TaskEvents", e);
        } finally {
            result.close();
        }
        Log.i(TAG, taskList.size() + " Tasks with Difficult " + difficult + " loaded from Database");
        return taskList;
    }

    public ArrayList<Task> getAllTasks() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor result = database.rawQuery("SELECT * FROM " + TaskContract.Task.TABLE_NAME, null);

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

        result = database.rawQuery("SELECT * FROM " + TaskEventContract.TaskEvent.TABLE_NAME, null);

        try {
            if (result.moveToFirst()) {
                do {
                    TaskEvent taskEvent = this.parseTaskEvent(result);
                    taskList.add(taskEvent);
                } while (result.moveToNext());
            }
        } finally {
            result.close();
        }

        Log.i(TAG, taskList.size() + " Tasks loaded from Database");
        return taskList;
    }

    public ArrayList<Task> getUnusedTasks(TaskDifficult difficult) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Task> unusedTasks = new ArrayList<>();

        String query = "Select * FROM " + TaskContract.Task.TABLE_NAME + " WHERE " + TaskContract
                .Task.COLUMN_NAME_ALREADY_USED + " = 0 AND " + TaskContract.Task.COLUMN_NAME_DIFFICULT + " = '" +
                difficult.toString() + "'";
        Cursor result = database.rawQuery(query, null);

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

        String taskEventQuery = "SELECT * FROM " + TaskEventContract.TaskEvent.TABLE_NAME + " WHERE " + TaskContract
                .Task.COLUMN_NAME_ALREADY_USED + " = 0 AND " + TaskContract.Task.COLUMN_NAME_DIFFICULT + " = '" +
                difficult.toString() + "'";
        Cursor taskEventResult = database.rawQuery(taskEventQuery, null);

        try {
            if (taskEventResult.moveToFirst()) {
                do {
                    unusedTasks.add(this.parseTaskEvent(taskEventResult));
                } while (taskEventResult.moveToNext());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error at reading unused Tasks", e);
        } finally {
            taskEventResult.close();
        }

        Log.i(TAG, unusedTasks.size() + " unused Tasks with Difficult " + difficult + " loaded from Database");
        return unusedTasks;
    }

    public boolean insertTaskEvent(TaskEvent taskEvent) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        this.getTaskEventContentValues(contentValues, taskEvent);

        database.replace(TaskEventContract.TaskEvent.TABLE_NAME, null, contentValues);
        this.insertTasksFromTaskEvent(taskEvent);
        return true;
    }

    private void getTaskEventContentValues(ContentValues contentValues, TaskEvent taskEvent) {
        this.getTaskContentValue(contentValues, taskEvent);
        contentValues.put(TaskEventContract.TaskEvent.COLUMN_NAME_MAX_TURN_TIME, taskEvent.getMaxTurnTime());
        contentValues.put(TaskEventContract.TaskEvent.COLUMN_NAME_MIN_TURN_TIME, taskEvent.getMinTurnTime());
        contentValues.put(TaskEventContract.TaskEvent.COLUMN_NAME_CURRENT_TASK_LIMIT, taskEvent.getCurrentTaskLimit());
        contentValues.put(TaskEventContract.TaskEvent.COLUMN_NAME_TASKS_TO_EVENT_COUNTER, taskEvent
                .getTasksToEventCounter());
        contentValues.put(TaskEventContract.TaskEvent.COLUMN_NAME_EVENT_COUNTER, taskEvent.getEventCounter());
        if (taskEvent.isActive()) {
            contentValues.put(TaskEventContract.TaskEvent.COLUMN_NAME_ACTIVE, 1);
        } else {
            contentValues.put(TaskEventContract.TaskEvent.COLUMN_NAME_ACTIVE, 0);
        }
    }

    public boolean updateTaskEvents(ArrayList<TaskEvent> taskEvents) {
        for (TaskEvent taskEvent : taskEvents) {
            this.insertTaskEvent(taskEvent);
        }
        return true;
    }

    public ArrayList<TaskEvent> getTaskEvents() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<TaskEvent> taskEvents = new ArrayList<>();

        String query = "Select * FROM " + TaskEventContract.TaskEvent.TABLE_NAME;
        Cursor result = database.rawQuery(query, null);

        try {
            if (result.moveToFirst()) {
                do {
                    taskEvents.add(this.parseTaskEvent(result));
                } while (result.moveToNext());
            }
        } finally {
            result.close();
        }

        Log.i(TAG, taskEvents.size() + " TaskEvents loaded from Database");
        return taskEvents;
    }

    private void findTasksToTaskEvent(TaskEvent taskEvent) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "Select * FROM " + TaskEventContract.TaskEventTask.TABLE_NAME + " WHERE " + TaskEventContract
                .TaskEventTask.COLUMN_NAME_TASK_EVENT_ID + " = " + taskEvent.getId();
        Cursor result = database.rawQuery(query, null);

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

    private void insertTasksFromTaskEvent(TaskEvent taskEvent) {
        SQLiteDatabase database = this.getWritableDatabase();
        for (Task task : taskEvent.getTasks()) {
            ContentValues contentValues = new ContentValues();
            this.getTaskContentValue(contentValues, task);
            contentValues.put(TaskEventContract.TaskEventTask.COLUMN_NAME_TASK_EVENT_ID, taskEvent.getId());

            if (database.replace(TaskEventContract.TaskEventTask.TABLE_NAME, null, contentValues) == -1) {
                Log.e(TAG, "Error at inserting Task from TaskEvent");
            }
        }
    }

    public boolean deactivateTaskEvent(TaskEvent taskEvent) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskEventContract.TaskEvent.COLUMN_NAME_ACTIVE, 0);
        return database.update(TaskEventContract.TaskEvent.TABLE_NAME, contentValues, TaskEventContract.TaskEvent
                .COLUMN_NAME_ID + " = ? ", new String[]{Integer.toString(taskEvent.getId())}) == 1;
    }

    private TaskEvent parseTaskEvent(Cursor cursor) {
        TaskEvent taskEvent = new TaskEvent();
        taskEvent.setMaxTurnTime(cursor.getInt(cursor.getColumnIndex(TaskEventContract.TaskEvent
                .COLUMN_NAME_MAX_TURN_TIME)));
        taskEvent.setMinTurnTime(cursor.getInt(cursor.getColumnIndex(TaskEventContract.TaskEvent
                .COLUMN_NAME_MIN_TURN_TIME)));
        taskEvent.setCurrentTaskLimit(cursor.getInt(cursor.getColumnIndex(TaskEventContract.TaskEvent
                .COLUMN_NAME_CURRENT_TASK_LIMIT)));
        taskEvent.setTasksToEventCounter(cursor.getInt(cursor.getColumnIndex(TaskEventContract.TaskEvent
                .COLUMN_NAME_TASKS_TO_EVENT_COUNTER)));
        taskEvent.setEventCounter(cursor.getInt(cursor.getColumnIndex(TaskEventContract.TaskEvent
                .COLUMN_NAME_EVENT_COUNTER)));
        taskEvent.setActive(cursor.getInt(cursor.getColumnIndex(TaskEventContract.TaskEvent.COLUMN_NAME_ACTIVE)) != 0);
        this.parseTask(cursor, taskEvent);
        this.findTasksToTaskEvent(taskEvent);

        return taskEvent;
    }

    private Task parseTask(Cursor cursor, Task task) {
        task.setId(cursor.getInt(cursor.getColumnIndex(TaskContract.Task.COLUMN_NAME_ID)));
        task.setText(cursor.getString(cursor.getColumnIndex(TaskContract.Task.COLUMN_NAME_TEXT)));
        task.setDrinkCount(cursor.getInt(cursor.getColumnIndex(TaskContract.Task.COLUMN_NAME_DRINK_COUNT)));
        task.setCost(cursor.getInt(cursor.getColumnIndex(TaskContract.Task.COLUMN_NAME_COST)));
        task.setDifficult(TaskDifficult.valueOf(cursor.getString(cursor.getColumnIndex(TaskContract.Task
                .COLUMN_NAME_DIFFICULT))));
        task.setTaskTarget(TaskTarget.valueOf(cursor.getString(cursor.getColumnIndex(TaskContract.Task
                .COLUMN_NAME_TARGET))));
        task.setAlreadyUsed(cursor.getInt(cursor.getColumnIndex(TaskContract.Task.COLUMN_NAME_ALREADY_USED))
                != 0);
        return task;
    }

    private Task parseTask(Cursor cursor) {
        Task task;
        if (TaskTarget.valueOf(cursor.getString(cursor.getColumnIndex(TaskContract.Task.COLUMN_NAME_TARGET))) ==
                TaskTarget.TASK_EVENT) {
            task = new TaskEvent();
        } else {
            task = new Task();
        }
        return this.parseTask(cursor, task);
    }

    public void startNewGame() {
        SQLiteDatabase database = this.getWritableDatabase();
        this.onUpgrade(database, 0, 0);
        for (Task task : TaskDefinitions.getTasks()) {
            if (!this.insertTask(task)) {
                Log.e(TAG, "Error at writing Task: " + task.getText());
            }
        }
        for (MiniGame miniGame : EnumSet.allOf(MiniGame.class)) {
            this.insertMiniGame(miniGame);
        }

        Log.i(TAG, "New Game started");
    }

    public int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    public void activateTaskEvent(TaskEvent taskEvent) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskEventContract.TaskEvent.COLUMN_NAME_ACTIVE, 1);

        if (database.update(TaskEventContract.TaskEvent.TABLE_NAME, contentValues, TaskEventContract.TaskEvent
                .COLUMN_NAME_ID + " = ?", new String[]{Integer.toString(taskEvent.getId())}) != 1) {
            Log.e(TAG, "Error at activating TaskEvent {" + taskEvent + "]");
        }
    }

    public ArrayList<TaskEvent> getActiveTaskEvents() {
        ArrayList<TaskEvent> activeTaskEvents = new ArrayList<>();
        ArrayList<TaskEvent> allTaskEvents = this.getTaskEvents();

        for (TaskEvent taskEvent : allTaskEvents) {
            if (taskEvent.isActive()) {
                activeTaskEvents.add(taskEvent);
            }
        }

        return activeTaskEvents;
    }
}
