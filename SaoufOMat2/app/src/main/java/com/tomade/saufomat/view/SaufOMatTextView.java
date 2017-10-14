package com.tomade.saufomat.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * TextView für Buttonbeschriftungen
 * Created by woors on 14.10.2017.
 */

public class SaufOMatTextView extends TextView {
    public SaufOMatTextView(Context context) {
        super(context);
        this.setStyles();
    }

    public SaufOMatTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setStyles();
    }

    public SaufOMatTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setStyles();
    }

    private void setStyles() {
        Typeface font = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/berlin_snas_fb_demi" +
                ".TTF");
        this.setTypeface(font);
    }
}
