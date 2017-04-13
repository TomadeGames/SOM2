package com.tomade.saufomat.builder;

import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.TaskTarget;

/**
 * Builderklasse zur erstellung von Aufgaben
 * Created by woors on 12.04.2017.
 */

public class TaskBuilder {
    private Task task;

    public TaskBuilder(String text, TaskDifficult difficult, TaskTarget taskTarget) {
        this.task = new Task();
        this.task.setId();
        this.task.setText(text);
        this.task.setDifficult(difficult);
        this.task.setTarget(taskTarget);
        this.task.setDrinkCount(0);
        this.task.setCost(0);
        this.task.setAlreadyUsed(false);
    }

    public TaskBuilder withCost(int cost) {
        this.task.setCost(cost);
        return this;
    }

    public TaskBuilder withDrinkCount(int drinkCount) {
        this.task.setDrinkCount(drinkCount);
        return this;
    }

    public TaskBuilder withAlreadyUsed(boolean alreadyUsed) {
        this.task.setAlreadyUsed(alreadyUsed);
        return this;
    }

    public TaskBuilder withSpecialEvent(int turnCount) {
        this.task.setSpecialEventCounter(turnCount);
        return this;
    }

    public Task build() {
        return this.task;
    }
}
