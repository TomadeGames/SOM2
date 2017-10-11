package com.tomade.saufomat.activity.mainGame.task;

import com.tomade.saufomat.model.player.Player;

import java.io.Serializable;

/**
 * Ein vereinfachter Task
 * Created by woors on 11.10.2017.
 */

public class SimpleTask implements Serializable {
    private static final long serialVersionUID = 2616870062364131305L;
    private static int nextId = 0;
    protected int id;
    protected String text;
    protected TaskTarget taskTarget;
    protected boolean alreadyUsed;
    protected int drinkCount;

    public SimpleTask() {
        this.id = nextId++;
    }

    public SimpleTask(String text, int drinkCount, TaskTarget taskTarget) {
        this();
        this.text = text;
        this.drinkCount = drinkCount;
        this.taskTarget = taskTarget;
        this.alreadyUsed = false;
    }

    /**
     * Setzt die Id
     *
     * @param id die Id
     */
    public void setId(int id) {
        this.id = id;
        if (id > nextId) {
            nextId = id + 1;
        }
    }

    public void setId() {
        this.id = nextId++;
    }

    public String getText() {
        return this.text;
    }

    public TaskTarget getTaskTarget() {
        return this.taskTarget;
    }

    public int getId() {
        return this.id;
    }

    public void setTaskTarget(TaskTarget taskTarget) {
        this.taskTarget = taskTarget;
    }

    public boolean isAlreadyUsed() {
        return this.alreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }

    /**
     * Gibt einen übersetzten Aufgabentext ohne Tokens zurück
     *
     * @param currentPlayer der aktuelle Spieler
     * @return der übersetzte Aufgabentext
     */
    public String getParsedText(Player currentPlayer) {
        return TaskParser.parseText(this.text, currentPlayer);
    }

    /**
     * Gibt die Anzahl der Getränke an, die beider dieser Aufgabe getrunken werden, wenn sie bestätigt wird.
     *
     * @param drinkCount die Anzahl der Schlücke, die bei "Ja" getrunken werden
     */
    public void setDrinkCount(int drinkCount) {
        this.drinkCount = drinkCount;
    }

    public int getDrinkCount() {
        return this.drinkCount;
    }


    public void setText(String text) {
        this.text = text;
    }
}
