package com.tomade.saufomat.activity.miniGame.bierrutsche;

import android.os.Bundle;

import com.tomade.saufomat.activity.miniGame.BaseMiniGamePresenter;
import com.tomade.saufomat.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by woors on 04.08.2017.
 */

public class BierrutschePresenter extends BaseMiniGamePresenter<BierrutscheActivity> {
    private static final int SINGLE_TURN_LIMIT = 3;
    private static final int DRINK_AMOUNT = 3;

    private int turnCount = 0;
    private int maxTurnCount;

    private Map<Player, Integer> distances = new HashMap<>();
    private int lastPlayerDistance = -1;
    private int[] currentDistances = new int[3];
    private int currentDistance;
    private int currentTurnScore;

    public BierrutschePresenter(BierrutscheActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.isFromMainGame())
            this.maxTurnCount = SINGLE_TURN_LIMIT * this.playerList.size();
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
        this.currentDistances[this.turnCount % SINGLE_TURN_LIMIT] = this.currentDistance;

        this.turnCount++;
        if (this.turnCount % SINGLE_TURN_LIMIT == 0) {
            this.currentTurnScore = this.getMaximum(this.currentDistances);
            this.distances.put(this.currentPlayer, this.currentTurnScore);
            if (this.turnCount >= this.maxTurnCount && this.fromMainGame) {
                this.activity.endGame();
            }
            return true;
        } else {
            return false;
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


    public String getWorstPlayerText() {
        String worstScore = "";
        List<Player> worstPlayer = new ArrayList<>();
        int worst = 101;
        for (Map.Entry<Player, Integer> entry : this.distances.entrySet()) {
            int score = entry.getValue();
            if (score < worst) {
                worstPlayer.clear();
                worstPlayer.add(entry.getKey());
                worst = score;
            } else if (score == worst) {
                worstPlayer.add(entry.getKey());
            }
        }
        for (Player worstSinglePlayer : worstPlayer) {
            worstSinglePlayer.increaseDrinks(DRINK_AMOUNT);
            worstScore += worstSinglePlayer.getName() + " trink " + DRINK_AMOUNT + "\n";
        }
        return worstScore;
    }

    public String getTurnText() {
        return this.getMaximum(this.currentDistances) + "\n" + this.turnCount % SINGLE_TURN_LIMIT + "/" +
                SINGLE_TURN_LIMIT;
    }

    public int getLastPlayerDistance() {
        return this.lastPlayerDistance;
    }

    public Map<Player, Integer> getDistances() {
        return this.distances;
    }

    public String getFullScore() {
        String fullScore = "";
        for (Map.Entry<Player, Integer> entry : this.distances.entrySet()) {
            fullScore += entry.getKey().getName() + ": " + entry.getValue() + "\n";
        }
        return fullScore;
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
}
