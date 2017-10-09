package com.tomade.saufomat.activity.miniGame.bierrutsche;

import android.os.Bundle;

import com.tomade.saufomat.DrinkHelper;
import com.tomade.saufomat.activity.miniGame.BaseMiniGamePresenter;
import com.tomade.saufomat.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Presenter für das Minipsiel Bierrutsche
 * Created by woors on 04.08.2017.
 */

public class BierrutschePresenter extends BaseMiniGamePresenter<BierrutscheActivity> {
    private static final int SINGLE_TURN_LIMIT = 3;

    private int turnCount = 0;

    private Map<Player, Integer> distances = new HashMap<>();
    private int lastPlayerDistance = -1;
    private int[] currentDistances = new int[3];
    private int currentDistance;

    private int firstDistance;

    public BierrutschePresenter(BierrutscheActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Beendet einen Zug
     *
     * @return gibt True zurück, wenn es der letzte Zug des Spielers war
     */
    public boolean endTurn() {
        if (this.currentDistance > 100) {
            this.currentDistance = 0;
        }
        if (this.currentPlayer.equals(this.currentPlayerAtStart)) {
            this.firstDistance = this.currentDistance;
            this.currentDistances = new int[3];
            if (this.currentPlayer.getNextPlayer().equals(this.currentPlayerAtStart) && this.fromMainGame) {
                this.activity.endGame();
            }
            return true;
        } else {
            this.currentDistances[this.turnCount % SINGLE_TURN_LIMIT] = this.currentDistance;

            this.turnCount++;
            if (this.turnCount % SINGLE_TURN_LIMIT == 0 || this.currentDistance > this.firstDistance) {
                int currentTurnScore = this.getMaximum(this.currentDistances);
                this.distances.put(this.currentPlayer, currentTurnScore);
                this.currentDistances = new int[3];
                this.turnCount = 0;
                if (this.currentPlayer.getNextPlayer().equals(this.currentPlayerAtStart) && this.fromMainGame) {
                    this.activity.endGame();
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public void nextPlayersTurn() {
        this.lastPlayerDistance = this.getMaximum(this.currentDistances);
        this.currentDistance = 0;
        this.currentDistances = new int[3];
    }

    private int getMaximum(int[] values) {
        int max = 0;
        for (int i : values) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    public String getBestPlayerText() {
        ArrayList<Player> betterPlayer = new ArrayList<>();
        for (Map.Entry<Player, Integer> entry : this.distances.entrySet()) {
            if (entry.getValue() > this.firstDistance) {
                betterPlayer.add(entry.getKey());
            }
        }
        StringBuilder resultText;

        if (betterPlayer.isEmpty()) {
            resultText = new StringBuilder("Alle ausser " + this.currentPlayerAtStart.getName() + " trinken 5!");
            DrinkHelper.increaseAllButOnePlayer(5, this.currentPlayerAtStart, this.activity);
        } else {
            resultText = new StringBuilder(this.currentPlayerAtStart.getName() + " trink " + betterPlayer.size() + "," +
                    " " +
                    "da ");
            for (int i = 0; i < betterPlayer.size(); i++) {
                resultText.append(betterPlayer.get(i).getName());
                if (i + 2 < betterPlayer.size()) {
                    resultText.append(", ");
                } else if (i + 1 < betterPlayer.size()) {
                    resultText.append(" und ");
                }
            }
            if (betterPlayer.size() > 1) {
                resultText.append(" besser waren!");
            } else {
                resultText.append(" besser war!");
            }
            this.currentPlayerAtStart.increaseDrinks(betterPlayer.size());
        }

        return resultText.toString();
    }

    public String getTurnText() {
        return this.currentDistance + "\n" + this.turnCount % SINGLE_TURN_LIMIT + "/" + SINGLE_TURN_LIMIT;
    }

    public int getLastPlayerDistance() {
        return this.lastPlayerDistance;
    }

    public Map<Player, Integer> getDistances() {
        return this.distances;
    }

    //TODO liste sortiert anzeigen
    public String getFullScore() {
        StringBuilder fullScore = new StringBuilder();
        fullScore.append("Vorgelegt:\n").append(this.currentPlayerAtStart.getName()).append(": ").append(this
                .firstDistance).append("\n\n");
        for (Map.Entry<Player, Integer> entry : this.distances.entrySet()) {
            fullScore.append(entry.getKey().getName()).append(": ").append(entry.getValue()).append("\n");
        }
        return fullScore.toString();
    }

    public int getCurrentDistance() {
        return this.currentDistance;
    }

    public void setCurrentDistances(int currentDistance) {
        this.currentDistance = currentDistance;
    }

    public int getCurrentPlayerScore() {
        return this.getMaximum(this.currentDistances);
    }

    public String getStartPlayerName() {
        return this.currentPlayerAtStart.getName();
    }

    public int getStartDistance() {
        return this.firstDistance;
    }
}
