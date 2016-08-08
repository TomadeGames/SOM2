package de.tomade.saufomat2.activity.miniGames.memory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import de.tomade.saufomat2.model.drawable.DrawableImage;

/**
 * Created by woors on 30.05.2016.
 */
public class MemorySymbol extends DrawableImage {
    private boolean isShown;
    private boolean isFinished;
    private int pictureId;
    private Bitmap backSidePicture;

    private List<MemoryAnimationListener> listeners = new ArrayList<>();

    public MemorySymbol(int pictureId, Bitmap memoryPicture, Bitmap backSidePicture, int x, int y, int width, int height) {
        super(memoryPicture, x, y, width, height);
        this.setPictureId(pictureId);
        this.setFinished(false);
        this.setShown(false);
        this.backSidePicture = Bitmap.createScaledBitmap(backSidePicture, width, height, true);
    }

    @Override
    public void draw(Canvas canvas){
        if(this.isVisible()) {
            if(isShown()) {
                canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
            }
            else{
                canvas.drawBitmap(backSidePicture, x-(bitmap.getWidth()/2), y - (bitmap.getHeight() / 2), null);
            }
        }
    }

    public void FlipUp(){
        setShown(true);
        for (MemoryAnimationListener l: listeners){
            l.upAnimationFinished();
        }
    }

    public void FlipDown(){
        setShown(false);
        for (MemoryAnimationListener l: listeners){
            l.downAnimationFinished();
        }
    }

    public void AddAnimationListener(MemoryAnimationListener listener) {
        listeners.add(listener);
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }
}
