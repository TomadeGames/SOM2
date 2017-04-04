package de.tomade.saufomat2.persistance.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.tomade.saufomat2.activity.mainGame.task.Task;
import de.tomade.saufomat2.activity.mainGame.task.TaskDifficult;
import de.tomade.saufomat2.activity.mainGame.task.TaskTarget;
import de.tomade.saufomat2.factory.TaskFactory;
import de.tomade.saufomat2.model.Player;

/**
 * Verwaltet Datenbankoperationen
 * Created by woors on 01.04.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "saufomat_database";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + PlayerContract.Player.TABLE_NAME + "(" +
                PlayerContract.Player.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                PlayerContract.Player.COLUMN_NAME_NAME + " TEXT, " +
                PlayerContract.Player.COLUMN_NAME_WEIGHT + " INTEGER, " +
                PlayerContract.Player.COLUMN_NAME_IS_MAN + " INTEGER, " +
                PlayerContract.Player.COLUMN_NAME_DRINKS + " INTEGER, " +
                PlayerContract.Player.COLUMN_NAME_NEXT_PLAYER + " INTEGER, " +
                PlayerContract.Player.COLUMN_NAME_LAST_PLAYER + " INTEGER)"
        );

        String taskStatement = "CREATE TABLE " + TaskContract.Task.TABLE_NAME + "(" +
                TaskContract.Task.COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                TaskContract.Task.COLUMN_NAME_TEXT + " TEXT, " +
                TaskContract.Task.COLUMN_NAME_DRINK_COUNT + " INTEGER, " +
                TaskContract.Task.COLUMN_NAME_COST + " INTEGER, " +
                TaskContract.Task.COLUMN_NAME_DIFFICULT + " TEXT, " +
                TaskContract.Task.COLUMN_NAME_TARGET + " TEXT, " +
                TaskContract.Task.COLUMN_NAME_ALREADY_USED + " INTEGER)";


        sqLiteDatabase.execSQL(taskStatement);

        TaskFactory taskFactory = new TaskFactory();

        for (Task task : taskFactory.getAllTasks()) {
            this.insertTask(task, sqLiteDatabase);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PlayerContract.Player.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TaskContract.Task.TABLE_NAME);

        this.onCreate(sqLiteDatabase);
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
        database.insert(PlayerContract.Player.TABLE_NAME, null, contentValues);
        return true;
    }

    private boolean insertTask(Task task, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.Task.COLUMN_NAME_ID, task.getId());
        contentValues.put(TaskContract.Task.COLUMN_NAME_TEXT, task.getText());
        contentValues.put(TaskContract.Task.COLUMN_NAME_DRINK_COUNT, task.getDrinkCount());
        contentValues.put(TaskContract.Task.COLUMN_NAME_COST, task.getCost());
        contentValues.put(TaskContract.Task.COLUMN_NAME_DIFFICULT, task.getDifficult().toString());
        contentValues.put(TaskContract.Task.COLUMN_NAME_TARGET, task.getTarget().toString());
        if (task.isAlreadyUsed()) {
            contentValues.put(TaskContract.Task.COLUMN_NAME_ALREADY_USED, 1);
        } else {
            contentValues.put(TaskContract.Task.COLUMN_NAME_ALREADY_USED, 0);
        }
        database.insert(TaskContract.Task.TABLE_NAME, null, contentValues);
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

        database.update(PlayerContract.Player.TABLE_NAME, contentValues,
                PlayerContract.Player.COLUMN_NAME_ID + " = ? ", new String[]{Integer.toString(player.getId())});
        return true;
    }

    public boolean updateTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.Task.COLUMN_NAME_TEXT, task.getText());
        contentValues.put(TaskContract.Task.COLUMN_NAME_DRINK_COUNT, task.getDrinkCount());
        contentValues.put(TaskContract.Task.COLUMN_NAME_COST, task.getCost());
        contentValues.put(TaskContract.Task.COLUMN_NAME_DIFFICULT, task.getDifficult().toString());
        contentValues.put(TaskContract.Task.COLUMN_NAME_TARGET, task.getTarget().toString());
        if (task.isAlreadyUsed()) {
            contentValues.put(TaskContract.Task.COLUMN_NAME_ALREADY_USED, 1);
        } else {
            contentValues.put(TaskContract.Task.COLUMN_NAME_ALREADY_USED, 0);
        }
        database.update(TaskContract.Task.TABLE_NAME, contentValues,
                TaskContract.Task.COLUMN_NAME_ID + " = ? ", new String[]{Integer.toString(task.getId())});
        return true;
    }

    public Cursor getTask(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TaskContract.Task.TABLE_NAME + " WHERE " + TaskContract.Task
                .COLUMN_NAME_ID + "=?", new String[]{Integer.toString(id)});
    }

    public ArrayList<Player> getAllPlayer() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor result = database.rawQuery("SELECT * FROM " + PlayerContract.Player.TABLE_NAME, null);
        ArrayList<Player> playerList = new ArrayList<>();
        Map<Player, Integer> nextPlayer = new HashMap<>();
        Map<Player, Integer> lastPlayer = new HashMap<>();
        if (result.moveToFirst()) {
            do {
                Player player = new Player();
                player.setId(result.getInt(result.getColumnIndex(PlayerContract.Player.COLUMN_NAME_ID)));
                player.setName(result.getString(result.getColumnIndex(PlayerContract.Player.COLUMN_NAME_NAME)));
                player.setWeight(result.getInt(result.getColumnIndex(PlayerContract.Player.COLUMN_NAME_WEIGHT)));
                player.setDrinks(result.getInt(result.getColumnIndex(PlayerContract.Player.COLUMN_NAME_DRINKS)));
                player.setIsMan(result.getInt(result.getColumnIndex(PlayerContract.Player.COLUMN_NAME_IS_MAN)) != 0);
                nextPlayer.put(player, result.getInt(result.getColumnIndex(PlayerContract.Player
                        .COLUMN_NAME_NEXT_PLAYER)));
                lastPlayer.put(player, result.getInt(result.getColumnIndex(PlayerContract.Player
                        .COLUMN_NAME_LAST_PLAYER)));
                playerList.add(player.getId(), player);
            } while (result.moveToNext());

            for (Map.Entry<Player, Integer> playerEntry : nextPlayer.entrySet()) {
                playerEntry.getKey().setNextPlayer(playerList.get(playerEntry.getValue()));
            }

            for (Map.Entry<Player, Integer> playerEntry : lastPlayer.entrySet()) {
                playerEntry.getKey().setLastPlayer(playerList.get(playerEntry.getValue()));
            }
        }
        result.close();
        return playerList;
    }

    public ArrayList<Task> getAllTasks() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor result = database.rawQuery("SELECT * FROM " + TaskContract.Task.TABLE_NAME, null);

        ArrayList<Task> taskList = new ArrayList<>();

        if (result.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(result.getInt(result.getColumnIndex(TaskContract.Task.COLUMN_NAME_ID)));
                task.setText(result.getString(result.getColumnIndex(TaskContract.Task.COLUMN_NAME_TEXT)));
                task.setDrinkCount(result.getInt(result.getColumnIndex(TaskContract.Task.COLUMN_NAME_DRINK_COUNT)));
                task.setCost(result.getInt(result.getColumnIndex(TaskContract.Task.COLUMN_NAME_COST)));
                task.setDifficult(TaskDifficult.valueOf(result.getString(result.getColumnIndex(TaskContract.Task
                        .COLUMN_NAME_DIFFICULT))));
                task.setTarget(TaskTarget.valueOf(result.getString(result.getColumnIndex(TaskContract.Task
                        .COLUMN_NAME_TARGET))));
                task.setAlreadyUsed(result.getInt(result.getColumnIndex(TaskContract.Task.COLUMN_NAME_ALREADY_USED))
                        != 0);
                taskList.add(task);
            } while (result.moveToNext());
        }
        result.close();
        return taskList;
    }

    public void startNewGame() {
        this.onUpgrade(this.getWritableDatabase(), 0, 0);
    }
}
