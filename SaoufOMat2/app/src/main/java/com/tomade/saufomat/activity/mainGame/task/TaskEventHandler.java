package com.tomade.saufomat.activity.mainGame.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woors on 12.04.2017.
 */

public class TaskEventHandler {
    private TaskEventHandler() {

    }

    private static List<Task> tasksWithEvents = new ArrayList<>();

    public static void addTaskWithEvent(Task task) {
        tasksWithEvents.add(task);
    }

    public static void increaseTurnCount() {
        for (Task task : tasksWithEvents) {
            task.decreaseSpecialEventCounter();
        }
    }
}
