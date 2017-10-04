package com.tomade.saufomat.persistance.sql.contract;

import android.provider.BaseColumns;

/**
 * Verarbeitet TaskEvents in der SQLite Datenbank
 * Created by woors on 07.08.2017.
 */

public class TaskEventContract {
    private TaskEventContract() {

    }

    public static class TaskEvent extends TaskContract.Task implements BaseColumns {
        public static final String TABLE_NAME = "task_event";
        public static final String COLUMN_NAME_MAX_TURN_TIME = "max_turn_time";
        public static final String COLUMN_NAME_MIN_TURN_TIME = "min_turn_time";
        public static final String COLUMN_NAME_CURRENT_TASK_LIMIT = "current_task_limit";
        public static final String COLUMN_NAME_TASKS_TO_EVENT_COUNTER = "tasks_to_event_counter";
        public static final String COLUMN_NAME_EVENT_COUNTER = "event_counter";
        public static final String COLUMN_NAME_ACTIVE = "active";
    }

    public static class TaskEventTask extends TaskContract.Task implements BaseColumns {
        public static final String TABLE_NAME = "task_event_task";
        public static final String COLUMN_NAME_TASK_EVENT_ID = "task_event_id";
    }
}
