package com.tomade.saufomat.activity.mainGame.listener;

import android.animation.Animator;
import android.view.View;
import android.view.animation.Animation;

import com.tomade.saufomat.activity.mainGame.IconPosition;
import com.tomade.saufomat.activity.mainGame.MainGameActivity;

/**
 * AnimatorListener für das linke Icon des Hauptspiels
 * Created by woors on 17.10.2017.
 */

public class IconAnimatorListener implements Animator.AnimatorListener {
    private View icon;
    private Animation followAnimation;
    private IconPosition iconPosition;
    private MainGameActivity source;

    public IconAnimatorListener(View icon, Animation followAnimation, IconPosition iconPosition, MainGameActivity
            source) {
        this.icon = icon;
        this.followAnimation = followAnimation;
        this.iconPosition = iconPosition;
        this.source = source;
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        this.icon.animate().y(-500).setDuration(0).setListener(new Animator
                .AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                IconAnimatorListener.this.icon.animate().setListener(null);
                IconAnimatorListener.this.icon.startAnimation(IconAnimatorListener.this.followAnimation);
                IconAnimatorListener.this.source.checkIfAllRolling(IconAnimatorListener.this.iconPosition);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }
}
