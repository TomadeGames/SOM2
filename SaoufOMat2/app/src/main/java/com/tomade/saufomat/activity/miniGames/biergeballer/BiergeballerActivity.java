package com.tomade.saufomat.activity.miniGames.biergeballer;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tomade.saufomat.activity.miniGames.BaseMiniGame;

/**
 * Activity, die das Spiel Biergeballer startet
 * Created by woors on 15.03.2017.
 */

public class BiergeballerActivity extends BaseMiniGame {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams
                .FLAG_FULLSCREEN);

        BiergeballerPanel biergeballerPanel = new BiergeballerPanel(this);
        this.setContentView(biergeballerPanel);
    }
}
