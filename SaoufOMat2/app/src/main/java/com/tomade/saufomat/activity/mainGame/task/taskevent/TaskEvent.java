package com.tomade.saufomat.activity.mainGame.task.taskevent;

import android.util.Log;

import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.TaskTarget;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by woors on 07.08.2017.
 */

public class TaskEvent extends Task implements Serializable {
    private static final String TAG = TaskEvent.class.getSimpleName();
    private static final long serialVersionUID = 8372172785821956260L;

    private Task[] tasks;
    private int minTurnTime;
    private int maxTurnTime;
    private int currentTaskLimit;

    private int tasksToEventCounter;
    private int eventCounter;
    private boolean active;

    public TaskEvent() {
        super();
        this.setTasksToEventCounter(0);
        this.setEventCounter(0);
        this.alreadyUsed = true;
    }

    public TaskEvent(String text, TaskDifficult difficult, int drinkCount, int cost, TaskTarget target,
                     Task[] tasks, int minTurnTime, int maxTurnTime) {
        super(text, difficult, drinkCount, cost, target);
        if (maxTurnTime < minTurnTime) {
            throw new IllegalArgumentException("maxTurnTime must be higher or equal than minTurnTime: maxTurnTime: "
                    + maxTurnTime + ", minTurnTime: " + minTurnTime);
        }
        this.setTasks(tasks);
        this.setMinTurnTime(minTurnTime);
        this.setMaxTurnTime(maxTurnTime);
        this.setCurrentTaskLimit(this.getNextCurrentTaskLimit());
    }

    public int getTasksToEventCounter() {
        return this.tasksToEventCounter;
    }

    public void setTasksToEventCounter(int tasksToEventCounter) {
        this.tasksToEventCounter = tasksToEventCounter;
    }

    public int getEventCounter() {
        return this.eventCounter;
    }

    public void setEventCounter(int eventCounter) {
        this.eventCounter = eventCounter;
    }

    /**
     * Zählt den Rundenzähler zum nächsten Event hoch.
     */
    public void increaseTaskToEventCounter() {
        this.tasksToEventCounter++;
    }

    private int getNextCurrentTaskLimit() {
        if (this.maxTurnTime == this.minTurnTime) {
            return this.maxTurnTime;
        }
        return new Random(System.currentTimeMillis()).nextInt(this.maxTurnTime - this.minTurnTime) + this.minTurnTime;
    }

    /**
     * Gibt die Aktuelle Aufgabe zurück, wenn das Event gefeuert wurde. Sonst NULL
     *
     * @return das gefeuerte Event oder NULL
     */
    public boolean checkIfEventFired() {
        if (this.tasksToEventCounter >= this.currentTaskLimit) {
            this.currentTaskLimit = this.getNextCurrentTaskLimit();
            this.tasksToEventCounter = 0;
            Log.d(TAG, "TaskEvent fired [" + this.eventCounter + "/" + this.tasks.length + "] Tasks to next Event:" +
                    this.currentTaskLimit);
            return true;
        }
        return false;
    }

    public Task fireEvent() {
        return this.tasks[this.eventCounter++];
    }

    public boolean isFinished() {
        return this.eventCounter >= this.tasks.length;
    }

    public Task[] getTasks() {
        return this.tasks;
    }

    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }

    public int getMinTurnTime() {
        return this.minTurnTime;
    }

    public void setMinTurnTime(int minTurnTime) {
        this.minTurnTime = minTurnTime;
    }

    public int getMaxTurnTime() {
        return this.maxTurnTime;
    }

    public void setMaxTurnTime(int maxTurnTime) {
        this.maxTurnTime = maxTurnTime;
    }

    public int getCurrentTaskLimit() {
        return this.currentTaskLimit;
    }

    public void setCurrentTaskLimit(int currentTaskLimit) {
        this.currentTaskLimit = currentTaskLimit;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
