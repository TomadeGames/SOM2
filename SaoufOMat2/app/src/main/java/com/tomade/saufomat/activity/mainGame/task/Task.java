package com.tomade.saufomat.activity.mainGame.task;

import com.tomade.saufomat.model.player.Player;

import java.io.Serializable;

/**
 * Aufgaben vom Hauptspiel
 * Created by woors on 10.03.2016.
 */
public class Task implements Serializable {
    private static final long serialVersionUID = 7664080231665909479L;

    private static int nextId = 0;
    private int id;
    private String text;
    private TaskDifficult difficult;
    /**
     * Getränkeanzahl bei Ja
     */
    private int drinkCount;
    /**
     * Getränkeanzahl bei Nein
     */
    private int cost;
    private TaskTarget target;
    private boolean alreadyUsed;

    public Task() {

    }

    public Task(String text, TaskDifficult difficult, int drinkCount, int cost, TaskTarget target) {
        this.id = nextId++;
        this.text = text;
        this.difficult = difficult;
        if (drinkCount < 0) {
            drinkCount = 0;
        }
        this.drinkCount = drinkCount;
        if (cost < 0) {
            cost = 0;
        }
        this.cost = cost;
        this.target = target;
        this.alreadyUsed = false;
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

    /**
     * Gibt die Kosten für eine Aufgabe zurück
     *
     * @return die Kosten der Aufgabe oder 0, wenn sie nicht abgelehnt werden kann
     */
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

    public void setId() {
        this.id = nextId++;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDifficult(TaskDifficult difficult) {
        this.difficult = difficult;
    }

    /**
     * Gibt die Anzahl der Getränke an, die beider dieser Aufgabe getrunken werden, wenn sie bestätigt wird.
     *
     * @param drinkCount die Anzahl der Schlücke, die bei "Ja" getrunken werden
     */
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

    /**
     * Gibt einen übersetzten Aufgabentext ohne Tokens zurück
     *
     * @param currentPlayer der aktuelle Spieler
     * @return der übersetzte Aufgabentext
     */
    public String getParsedText(Player currentPlayer) {
        return TaskParser.parseText(this.text, currentPlayer);
    }

}
