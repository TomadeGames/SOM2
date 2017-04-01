package de.tomade.saufomat2.sql;

import android.provider.BaseColumns;

/**
 * Verarbeitet Spieler in der SQLite Datenbank
 * Created by woors on 01.04.2017.
 */

public final class PlayerContract {
    private PlayerContract() {
    }

    public static class Player implements BaseColumns {
        public static final String TABLE_NAME = "player";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_IS_MAN = "isMan";
        public static final String COLUMN_NAME_DRINKS = "drinks";
        public static final String COLUMN_NAME_NEXT_PLAYER = "next_player";
        public static final String COLUMN_NAME_LAST_PLAYER = "last_player";
    }
}
