package com.tomade.saufomat.persistance.sql.contract;

import android.provider.BaseColumns;

/**
 * Created by woors on 07.08.2017.
 */

public class TaskEventContract {
    private TaskEventContract() {

    }

    public static class TaskEvent implements BaseColumns {
        public static final String TABLE_NAME = "task_event";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_EVENT_COUNTER = "event_counter";
        public static final String COLUMN_NAME_EVENT_LIMIT = "event_limit";
        public static final String COLUMN_NAME_TASKS_TO_EVENT_COUNTER = "tasks_to_event_counter";
        public static final String COLUMN_NAME_TASKS_TO_EVENT_LIMIT = "tasks_to_event_limit";
    }

}
