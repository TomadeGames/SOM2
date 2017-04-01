package de.tomade.saufomat2.sql;

import android.provider.BaseColumns;

/**
 * Verwaltet Spielvariabeln in der SQLite Datenbank
 * Created by woors on 01.04.2017.
 */

public final class GameVariablesContract {
    private GameVariablesContract() {

    }

    public static class GameVariables implements BaseColumns {
        public static final String TABLE_NAME = "game_variables";
        public static final String COLUMN_NAME = "ad_counter";
        public static final String COLUMN_NAME_CURRENT_PLAYER = "current_player";
    }
}
