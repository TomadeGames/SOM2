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
import com.tomade.saufomat.persistance.sql.table.IchHabNochNieTable;
import com.tomade.saufomat.persistance.sql.table.MiniGameTable;
import com.tomade.saufomat.persistance.sql.table.PlayerTable;
import com.tomade.saufomat.persistance.sql.table.TaskEventTable;
import com.tomade.saufomat.persistance.sql.table.TaskTable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Verwaltet Datenbankoperationen
 * Created by woors on 01.04.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "saufomat_database";

    //TODO wenn die Datenbank geändert wird muss dieser Wert inkrementiert werden
    private static final int DATABASE_VERSION = 37;

    private Context context;
    private PlayerTable playerTable;
    private TaskTable taskTable;
    private MiniGameTable miniGameTable;
    private TaskEventTable taskEventTable;
    private IchHabNochNieTable ichHabNochNieTable;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.playerTable = new PlayerTable();
        this.taskTable = new TaskTable();
        this.miniGameTable = new MiniGameTable();
        this.taskEventTable = new TaskEventTable();
        this.ichHabNochNieTable = new IchHabNochNieTable();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.playerTable.createTable(sqLiteDatabase);
        this.taskTable.createTable(sqLiteDatabase);
        this.miniGameTable.createTable(sqLiteDatabase);
        this.taskEventTable.createTable(sqLiteDatabase);
        this.ichHabNochNieTable.createTable(sqLiteDatabase);

        new SaveGameHelper(this.context).saveDatabaseVersion(DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        this.playerTable.deleteTable(database);
        this.taskTable.deleteTable(database);
        this.miniGameTable.deleteTable(database);
        this.taskEventTable.deleteTable(database);
        this.ichHabNochNieTable.deleteTable(database);
        Log.i(TAG, "onUpgrade: Tables dropped");
        this.onCreate(database);
    }

    /**
     * Startet ein neues Spiel und löscht den alten Spielstand
     */
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

    /**
     * Fügt einen Spieler ein
     *
     * @param player der Spieler
     */
    public void insertPlayer(Player player) {
        this.playerTable.insertEntry(this.getWritableDatabase(), player);
    }

    /**
     * Markiert ein Minispiel als gespielt
     *
     * @param miniGame das Minispiel
     */
    public void miniGameUsed(MiniGame miniGame) {
        this.miniGameTable.setMiniGameUsed(this.getWritableDatabase(), miniGame, true);
    }

    /**
     * Markiert ein Minispiel als nicht gespielt
     *
     * @param miniGame das Minispiel
     */
    public void resetMiniGame(MiniGame miniGame) {
        this.miniGameTable.setMiniGameUsed(this.getWritableDatabase(), miniGame, false);
    }

    /**
     * Aktualisiert einen Spieler
     *
     * @param player der Spieler
     */
    public void updatePlayer(Player player) {
        this.playerTable.updateEntry(this.getWritableDatabase(), player);
    }

    /**
     * Aktualisiert eine Aufgabe
     *
     * @param task die Aufgabe
     */
    public void updateTask(Task task) {
        this.taskTable.updateEntry(this.getWritableDatabase(), task);
    }

    /**
     * Gibt alle ungespielten Minispiele zurück, deren minimale Spieleranzahl <= der aktuellen Spielerzahl ist
     *
     * @param playerCount die Spieleranzahl
     * @return alle spielbaren, noch nicht gespielten Minispiele
     */
    public ArrayList<MiniGame> getUnusedMiniGames(int playerCount) {
        return this.miniGameTable.getUnusedMiniGames(this.getReadableDatabase(), playerCount);
    }

    /**
     * Gibt alle Spieler zurück
     *
     * @return alle Spieler
     */
    public ArrayList<Player> getAllPlayer() {
        return this.playerTable.getAllEntries(this.getReadableDatabase());
    }

    /**
     * Gibt alle Aufgaben einer Schwierigkeitsstufe zurück
     *
     * @param difficult die Schwieriegkeitsstufe
     * @return alle Aufgaben einer Schwierigkeitsstufe
     */
    public ArrayList<Task> getAllTasks(TaskDifficult difficult) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Task> taskList = new ArrayList<>();

        taskList.addAll(this.taskTable.getAllTasks(database, difficult));
        taskList.addAll(this.taskEventTable.getAllTasks(database, difficult));
        return taskList;
    }

    /**
     * Gibt alle Aufgaben zurück
     *
     * @return alle Aufgaben
     */
    public ArrayList<Task> getAllTasks() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.addAll(this.taskTable.getAllEntries(database));
        taskList.addAll(this.taskEventTable.getAllEntries(database));
        return taskList;
    }

    /**
     * Gibt alle ungeshenen Aufgaben einer Schweirigkeitsstufe zurück
     *
     * @param difficult die Schweirigkeitsstufe
     * @return alle ungesheen Aufgaben
     */
    public ArrayList<Task> getUnusedTasks(TaskDifficult difficult) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Task> unusedTasks = new ArrayList<>();

        unusedTasks.addAll(this.taskTable.getUnusedTasks(database, difficult));
        unusedTasks.addAll(this.taskEventTable.getUnusedTasks(database, difficult));

        Log.i(TAG, unusedTasks.size() + " unused Tasks with Difficult " + difficult + " loaded from Database");
        return unusedTasks;
    }

    /**
     * Aktualisiert TaskEvents
     *
     * @param taskEvents die TaskEvents
     */
    public void updateTaskEvents(ArrayList<TaskEvent> taskEvents) {
        for (TaskEvent taskEvent : taskEvents) {
            this.insertTaskEvent(taskEvent);
        }
    }

    public int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    /**
     * Aktiviert ein TaskEvent
     *
     * @param taskEvent das TaskEvent
     */
    public void activateTaskEvent(TaskEvent taskEvent) {
        this.taskEventTable.activateTaskEvent(this.getWritableDatabase(), taskEvent);
    }

    /**
     * Deaktiviert ein TaskEvent
     *
     * @param taskEvent das TaskEvent
     */
    public void deactivateTaskEvent(TaskEvent taskEvent) {
        this.taskEventTable.deactivateTaskEvent(this.getWritableDatabase(), taskEvent);
    }

    /**
     * Gibt alle aktiven TaskEvents zurück
     *
     * @return alle aktiven TaskEvents
     */
    public ArrayList<TaskEvent> getActiveTaskEvents() {
        return this.taskEventTable.getActiveTaskEvents(this.getReadableDatabase());
    }

    /**
     * Markiert eine Ich hab noch nie Frage als gelesen
     *
     * @param task die Frage
     */
    public void useIchHabNochNieTask(String task) {
        this.ichHabNochNieTable.useTask(this.getWritableDatabase(), task);
    }

    /**
     * Gibt alle ungelesenen Ich hab noch nie Fragen zurück
     *
     * @return alle ungelesenen Ich hab noch nie Fragen
     */
    public List<String> getUnusedTasks() {
        return this.ichHabNochNieTable.getUnusedTasks(this.getReadableDatabase());
    }

    /**
     * Setzt Ich hab noch nie Fragen zurück
     *
     * @param tasks die Ich hab noch nie Fragen, die zurückgesetzt werden sollen
     */
    public void resetIchHabNochNieTasks(String[] tasks) {
        this.ichHabNochNieTable.resetTasks(this.getWritableDatabase(), tasks);
    }

    private void insertMiniGame(MiniGame miniGame) {
        SQLiteDatabase database = this.getWritableDatabase();
        this.miniGameTable.insertEntry(database, miniGame);
    }

    private void insertTaskEvent(TaskEvent taskEvent) {
        this.taskEventTable.insertEntry(this.getWritableDatabase(), taskEvent);
    }

    private void insertTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        if (task instanceof TaskEvent) {
            this.taskEventTable.insertEntry(database, (TaskEvent) task);
        } else {
            this.taskTable.insertEntry(database, task);
        }
    }
}
