package com.tomade.saufomat.persistance;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.tomade.saufomat.constant.SharedPreferencesFileKey;
import com.tomade.saufomat.model.player.Player;
import com.tomade.saufomat.persistance.sql.DatabaseHelper;

import java.util.List;

/**
 * Klasse, die sich um den Key-Value Speicher für den Aktuellen Spielstand kümmert
 * Created by woors on 04.04.2017.
 */

public class SaveGameHelper {
    private static final String TAG = SaveGameHelper.class.getSimpleName();
    private static final String AD_COUNTER_KEY = "ad_counter";
    private static final String CURRENT_PLAYER_KEY = "current_player";
    private static final String IS_GAME_SAVED_KEY = "is_game_saved";
    private static final String GAME_VERSION_KEY = "game_version";
    private static final String DATABASE_VERSION_KEY = "database_version";

    private interface WerfDichDicht {
        String GLASS0_STATE = "glass0state";
        String GLASS1_STATE = "glass1state";
        String GLASS2_STATE = "glass2state";
        String GLASS3_STATE = "glass3state";
        String GLASS4_STATE = "glass4state";
        String GLASS5_STATE = "glass5state";
    }

    //TODO: wenn etwas an den gespeicherten Spielen geändert wird (egal ob Datenbank oder Key-Value-Speicher), muss
    // dieser Wert erhöht werden
    private static final int GAME_VERSION = 4;

    private Context context;
    private SharedPreferences sharedPreferences;

    public SaveGameHelper(Context context) {
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences(SharedPreferencesFileKey
                .SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
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

    public void saveWerfDichDicht(boolean[] isGlassFull) {
        if (isGlassFull.length != 6) {
            throw new IllegalArgumentException("isGlassFull must have 6 entries");
        }
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(WerfDichDicht.GLASS0_STATE, isGlassFull[0]);
        editor.putBoolean(WerfDichDicht.GLASS1_STATE, isGlassFull[1]);
        editor.putBoolean(WerfDichDicht.GLASS2_STATE, isGlassFull[2]);
        editor.putBoolean(WerfDichDicht.GLASS3_STATE, isGlassFull[3]);
        editor.putBoolean(WerfDichDicht.GLASS4_STATE, isGlassFull[4]);
        editor.putBoolean(WerfDichDicht.GLASS5_STATE, isGlassFull[5]);
        editor.apply();
        Log.d(TAG, "Werf dich dicht saved [" +
                isGlassFull[0] + ", " +
                isGlassFull[1] + ", " +
                isGlassFull[2] + ", " +
                isGlassFull[3] + ", " +
                isGlassFull[4] + ", " +
                isGlassFull[5] + "]");
    }

    public boolean[] getSavedWerfDichDichtState() {
        boolean[] werfDichDichtState = new boolean[6];
        werfDichDichtState[0] = this.sharedPreferences.getBoolean(WerfDichDicht.GLASS0_STATE, false);
        werfDichDichtState[1] = this.sharedPreferences.getBoolean(WerfDichDicht.GLASS1_STATE, false);
        werfDichDichtState[2] = this.sharedPreferences.getBoolean(WerfDichDicht.GLASS2_STATE, false);
        werfDichDichtState[3] = this.sharedPreferences.getBoolean(WerfDichDicht.GLASS3_STATE, false);
        werfDichDichtState[4] = this.sharedPreferences.getBoolean(WerfDichDicht.GLASS4_STATE, false);
        werfDichDichtState[5] = this.sharedPreferences.getBoolean(WerfDichDicht.GLASS5_STATE, false);

        Log.d(TAG, "Werf dich dicht loaded [" +
                werfDichDichtState[0] + ", " +
                werfDichDichtState[1] + ", " +
                werfDichDichtState[2] + ", " +
                werfDichDichtState[3] + ", " +
                werfDichDichtState[4] + ", " +
                werfDichDichtState[5] + "]");
        return werfDichDichtState;
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
        Log.d(TAG, "Loaded Game version is " + loadedGameVersion + " current GameVersion is " + GAME_VERSION);
        if (loadedGameVersion == GAME_VERSION && new DatabaseHelper(this.context).getDatabaseVersion() == this
                .getDatabseVersion()) {
            return this.sharedPreferences.getBoolean(IS_GAME_SAVED_KEY, false);
        }
        Log.i(TAG, "Loaded Game version [" + loadedGameVersion + "] is not equal current version [" +
                GAME_VERSION + "] or Database is not valid");
        return false;
    }

    public void saveDatabaseVersion(int databaseVersion) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(DATABASE_VERSION_KEY, databaseVersion);
        editor.apply();
        Log.d(TAG, "Database version saved [" + databaseVersion + "]");
    }

    public int getDatabseVersion() {
        int databaseVersion = this.sharedPreferences.getInt(DATABASE_VERSION_KEY, -1);
        Log.d(TAG, "Loaded Database version is " + databaseVersion);
        return databaseVersion;
    }

    public void clearGame() {
        this.saveWerfDichDicht(new boolean[6]);
        this.saveGameSaved(false);
    }
}