package com.tomade.saufomat.activity.miniGame.augensaufen;

import android.os.Bundle;

import com.tomade.saufomat.activity.miniGame.BaseMiniGamePresenter;

import java.util.Random;

/**
 * Presenter der AugensaufenActivity
 * Created by woors on 03.08.2017.
 */

public class AugensaufenPresenter extends BaseMiniGamePresenter<AugensaufenActivity> {
    public AugensaufenPresenter(AugensaufenActivity activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public int getRandomNumber() {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(6);
    }
}
