package com.tomade.saufomat.activity.mainGame.taskevent;

import java.io.Serializable;

/**
 * Created by woors on 07.08.2017.
 */

public class TaskEvent implements Serializable {
    private static final long serialVersionUID = 8372172785821956260L;
    private static int nextId = 0;
    private int id;
    private int tasksToEventCounter;
    private int tasksToEventLimit;
    private int eventCounter;
    private TaskEventType type;

    public TaskEvent() {
        this.id = nextId;
        nextId++;
        this.tasksToEventCounter = 0;
        this.eventCounter = 0;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTasksToEventCounter() {
        return this.tasksToEventCounter;
    }

    public void setTasksToEventCounter(int tasksToEventCounter) {
        this.tasksToEventCounter = tasksToEventCounter;
    }

    public int getTasksToEventLimit() {
        return this.tasksToEventLimit;
    }

    public void setTasksToEventLimit(int tasksToEventLimit) {
        this.tasksToEventLimit = tasksToEventLimit;
    }

    public int getEventCounter() {
        return this.eventCounter;
    }

    public void setEventCounter(int eventCounter) {
        this.eventCounter = eventCounter;
    }

    public TaskEventType getType() {
        return this.type;
    }

    public void setType(TaskEventType type) {
        this.type = type;
    }

    public void increaseEventCounter() {
        this.eventCounter++;
    }

    public void increaseTaskToEventCounter() {
        this.tasksToEventCounter++;
    }
}
