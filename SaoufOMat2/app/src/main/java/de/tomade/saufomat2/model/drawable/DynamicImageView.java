package de.tomade.saufomat2.model.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by woors on 05.04.2016.
 */
public class DynamicImageView extends ImageView {
    private boolean fullX;

    public DynamicImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setFullX(false);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final Drawable d = this.getDrawable();

        if (d != null) {
            // ceil not round - avoid thin vertical gaps along the left/right edges
            final int height = MeasureSpec.getSize(heightMeasureSpec);
            final int width;
            if (fullX) {
                width = (int) Math.ceil(height * (float) d.getIntrinsicWidth() / d.getIntrinsicHeight());
            } else {
                width = MeasureSpec.getSize(widthMeasureSpec);
            }
            this.setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public boolean isFullX() {
        return fullX;
    }

    public void setFullX(boolean fullX) {
        this.fullX = fullX;
    }
}