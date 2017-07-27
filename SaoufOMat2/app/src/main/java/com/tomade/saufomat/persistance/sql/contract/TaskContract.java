package com.tomade.saufomat.persistance.sql.contract;

import android.provider.BaseColumns;

/**
 * Verarbeitet Aufgaben in der SQLite Datenbank
 * Created by woors on 01.04.2017.
 */

public final class TaskContract {
    private TaskContract() {
    }

    public static class Task implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_DIFFICULT = "difficult";
        public static final String COLUMN_NAME_DRINK_COUNT = "drink_count";     //Getränkeanzahl bei Ja
        public static final String COLUMN_NAME_COST = "cost";           //Getränkeanzahl bei Nein
        public static final String COLUMN_NAME_TARGET = "target";
        public static final String COLUMN_NAME_ALREADY_USED = "already_used";
    }

}