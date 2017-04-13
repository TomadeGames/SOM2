package com.tomade.saufomat.persistance.sql;

import android.provider.BaseColumns;

/**
 * Created by woors on 09.04.2017.
 */

public final class MiniGameContract {
    private MiniGameContract() {

    }

    public static class MiniGame implements BaseColumns {
        public static final String TABLE_NAME = "mini_game";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ALREADY_USED = "already_used";
    }
}
