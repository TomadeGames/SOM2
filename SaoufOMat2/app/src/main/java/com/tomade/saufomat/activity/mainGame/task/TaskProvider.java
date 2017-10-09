package com.tomade.saufomat.activity.mainGame.task;

import android.content.Context;

import com.tomade.saufomat.persistance.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by woors on 09.04.2017.
 */

public class TaskProvider {
    /**
     * Für Debug-Zwecke kann hier eine Schwierigkeit für alle Aufgaben festgelegt werden.
     * Standard ist NULL
     */
    private static final TaskDifficult DEBUG_DIFFICULT = null;

    private final Context context;
    private final Random random = new Random(System.currentTimeMillis());

    public TaskProvider(Context context) {
        this.context = context;
    }

    public Task getNextTask(TaskDifficult difficult) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this.context);
        ArrayList<Task> tasks;
        if (DEBUG_DIFFICULT == null) {
            tasks = databaseHelper.getUnusedIchHabNochNieTasks(difficult);
        } else {
            tasks = databaseHelper.getUnusedIchHabNochNieTasks(DEBUG_DIFFICULT);
        }
        if (tasks.isEmpty()) {
            if (DEBUG_DIFFICULT == null) {
                this.resetTasks(difficult);
                tasks = databaseHelper.getUnusedIchHabNochNieTasks(difficult);
            } else {
                this.resetTasks(DEBUG_DIFFICULT);
                tasks = databaseHelper.getUnusedIchHabNochNieTasks(DEBUG_DIFFICULT);
            }
        }

        Task currentTask = tasks.get(this.random.nextInt(tasks.size()));
        currentTask.setAlreadyUsed(true);
        databaseHelper.updateTask(currentTask);
        return currentTask;
    }

    public void resetTasks() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this.context);
        ArrayList<Task> tasks = databaseHelper.getAllTasks();
        for (Task task : tasks) {
            task.setAlreadyUsed(false);
            databaseHelper.updateTask(task);
        }
    }

    private void resetTasks(TaskDifficult difficult) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this.context);
        ArrayList<Task> tasks = databaseHelper.getAllTasks(difficult);
        for (Task task : tasks) {
            task.setAlreadyUsed(false);
            databaseHelper.updateTask(task);
        }
    }
}
