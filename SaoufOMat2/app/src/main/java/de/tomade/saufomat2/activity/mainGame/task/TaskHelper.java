package de.tomade.saufomat2.activity.mainGame.task;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

import de.tomade.saufomat2.persistance.sql.DatabaseHelper;

/**
 * Created by woors on 09.04.2017.
 */

public class TaskHelper {
    private final Context context;
    private final Random random = new Random(System.currentTimeMillis());

    public TaskHelper(Context context) {
        this.context = context;
    }

    public Task getNextTask(TaskDifficult difficult) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this.context);
        ArrayList<Task> tasks = databaseHelper.getUnusedTasks(difficult);
        if (tasks.isEmpty()) {
            this.resetTasks(difficult);
            tasks = databaseHelper.getUnusedTasks(difficult);
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
