package de.tomade.saoufomat2.activity.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woors on 09.03.2016.
 */
public class DrawableButton extends Icon {

    protected List<ButtonListener> listenerList = new ArrayList<>();

    public DrawableButton(Bitmap bitmap, int x, int y, int with, int height) {
        super(bitmap, x, y, with, height);
    }

    public void addListener(ButtonListener listener) {
        listenerList.add(listener);
    }

    public void removeListener(ButtonListener listener) {
        listenerList.remove(listener);
    }

    public void checkClick(float xPressed, float yPressed) {
        int posTopRightX = this.getX() - this.getWith() / 2;
        int posTopRightY = this.getY() - this.getHeight() / 2;
        if (xPressed > posTopRightX && xPressed < posTopRightX + this.getWith()
                && yPressed > posTopRightY && yPressed < posTopRightY + this.getHeight()) {
            this.fireEvent(new ButtonEvent(this));
        }
    }

    protected void fireEvent(ButtonEvent evt) {
        for (ButtonListener listener : listenerList) {
            listener.onInput(evt);
        }
    }
}
