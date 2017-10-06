package com.tomade.saufomat.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import com.tomade.saufomat.constant.MiniGame;
import com.tomade.saufomat.constant.SharedPreferencesFileKey;

/**
 * Klasse, die sich für Allgemeine Werte im Key-Value Speicher kümmert
 * Created by woors on 06.10.2017.
 */

public class GameValueHelper {
    private Context context;
    private SharedPreferences sharedPreferences;

    public GameValueHelper(Context context) {
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences(SharedPreferencesFileKey
                .SHARED_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);
    }

    public void setTutorialSeen(MiniGame miniGame) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(this.generateMiniGameTutorialKey(miniGame), true);
        editor.apply();
    }

    public boolean isTutorialSeen(MiniGame miniGame) {
        return this.sharedPreferences.getBoolean(this.generateMiniGameTutorialKey(miniGame), false);
    }

    public void resetAllTutorials() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        for (MiniGame miniGame : MiniGame.values()) {
            editor.putBoolean(this.generateMiniGameTutorialKey(miniGame), false);
        }
        editor.apply();
    }

    private String generateMiniGameTutorialKey(MiniGame miniGame) {
        return this.context.getString(miniGame.getNameId()) + "_tutorial";
    }
}
