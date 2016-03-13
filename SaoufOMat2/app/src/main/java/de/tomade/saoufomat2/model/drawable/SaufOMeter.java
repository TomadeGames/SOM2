package de.tomade.saoufomat2.model.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by woors on 13.03.2016.
 */
public class SaufOMeter {
    private int x;
    private int y;
    private int with;
    private int height;
    private Bitmap[] images;
    private int currentFrame = 0;
    private boolean isVisible = true;

    public SaufOMeter(Bitmap[] images, int x, int y, int width, int height) {
        this.setX(x);
        this.setY(y);
        this.setWith(width);
        this.setHeight(height);
        this.setImages(images);
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

    public Bitmap[] getImages() {
        return images;
    }

    public void setImages(Bitmap[] images) {
        this.images = new Bitmap[images.length];
        for(int i = 0; i < images.length; i++){
            this.images[i] = Bitmap.createScaledBitmap(images[i],this.with,this.height,true);
        }
    }

    public void draw(Canvas canvas){
        Bitmap bitmap = images[getCurrentFrame()];
        if(this.isVisible()) {
            canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }
}
