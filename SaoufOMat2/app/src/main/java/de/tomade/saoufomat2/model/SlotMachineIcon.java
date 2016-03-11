package de.tomade.saoufomat2.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by woors on 10.03.2016.
 */
public class SlotMachineIcon extends DrawableImage {
    private int screenWith;
    private int screenHeight;

    private Bitmap imageEasy;
    private Bitmap imageMedium;
    private Bitmap imageHard;

    private IconState state;

    private int getBeerWith() {
        return screenWith / 10;
    }

    private int getBeerHeight() {
        return (int) (screenHeight / 2.6);
    }

    private int getCocktailWith() {
        return screenWith / 8;
    }

    private int getCocktailHeight() {
        return screenHeight / 3;
    }

    private int getShotWith() {
        return screenWith / 11;
    }

    private int getShotHeight() {
        return screenHeight / 5;
    }

    private int getGameWith() {
        return screenWith / 6;
    }

    private int getGameHeight() {
        return screenHeight / 3;
    }

    public SlotMachineIcon(Bitmap imageEasy, Bitmap imageMedium, Bitmap imageHard, Bitmap imageGame, int x, int y, int width, int height, IconState state) {
        super(imageGame, x, y, width / 6, height / 3);
        this.screenHeight = height;
        this.screenWith = width;
        this.imageMedium = Bitmap.createScaledBitmap(imageMedium, getCocktailWith(), getCocktailHeight(), true);
        this.imageHard = Bitmap.createScaledBitmap(imageHard, getShotWith(), getShotHeight(), true);
        this.imageEasy = Bitmap.createScaledBitmap(imageEasy, getBeerWith(), getBeerHeight(), true);
        this.setState(state);
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.isVisible()) {
            Bitmap image = null;
            switch (getState()) {
                case EASY:
                    image = this.imageEasy;
                    break;
                case MEDIUM:
                    image = this.imageMedium;
                    break;
                case HARD:
                    image = this.imageHard;
                    break;
                case GAME:
                    image = this.bitmap;
                    break;
            }
            canvas.drawBitmap(image, x - (image.getWidth() / 2), y - (image.getHeight() / 2), null);
        }
    }

    public IconState getState() {
        return state;
    }

    public void setState(IconState state) {
        this.state = state;
    }
}
