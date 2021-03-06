package com.tomade.saufomat.model.player;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * Ein Spiler
 * Created by markk on 09.03.2016.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 6294040778310797624L;
    private static int nextId = 0;
    private int id;
    private String name;
    private int weight;
    private boolean isMan;
    private int drinks = 0;
    private Player nextPlayer;
    private Player lastPlayer;
    private Statistic statistic;

    public Player() {
        this.id = nextId;
        nextId++;
        this.statistic = new Statistic();
    }

    @Nullable
    public static Player getPlayerById(List<Player> playerList, int id) {
        for (Player p : playerList) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean getIsMan() {
        return this.isMan;
    }

    public void setIsMan(boolean isMan) {
        this.isMan = isMan;
    }

    public int getDrinks() {
        return this.drinks;
    }

    public void setDrinks(int drinks) {
        this.drinks = drinks;
    }

    public void increaseDrinks(int increase) {
        this.drinks += increase;
    }

    public Player getNextPlayer() {
        return this.nextPlayer;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public Player getLastPlayer() {
        return this.lastPlayer;
    }

    public void setLastPlayer(Player lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    public void setId(int id) {
        this.id = id;
        if (id > nextId) {
            nextId = id + 1;
        }
    }

    public Statistic getStatistic() {
        return this.statistic;
    }

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }
}
