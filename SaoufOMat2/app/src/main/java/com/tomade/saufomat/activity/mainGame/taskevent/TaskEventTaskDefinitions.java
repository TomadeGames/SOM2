package com.tomade.saufomat.activity.mainGame.taskevent;

import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.TaskTarget;

import java.util.HashMap;
import java.util.Map;

/**
 * Definition der Aufgaben zu den TaskEvents
 * Created by woors on 07.08.2017.
 */

public class TaskEventTaskDefinitions {
    private static Map<TaskEventType, Task[]> taskEventTasks = new HashMap<>();

    public static Task getTask(TaskEventType eventType, int eventCount) {
        if (taskEventTasks.isEmpty()) {
            initTaskEventTasks();
        }
        return taskEventTasks.get(eventType)[eventCount];
    }

    public static int getTaskAmount(TaskEventType taskEventType) {
        if (taskEventTasks.isEmpty()) {
            initTaskEventTasks();
        }
        return taskEventTasks.get(taskEventType).length;
    }

    private static void initTaskEventTasks() {
        taskEventTasks.put(TaskEventType.GLAS_IN_THE_MIDDLE, new Task[]{
                new Task("Fülle das Glas in der Mitte mit 2cl eines beliebigen Getränks", TaskDifficult.EASY, 0, 0,
                        TaskTarget.EVENT),
                new Task("Fülle das Glas in der Mitte mit 2cl eines beliebigen Getränks", TaskDifficult.EASY, 0, 0,
                        TaskTarget.EVENT),
                new Task("Fülle das Glas in der Mitte mit 2cl eines beliebigen Getränks", TaskDifficult.EASY, 0, 0,
                        TaskTarget.EVENT),
                new Task("Leere das Glas in der Mitte", TaskDifficult.EASY, 1, 0, TaskTarget.EVENT)});
    }
}
