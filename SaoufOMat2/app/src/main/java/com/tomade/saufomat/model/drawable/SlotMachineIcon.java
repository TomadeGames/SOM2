package com.tomade.saufomat.model.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.tomade.saufomat.ContentLoader;
import com.tomade.saufomat.activity.mainGame.IconState;

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

    public SlotMachineIcon(Resources resources, int imageEasyId, int imageMediumId, int imageHardId, int imageGameId,
                           int x, int y,
                           int width, int height, IconState state) {
        super(resources, imageGameId, x, y, width / 6, height / 3);
        this.screenHeight = height;
        this.screenWith = width;
        this.imageMedium = ContentLoader.getImage(resources, imageMediumId, this.getCocktailWith(), this
                .getCocktailHeight());
        this.imageHard = ContentLoader.getImage(resources, imageHardId, this.getShotWith(), this.getShotHeight());
        this.imageEasy = ContentLoader.getImage(resources, imageEasyId, this.getBeerWith(), this.getBeerHeight());
        this.setState(state);
    }

    private int getBeerWith() {
        return this.screenWith / 10;
    }

    private int getBeerHeight() {
        return (int) (this.screenHeight / 2.6);
    }

    private int getCocktailWith() {
        return this.screenWith / 8;
    }

    private int getCocktailHeight() {
        return this.screenHeight / 3;
    }

    private int getShotWith() {
        return this.screenWith / 11;
    }

    private int getShotHeight() {
        return this.screenHeight / 5;
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
                    image = this.image;
                    break;
            }
            canvas.drawBitmap(image, this.x - (image.getWidth() / 2), this.y - (image.getHeight() / 2), null);
        }
    }

    public IconState getState() {
        return this.state;
    }

    public void setState(IconState state) {
        this.state = state;
    }
}