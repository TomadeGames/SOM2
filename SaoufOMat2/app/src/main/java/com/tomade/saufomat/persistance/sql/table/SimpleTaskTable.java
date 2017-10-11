package com.tomade.saufomat.persistance.sql.table;

import android.support.annotation.CallSuper;

import com.tomade.saufomat.activity.mainGame.task.SimpleTask;

/**
 * Created by woors on 11.10.2017.
 */

public abstract class SimpleTaskTable<ENTRY extends SimpleTask> extends BasePOJOTable<ENTRY> {
    protected static final String COLUMN_NAME_ID = "id";
    protected static final String COLUMN_NAME_TEXT = "text";
    protected static final String COLUMN_NAME_DRINK_COUNT = "drink_count";
    protected static final String COLUMN_NAME_TARGET = "target";
    protected static final String COLUMN_NAME_ALREADY_USED = "already_used";

    protected SimpleTaskTable(String tableName) {
        super(tableName);
    }

    @CallSuper
    protected String getColumnsForSimpleTaskCreateStatement() {
        return COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_TEXT + " TEXT, " +
                COLUMN_NAME_DRINK_COUNT + " INTEGER, " +
                COLUMN_NAME_TARGET + " TEXT, " +
                COLUMN_NAME_ALREADY_USED + " INTEGER";
    }

    @CallSuper
    @Override
    protected String getColumnsForCreateStatement() {
        return COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_TEXT + " TEXT, " +
                COLUMN_NAME_DRINK_COUNT + " INTEGER, " +
                COLUMN_NAME_TARGET + " TEXT, " +
                COLUMN_NAME_ALREADY_USED + " INTEGER";
    }
}
