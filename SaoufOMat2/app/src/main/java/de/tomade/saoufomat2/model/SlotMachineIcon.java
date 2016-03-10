package de.tomade.saoufomat2.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by woors on 10.03.2016.
 */
public class SlotMachineIcon extends DrawableImage {
    private Bitmap imageEasy;
    private Bitmap imageMedium;
    private Bitmap imageHard;

    private IconState state;

    public SlotMachineIcon(Bitmap imageEasy, Bitmap imageMedium, Bitmap imageHard, Bitmap imageGame, int x, int y, int width, int height, IconState state) {
        super(imageGame, x, y, width, height);
        this.imageMedium = imageMedium;
        this.imageHard = imageHard;
        this.imageEasy = imageEasy;
        this.setState(state);
    }

    @Override
    public void draw(Canvas canvas){
        if(this.isVisible()) {
            Bitmap image = null;
            switch (getState()){
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
