package com.tomade.saufomat.model.drawable;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Animierter Rundenzähler
 * Created by woors on 14.03.2017.
 */

public class TurnCounter extends AppCompatTextView {
    private int turnCount;
    private TextView plusAnimationTextView;

    public TurnCounter(Context context) {
        this(context, null);
    }

    public TurnCounter(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);

    }

    public TurnCounter(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        this.turnCount = 1;
        this.plusAnimationTextView = new TextView(context);
        this.plusAnimationTextView.setWidth(40);
        this.plusAnimationTextView.setGravity(Gravity.LEFT);
        this.plusAnimationTextView.setTextColor(Color.parseColor("#ffffff"));
        this.plusAnimationTextView.setTextSize(30);
        this.plusAnimationTextView.setVisibility(GONE);
    }

    public void increaseValue(final int increment) {
        this.plusAnimationTextView.setText(increment);
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            int counter = 0;

            @Override
            public void run() {
                if (this.counter >= 5) {
                    TurnCounter.this.turnCount += increment;
                    TurnCounter.this.setText(TurnCounter.this.turnCount);
                } else {
                    TurnCounter.this.plusAnimationTextView.setTextSize(30 - this.counter * 10 / 4);
                    this.counter++;
                    mHandler.postDelayed(this, 100);
                }
            }
        }, 250);
    }
}
