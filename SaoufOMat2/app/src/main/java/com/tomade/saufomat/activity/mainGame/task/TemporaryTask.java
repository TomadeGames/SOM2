package com.tomade.saufomat.activity.mainGame.task;

/**
 * Aufgaben, die eine Bestimmte Rundenzahl andauern
 * Created by woors on 21.08.2017.
 */

public class TemporaryTask extends Task {
    private static final long serialVersionUID = -4699686041061743476L;

    private int turnAmount;
    private int turnCount;
    private String endText;

    public TemporaryTask() {
        this.turnCount = 0;
    }

    public TemporaryTask(String text, TaskDifficult difficult, int drinkCount, int cost, TaskTarget target, int
            turnCount, String endText) {
        super(text, difficult, drinkCount, cost, target);
        this.turnAmount = turnCount;
        this.endText = endText;
        this.turnCount = 0;
    }

    public int getTurnAmount() {
        return this.turnAmount;
    }

    public void setTurnAmount(int turnAmount) {
        this.turnAmount = turnAmount;
    }

    public int getTurnCount() {
        return this.turnCount;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    /**
     * Erhöht den Rundenzähler
     *
     * @return gibt true zurück, wenn der Rundenzähler das Limit erreicht hat
     */
    public boolean increaseTurnCount() {
        this.turnCount++;
        return this.turnCount >= this.turnAmount;
    }

    public String getEndText() {
        return this.endText;
    }

    public void setEndText(String endText) {
        this.endText = endText;
    }
}
