package com.tomade.saufomat.persistance.sql.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

import com.tomade.saufomat.model.player.Player;
import com.tomade.saufomat.model.player.Statistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Tabelle für Spieler
 * Created by woors on 05.10.2017.
 */

public class PlayerTable extends BaseTable<Player> {
    private static final String TAG = PlayerTable.class.getSimpleName();
    private static final String TABLE_NAME = "player";
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_WEIGHT = "weight";
    private static final String COLUMN_NAME_IS_MAN = "is_man";
    private static final String COLUMN_NAME_DRINKS = "drinks";
    private static final String COLUMN_NAME_EASY_WINS = "easy_wins";
    private static final String COLUMN_NAME_MEDIUM_WINS = "medium_wins";
    private static final String COLUMN_NAME_HARD_WINS = "hard_wins";
    private static final String COLUMN_NAME_NEXT_PLAYER = "next_player";

    public PlayerTable() {
        super(TABLE_NAME);
    }

    private static final String COLUMN_NAME_LAST_PLAYER = "last_player";

    @Override
    public void createTable(SQLiteDatabase sqLiteDatabase) {
        String statement = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_NAME + " TEXT, " +
                COLUMN_NAME_WEIGHT + " INTEGER, " +
                COLUMN_NAME_IS_MAN + " INTEGER, " +
                COLUMN_NAME_DRINKS + " INTEGER, " +
                COLUMN_NAME_EASY_WINS + " INTEGER, " +
                COLUMN_NAME_MEDIUM_WINS + " INTEGER, " +
                COLUMN_NAME_HARD_WINS + " INTEGER, " +
                COLUMN_NAME_NEXT_PLAYER + " INTEGER, " +
                COLUMN_NAME_LAST_PLAYER + " INTEGER)";

        sqLiteDatabase.execSQL(statement);
        Log.i(TAG, "Table " + TABLE_NAME + " created");
    }

    @Override
    public void insertEntry(SQLiteDatabase sqLiteDatabase, Player player) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_ID, player.getId());
        contentValues.put(COLUMN_NAME_NAME, player.getName());
        contentValues.put(COLUMN_NAME_WEIGHT, player.getWeight());
        if (player.getIsMan()) {
            contentValues.put(COLUMN_NAME_IS_MAN, 1);
        } else {
            contentValues.put(COLUMN_NAME_IS_MAN, 0);
        }
        contentValues.put(COLUMN_NAME_DRINKS, player.getDrinks());
        contentValues.put(COLUMN_NAME_NEXT_PLAYER, player.getNextPlayer().getId());
        contentValues.put(COLUMN_NAME_LAST_PLAYER, player.getLastPlayer().getId());

        contentValues.put(COLUMN_NAME_EASY_WINS, player.getStatistic().getEasyWins());
        contentValues.put(COLUMN_NAME_MEDIUM_WINS, player.getStatistic().getMediumWins());
        contentValues.put(COLUMN_NAME_HARD_WINS, player.getStatistic().getHardWins());

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        Log.i(TAG, "Player [" + player + "] added in Table");
    }

    @Override
    public void updateEntry(SQLiteDatabase sqLiteDatabase, Player player) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_NAME, player.getName());
        contentValues.put(COLUMN_NAME_WEIGHT, player.getWeight());
        if (player.getIsMan()) {
            contentValues.put(COLUMN_NAME_IS_MAN, 1);
        } else {
            contentValues.put(COLUMN_NAME_IS_MAN, 0);
        }
        contentValues.put(COLUMN_NAME_DRINKS, player.getDrinks());
        contentValues.put(COLUMN_NAME_NEXT_PLAYER, player.getNextPlayer().getId());
        contentValues.put(COLUMN_NAME_LAST_PLAYER, player.getLastPlayer().getId());

        contentValues.put(COLUMN_NAME_EASY_WINS, player.getStatistic().getEasyWins());
        contentValues.put(COLUMN_NAME_MEDIUM_WINS, player.getStatistic().getMediumWins());
        contentValues.put(COLUMN_NAME_HARD_WINS, player.getStatistic().getHardWins());

        sqLiteDatabase.update(TABLE_NAME, contentValues, COLUMN_NAME_ID + " = ? ", new String[]{Integer.toString
                (player.getId())});

        Log.i(TAG, "Player [" + player + "] updated");
    }

    @Override
    public ArrayList<Player> getAllEntries(SQLiteDatabase sqLiteDatabase) {
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        SparseArray<Player> playerList = new SparseArray<>();
        Map<Player, Integer> nextPlayer = new HashMap<>();
        Map<Player, Integer> lastPlayer = new HashMap<>();

        if (result.moveToFirst()) {
            do {
                Player player = new Player();
                player.setId(result.getInt(result.getColumnIndex(COLUMN_NAME_ID)));
                player.setName(result.getString(result.getColumnIndex(COLUMN_NAME_NAME)));
                player.setWeight(result.getInt(result.getColumnIndex(COLUMN_NAME_WEIGHT)));
                player.setDrinks(result.getInt(result.getColumnIndex(COLUMN_NAME_DRINKS)));
                player.setIsMan(result.getInt(result.getColumnIndex(COLUMN_NAME_IS_MAN)) !=
                        0);
                nextPlayer.put(player, result.getInt(result.getColumnIndex(COLUMN_NAME_NEXT_PLAYER)));
                lastPlayer.put(player, result.getInt(result.getColumnIndex(COLUMN_NAME_LAST_PLAYER)));

                Statistic statistic = new Statistic();
                statistic.setEasyWins(result.getInt(result.getColumnIndex(COLUMN_NAME_EASY_WINS)));
                statistic.setMediumWins(result.getInt(result.getColumnIndex(COLUMN_NAME_MEDIUM_WINS)));
                statistic.setHardWins(result.getInt(result.getColumnIndex(COLUMN_NAME_HARD_WINS)));

                playerList.put(player.getId(), player);
            } while (result.moveToNext());
            result.close();

            for (Map.Entry<Player, Integer> playerEntry : nextPlayer.entrySet()) {
                playerEntry.getKey().setNextPlayer(playerList.get(playerEntry.getValue()));
            }

            for (Map.Entry<Player, Integer> playerEntry : lastPlayer.entrySet()) {
                playerEntry.getKey().setLastPlayer(playerList.get(playerEntry.getValue()));
            }
        }

        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < playerList.size(); i++) {
            players.add(playerList.get(playerList.keyAt(i)));
        }
        Log.i(TAG, playerList.size() + " Players loaded from Databse");
        return players;
    }
}
