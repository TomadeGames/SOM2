package com.tomade.saufomat.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.tomade.saufomat.R;

/**
 * ImageView für eine Spielkarte
 * Created by woors on 18.10.2017.
 */

public class CardImageView extends android.support.v7.widget.AppCompatImageView {
    public CardImageView(Context context) {
        super(context);
        this.showBackside();
    }

    public CardImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.showBackside();
    }

    public CardImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.showBackside();
    }

    private void showBackside() {
        this.setImageResource(R.drawable.rueckseite);
    }

    public void flipCardBack() {
        this.flipCardBack(null);
    }

    public void flipCardBack(Animator.AnimatorListener animatorListener) {
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this.getContext(), R.animator
                .flip_card_back);
        animatorSet.setTarget(this);
        Animator lastAnimator = animatorSet.getChildAnimations().get(0);
        lastAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setImageResource(R.drawable.rueckseite);
            }
        });
        if (animatorListener != null) {
            animatorSet.addListener(animatorListener);
        }
        animatorSet.start();
    }

    public void flipCard(int cardImageId) {
        this.flipCard(cardImageId, null);
    }

    public void flipCard(final int cardImageId, Animator.AnimatorListener animatorListener) {
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this.getContext(), R.animator.flip_card);
        animatorSet.setTarget(this);
        Animator firstAnimator = animatorSet.getChildAnimations().get(0);
        firstAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setImageResource(cardImageId);
            }
        });
        if (animatorListener != null) {
            animatorSet.addListener(animatorListener);
        }
        animatorSet.start();
    }
}
