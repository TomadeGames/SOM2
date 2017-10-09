package com.tomade.saufomat.activity.miniGame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.View;
import android.view.WindowManager;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.ActivityWithPlayer;
import com.tomade.saufomat.activity.BaseActivity;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.model.player.Player;

/**
 * Basisklasse für Minispiele
 * Created by woors on 07.03.2017.
 */

public abstract class BaseMiniGameActivity<PRESENTER extends BaseMiniGamePresenter> extends BaseActivity<PRESENTER>
        implements ActivityWithPlayer, View.OnClickListener {
    protected static final int TARGET_TURN_COUNT = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.presenter.showTutorialIfFirstStart();
    }

    @Override
    @CallSuper
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tutorialButton:
                this.showTutorial();
                break;
            case R.id.backButton:
                this.presenter.leaveGame();
                break;
        }
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

    public void showTutorial() {
        Intent intent = new Intent(this, TutorialDialog.class);
        intent.putExtra(IntentParameter.Tutorial.TEXT_ID, this.presenter.getThisGame().getTutorialId());
        this.startActivity(intent);
    }
}
