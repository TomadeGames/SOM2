package com.tomade.saufomat.persistance.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDefinitions;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.taskevent.TaskEvent;
import com.tomade.saufomat.constant.MiniGame;
import com.tomade.saufomat.model.player.Player;
import com.tomade.saufomat.persistance.SaveGameHelper;
import com.tomade.saufomat.persistance.sql.table.MiniGameTable;
import com.tomade.saufomat.persistance.sql.table.PlayerTable;
import com.tomade.saufomat.persistance.sql.table.TaskEventTable;
import com.tomade.saufomat.persistance.sql.table.TaskTable;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Verwaltet Datenbankoperationen
 * Created by woors on 01.04.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "saufomat_database";

    //TODO wenn die Datenbank geändert wird muss dieser Wert inkrementiert werden
    private static final int DATABASE_VERSION = 35;

    private Context context;
    private PlayerTable playerTable;
    private TaskTable taskTable;
    private MiniGameTable miniGameTable;
    private TaskEventTable taskEventTable;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.playerTable = new PlayerTable();
        this.taskTable = new TaskTable();
        this.miniGameTable = new MiniGameTable();
        this.taskEventTable = new TaskEventTable();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.playerTable.createTable(sqLiteDatabase);
        this.taskTable.createTable(sqLiteDatabase);
        this.miniGameTable.createTable(sqLiteDatabase);
        this.taskEventTable.createTable(sqLiteDatabase);

        new SaveGameHelper(this.context).saveDatabaseVersion(DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        this.playerTable.deleteTable(database);
        this.taskTable.deleteTable(database);
        this.miniGameTable.deleteTable(database);
        this.taskEventTable.deleteTable(database);
        Log.i(TAG, "onUpgrade: Tables dropped");
        this.onCreate(database);
    }

    private void insertMiniGame(MiniGame miniGame) {
        SQLiteDatabase database = this.getWritableDatabase();
        this.miniGameTable.insertEntry(database, miniGame);
    }

    public void insertPlayer(Player player) {
        SQLiteDatabase database = this.getWritableDatabase();
        this.playerTable.insertEntry(database, player);
    }

    private void insertTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        if (task instanceof TaskEvent) {
            this.taskEventTable.insertEntry(database, (TaskEvent) task);
        } else {
            this.taskTable.insertEntry(database, task);
        }
    }

    public void miniGameUsed(MiniGame miniGame) {
        SQLiteDatabase database = this.getWritableDatabase();
        this.miniGameTable.setMiniGameUsed(database, miniGame, true);
    }

    public void resetMiniGame(MiniGame miniGame) {
        SQLiteDatabase database = this.getWritableDatabase();
        this.miniGameTable.setMiniGameUsed(database, miniGame, false);
    }

    public void updatePlayer(Player player) {
        SQLiteDatabase database = this.getWritableDatabase();
        this.playerTable.updateEntry(database, player);
    }

    public void updateTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        this.taskTable.updateEntry(database, task);
    }


    public ArrayList<MiniGame> getUnusedMiniGames() {
        SQLiteDatabase database = this.getReadableDatabase();
        return this.miniGameTable.getUnusedMiniGames(database);
    }

    public ArrayList<Player> getAllPlayer() {
        SQLiteDatabase database = this.getReadableDatabase();
        return this.playerTable.getAllEntries(database);
    }

    public ArrayList<Task> getAllTasks(TaskDifficult difficult) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Task> taskList = new ArrayList<>();

        taskList.addAll(this.taskTable.getAllTasks(database, difficult));
        taskList.addAll(this.taskEventTable.getAllTasks(database, difficult));
        return taskList;
    }

    public ArrayList<Task> getAllTasks() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.addAll(this.taskTable.getAllEntries(database));
        taskList.addAll(this.taskEventTable.getAllEntries(database));
        return taskList;
    }

    public ArrayList<Task> getUnusedTasks(TaskDifficult difficult) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Task> unusedTasks = new ArrayList<>();

        unusedTasks.addAll(this.taskTable.getUnusedTasks(database, difficult));
        unusedTasks.addAll(this.taskEventTable.getUnusedTasks(database, difficult));

        Log.i(TAG, unusedTasks.size() + " unused Tasks with Difficult " + difficult + " loaded from Database");
        return unusedTasks;
    }

    private void insertTaskEvent(TaskEvent taskEvent) {
        SQLiteDatabase database = this.getWritableDatabase();
        this.taskEventTable.insertEntry(database, taskEvent);
    }

    public void updateTaskEvents(ArrayList<TaskEvent> taskEvents) {
        for (TaskEvent taskEvent : taskEvents) {
            this.insertTaskEvent(taskEvent);
        }
    }

    public void deactivateTaskEvent(TaskEvent taskEvent) {
        SQLiteDatabase database = this.getWritableDatabase();
        this.taskEventTable.deactivateTaskEvent(database, taskEvent);
    }


    public void startNewGame() {
        SQLiteDatabase database = this.getWritableDatabase();
        this.onUpgrade(database, 0, 0);
        for (Task task : TaskDefinitions.getTasks()) {
            this.insertTask(task);
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
        this.taskEventTable.activateTaskEvent(database, taskEvent);
    }

    public ArrayList<TaskEvent> getActiveTaskEvents() {
        SQLiteDatabase database = this.getReadableDatabase();
        return this.taskEventTable.getActiveTaskEvents(database);
    }
}
