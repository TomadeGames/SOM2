package com.tomade.saufomat.activity.mainGame.task;

/**
 * Aufgabe, die einen Countdown auslöst
 * Created by woors on 11.10.2017.
 */

public class TimedTask extends Task {
    private static final long serialVersionUID = -8112325314143065754L;

    private SimpleTask taskIfWon;
    private SimpleTask taskIfLost;
    private long time;

    /**
     * Konstruktor
     *
     * @param text       Aufgabentext
     * @param difficult  Schwierigkeitsstufe
     * @param cost       Anzahl Getränke wenn diese Aufgabe abgelehnt wurde
     * @param target     das Ziel
     * @param taskIfWon  Aufgabe, wenn diese Aufgabe geschafft wurde
     * @param taskIfLost Aufgabe, wenn diese Aufgabe gescheitert ist
     * @param time       die Zeit der Aufgabe in Millisekunden
     */
    public TimedTask(String text, TaskDifficult difficult, int cost, TaskTarget target, SimpleTask
            taskIfWon, SimpleTask taskIfLost, long time) {
        super(text, difficult, 0, cost, target);
        this.taskIfWon = taskIfWon;
        this.taskIfLost = taskIfLost;
        this.time = time;
    }

    public TimedTask() {
    }

    public SimpleTask getTaskIfWon() {
        return this.taskIfWon;
    }

    public void setTaskIfWon(SimpleTask taskIfWon) {
        this.taskIfWon = taskIfWon;
    }

    public SimpleTask getTaskIfLost() {
        return this.taskIfLost;
    }

    public void setTaskIfLost(SimpleTask taskIfLost) {
        this.taskIfLost = taskIfLost;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
