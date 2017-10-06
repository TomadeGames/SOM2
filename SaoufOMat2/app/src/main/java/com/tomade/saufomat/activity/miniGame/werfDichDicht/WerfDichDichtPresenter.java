package com.tomade.saufomat.activity.miniGame.werfDichDicht;

import android.os.Bundle;

import com.tomade.saufomat.activity.miniGame.BaseMiniGamePresenter;
import com.tomade.saufomat.persistance.SaveGameHelper;

import java.util.Random;

/**
 * Created by woors on 04.08.2017.
 */

public class WerfDichDichtPresenter extends BaseMiniGamePresenter<WerfDichDichtActivity> {
    private boolean[] isFull = new boolean[6];
    private int turnCount = 0;
    private int maxTurns;

    private int shotsClearedInOneTurn = 0;

    private Random random;

    public WerfDichDichtPresenter(WerfDichDichtActivity activity) {
        super(activity);
    }

    @Override
    public void leaveGame() {
        if (this.fromMainGame) {
            SaveGameHelper saveGameHelper = new SaveGameHelper(this.activity);
            saveGameHelper.saveWerfDichDicht(this.isFull);
        }
        super.leaveGame();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.random = new Random(System.currentTimeMillis());

        if (this.fromMainGame) {
            this.maxTurns = this.getPlayerAmount() * 3;
            if (this.maxTurns > 30) {
                this.maxTurns = this.getPlayerAmount() * 2;
                if (this.maxTurns > 30) {
                    this.maxTurns = this.getPlayerAmount();
                }
            }
        }
    }

    public boolean isShotFull(int index) {
        return this.isFull[index];
    }

    public int getNextRandomValue() {
        return this.random.nextInt(6);
    }

    public void loadLastGame() {
        SaveGameHelper saveGameHelper = new SaveGameHelper(this.activity);
        this.isFull = saveGameHelper.getSavedWerfDichDichtState();

    }

    public int getMaxTurns() {
        return this.maxTurns;
    }

    public int getTurnCount() {
        return this.turnCount;
    }

    public boolean[] getIsFull() {
        return this.isFull;
    }
}
