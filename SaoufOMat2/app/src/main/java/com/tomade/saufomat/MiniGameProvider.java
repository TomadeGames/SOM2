package com.tomade.saufomat;

import android.content.Context;

import com.tomade.saufomat.constant.MiniGame;
import com.tomade.saufomat.persistance.sql.DatabaseHelper;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * Verwaltungsklasse für Minigames
 * Created by woors on 07.03.2017.
 */

public class MiniGameProvider {
    private Context context;

    public MiniGameProvider(Context context) {
        this.context = context;
    }

    public MiniGame getRandomMiniGame() {
        Random random = new Random(System.currentTimeMillis());
        DatabaseHelper databaseHelper = new DatabaseHelper(this.context);
        List<MiniGame> miniGames = databaseHelper.getUnusedMiniGames();
        if (miniGames.isEmpty()) {
            this.resetMiniGames();
            miniGames = databaseHelper.getUnusedMiniGames();
        }

        return miniGames.get(random.nextInt(miniGames.size()));
    }

    private void resetMiniGames() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this.context);
        for (MiniGame miniGame : EnumSet.allOf(MiniGame.class)) {
            databaseHelper.resetMiniGame(miniGame);
        }
    }

}
