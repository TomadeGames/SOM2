package com.tomade.saufomat.activity.mainGame.task;

/**
 * Aufgaben vom Hauptspiel
 * Created by woors on 10.03.2016.
 */
public class Task extends SimpleTask {
    private static final long serialVersionUID = 7664080231665909479L;

    private TaskDifficult difficult;
    /**
     * Getränkeanzahl bei Nein
     */
    private int cost;

    public Task() {
        super();
    }

    public Task(String text, TaskDifficult difficult, int drinkCount, int cost, TaskTarget target) {
        super(text, drinkCount, target);
        if (difficult == TaskDifficult.GAME) {
            throw new IllegalArgumentException("Taskdifficult must not be GAME");
        }
        this.difficult = difficult;
        if (cost < 0) {
            cost = 0;
        }
        this.cost = cost;
    }

    public TaskDifficult getDifficult() {
        return this.difficult;
    }

    /**
     * Gibt die Kosten für eine Aufgabe zurück
     *
     * @return die Kosten der Aufgabe oder 0, wenn sie nicht abgelehnt werden kann
     */
    public int getCost() {
        return this.cost;
    }

    public void setDifficult(TaskDifficult difficult) {
        this.difficult = difficult;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

}
