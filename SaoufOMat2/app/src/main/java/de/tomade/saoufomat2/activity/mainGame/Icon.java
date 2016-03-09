package de.tomade.saoufomat2.activity.mainGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;


/**
 * Created by woors on 09.03.2016.
 */
public class Icon {
    private Bitmap bitmap;
    private int x;
    private int y;

    public Icon(Bitmap bitmap, int x, int y){
        this.setBitmap(bitmap);
        this.setX(x);
        this.setY(y);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth()/2), y - (bitmap.getHeight() / 2), null);
    }
}
