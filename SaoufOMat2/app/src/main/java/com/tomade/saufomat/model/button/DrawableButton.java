package com.tomade.saufomat.model.button;

import android.content.res.Resources;

import com.tomade.saufomat.model.drawable.DrawableImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woors on 09.03.2016.
 */
public class DrawableButton extends DrawableImage {

    protected List<ButtonListener> listenerList = new ArrayList<>();

    public DrawableButton(Resources resources, int imageId, int x, int y, int with, int height) {
        super(resources, imageId, x, y, with, height);
    }

    public void addListener(ButtonListener listener) {
        this.listenerList.add(listener);
    }

    public boolean removeListener(ButtonListener listener) {
        return this.listenerList.remove(listener);
    }

    public boolean checkClick(float xPressed, float yPressed) {
        int posTopRightX = this.getX() - this.getWith() / 2;
        int posTopRightY = this.getY() - this.getHeight() / 2;
        if (xPressed >= posTopRightX && xPressed <= posTopRightX + this.getWith()
                && yPressed >= posTopRightY && yPressed <= posTopRightY + this.getHeight()) {
            this.fireEvent(new ButtonEvent(this));
            return true;
        }
        return false;
    }

    protected void fireEvent(ButtonEvent evt) {
        for (ButtonListener listener : this.listenerList) {
            listener.onInput(evt);
        }
    }
}
