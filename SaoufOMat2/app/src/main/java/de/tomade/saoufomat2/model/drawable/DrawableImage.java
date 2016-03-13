package de.tomade.saoufomat2.model.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;


/**
 * Created by woors on 09.03.2016.
 */
public class DrawableImage {
    protected Bitmap bitmap;
    protected int x;
    protected int y;
    protected int with;
    protected int height;
    protected boolean visible = true;

    public DrawableImage(Bitmap bitmap, int x, int y, int width, int height){
        this.setX(x);
        this.setY(y);
        this.setWith(width);
        this.setHeight(height);
        this.setBitmap(bitmap);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = Bitmap.createScaledBitmap(bitmap, this.getWith(), this.getHeight(), true);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void draw(Canvas canvas){
        if(this.isVisible()) {
            canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
        }
    }

    public int getWith() {
        return with;
    }

    public void setWith(int with) {
        this.with = with;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
