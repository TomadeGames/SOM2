package com.tomade.saufomat.activity.miniGame;

import android.os.Bundle;
import android.view.WindowManager;

import com.tomade.saufomat.ActivityWithPlayer;
import com.tomade.saufomat.activity.BaseActivity;
import com.tomade.saufomat.model.player.Player;

/**
 * Basisklasse für Minispiele
 * Created by woors on 07.03.2017.
 */

public abstract class BaseMiniGameActivity<PRESENTER extends BaseMiniGamePresenter> extends BaseActivity<PRESENTER>
        implements
        ActivityWithPlayer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public Player getCurrentPlayer() {
        return this.presenter.getCurrentPlayer();
    }

    @Override
    public boolean arePlayerValid() {
        return this.presenter.isFromMainGame();
    }
}
