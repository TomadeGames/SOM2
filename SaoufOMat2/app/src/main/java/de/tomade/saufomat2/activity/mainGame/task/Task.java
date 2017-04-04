package de.tomade.saufomat2.activity.mainGame.task;

import java.io.Serializable;

/**
 * Created by woors on 10.03.2016.
 */
public class Task implements Serializable {

    private static final long serialVersionUID = 7664080231665909479L;
    private static int nextId = 0;
    private int id;
    private String text;
    private TaskDifficult difficult;
    private int drinkCount;     //Getränkeanzahl bei Ja
    private int cost;           //Getränkeanzahl bei Nein
    private TaskTarget target;
    private boolean alreadyUsed;

    public Task() {

    }

    public Task(String text, TaskDifficult difficult, int drinkCount, int cost, TaskTarget target) {
        this.setId(nextId++);
        this.setText(text);
        this.setDifficult(difficult);
        if (drinkCount < 0) {
            drinkCount = 0;
        }
        this.setDrinkCount(drinkCount);
        if (cost < 0) {
            cost = 0;
        }
        this.setCost(cost);
        this.setTarget(target);
        this.setAlreadyUsed(false);
    }

    public String getText() {
        return this.text;
    }

    public TaskDifficult getDifficult() {
        return this.difficult;
    }

    public int getDrinkCount() {
        return this.drinkCount;
    }

    public int getCost() {
        return this.cost;
    }

    public TaskTarget getTarget() {
        return this.target;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
        if (id > nextId) {
            nextId = id + 1;
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDifficult(TaskDifficult difficult) {
        this.difficult = difficult;
    }

    public void setDrinkCount(int drinkCount) {
        this.drinkCount = drinkCount;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setTarget(TaskTarget target) {
        this.target = target;
    }

    public boolean isAlreadyUsed() {
        return this.alreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }
}
