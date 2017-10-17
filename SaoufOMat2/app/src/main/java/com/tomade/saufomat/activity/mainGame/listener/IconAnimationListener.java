package com.tomade.saufomat.activity.mainGame.listener;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import com.tomade.saufomat.activity.mainGame.IconPosition;
import com.tomade.saufomat.activity.mainGame.MainGameActivity;

/**
 * Created by woors on 17.10.2017.
 */

public class IconAnimationListener implements Animation.AnimationListener {
    private static final String TAG = IconAnimationListener.class.getSimpleName();
    private static final long ICON_STOP_DURATION = 500;
    private View icon;
    private float iconStopPosition;
    private IconPosition iconPosition;
    private MainGameActivity source;


    public IconAnimationListener(View icon, float iconStopPosition, IconPosition iconPosition, MainGameActivity
            source) {
        this.icon = icon;
        this.iconStopPosition = iconStopPosition;
        this.iconPosition = iconPosition;
        this.source = source;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        this.source.changeIcon(this.iconPosition);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.d(TAG, "moving icon to endPosition");
        this.icon.animate().y(this.iconStopPosition).setDuration(ICON_STOP_DURATION).start();
        this.source.animateSaufOMeter();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        this.source.rollStarted();
        this.source.changeIcon(this.iconPosition);
    }
}
