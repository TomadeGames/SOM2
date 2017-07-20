package com.tomade.saufomat.model.player;

import java.io.Serializable;

/**
 * Statistik für einzelne Spieler
 * Created by woors on 20.07.2017.
 */

public class Statistic implements Serializable {
    private int easyWins;
    private int mediumWins;
    private int hardWins;

    public Statistic() {
        this.easyWins = 0;
        this.mediumWins = 0;
        this.hardWins = 0;
    }

    public int getHardWins() {
        return this.hardWins;
    }

    public void setHardWins(int hardWins) {
        this.hardWins = hardWins;
    }

    public int getMediumWins() {
        return this.mediumWins;
    }

    public void setMediumWins(int mediumWins) {
        this.mediumWins = mediumWins;
    }

    public int getEasyWins() {
        return this.easyWins;
    }

    public void setEasyWins(int easyWins) {
        this.easyWins = easyWins;
    }

    public void increaseEasyWins() {
        this.easyWins++;
    }

    public void increaseMediumWins() {
        this.mediumWins++;
    }

    public void increaseHardWins() {
        this.hardWins++;
    }
}
