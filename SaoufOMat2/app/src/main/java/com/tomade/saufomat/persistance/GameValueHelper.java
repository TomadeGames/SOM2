package com.tomade.saufomat.persistance;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.tomade.saufomat.model.Player;
import com.tomade.saufomat.persistance.sql.DatabaseHelper;

import java.util.List;

/**
 * Speiechert für Werte als Key-, Value-Pairs und ließt diese wieder aus
 * Created by woors on 04.04.2017.
 */

public class GameValueHelper {
    private static final String TAG = GameValueHelper.class.getSimpleName();
    private static final String FILE_KEY = "de.tomade.saufomat.PREFERENCE_FILE_KEY";
    private static final String AD_COUNTER_KEY = "ad_counter";
    private static final String CURRENT_PLAYER_KEY = "current_player";
    private static final String IS_GAME_SAVED_KEY = "is_game_saved";
    private static final String GAME_VERSION_KEY = "game_version";

    //TODO: wenn etwas an den gespeicherten Spielen geändert wird, muss dieser Wert erhöht werden
    private static final int GAME_VERSION = 1;

    private Context context;
    private SharedPreferences sharedPreferences;

    public GameValueHelper(Context context) {
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
    }

    public void saveAdCounter(int adCounter) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(AD_COUNTER_KEY, adCounter);
        editor.apply();
        Log.d(TAG, "AdCounter [" + adCounter + "] saved");
    }

    public void saveCurrentPlayer(Player currentPlayer) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(CURRENT_PLAYER_KEY, currentPlayer.getId());
        editor.apply();
        Log.d(TAG, "CurrentPlayer [" + currentPlayer + "] saved");
    }

    public void saveGameSaved(boolean gameSaved) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(IS_GAME_SAVED_KEY, gameSaved);
        editor.putInt(GAME_VERSION_KEY, GAME_VERSION);
        editor.apply();
        Log.d(TAG, "GameSaved [" + gameSaved + "] saved");
    }

    public int getAdCounter() {
        return this.sharedPreferences.getInt(AD_COUNTER_KEY, 0);
    }

    public Player getCurrentPlayer() {
        int currentPlayerId = this.sharedPreferences.getInt(CURRENT_PLAYER_KEY, -1);
        List<Player> allPlayer = new DatabaseHelper(this.context).getAllPlayer();
        for (Player player : allPlayer) {
            if (player.getId() == currentPlayerId) {
                return player;
            }
        }
        return allPlayer.get(0);
    }

    public boolean isGameSaved() {
        int loadedGameVersion = this.sharedPreferences.getInt(GAME_VERSION_KEY, -1);
        if (loadedGameVersion < GAME_VERSION) {
            Log.i(TAG, "Loaded Game version is to low (Loaded Version: " + loadedGameVersion + ", current Version: "
                    + GAME_VERSION + ")");
            return false;
        }
        return this.sharedPreferences.getBoolean(IS_GAME_SAVED_KEY, false);
    }
}
