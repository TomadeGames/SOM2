package com.tomade.saufomat.model.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.tomade.saufomat.ContentLoader;


/**
 * Created by woors on 09.03.2016.
 */
public class DrawableImage {
    protected Bitmap image;
    protected int x;
    protected int y;
    protected int with;
    protected int height;
    protected boolean visible = true;

    public DrawableImage(Resources resources, int imageId, int x, int y, int width, int height) {
        this.setX(x);
        this.setY(y);
        this.setWith(width);
        this.setHeight(height);
        this.setImage(ContentLoader.getImage(resources, imageId));
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setImage(Bitmap image) {
        this.image = Bitmap.createScaledBitmap(image, this.getWith(), this.getHeight(), true);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void draw(Canvas canvas) {
        if (this.isVisible()) {
            canvas.drawBitmap(this.image, this.x - (this.image.getWidth() / 2), this.y - (this.image.getHeight() /
                    2), null);
        }
    }

    public int getWith() {
        return this.with;
    }

    public void setWith(int with) {
        this.with = with;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
