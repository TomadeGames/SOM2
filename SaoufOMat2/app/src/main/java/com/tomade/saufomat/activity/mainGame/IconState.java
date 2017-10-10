package com.tomade.saufomat.activity.mainGame;

import com.tomade.saufomat.R;

/**
 * Die möglichen Icons für das Hauptspiel
 * Created by woors on 10.03.2016.
 */
public enum IconState {
    EASY(R.drawable.beer_icon),
    MEDIUM(R.drawable.cocktail_icon),
    HARD(R.drawable.shot_icon),
    GAME(R.drawable.dice_icon);

    private int imageId;

    IconState(int imageId) {
        this.imageId = imageId;
    }

    /**
     * Gibt die Id zum Bild des Icons zurück
     *
     * @return die Id vom Bild
     */
    public int getImageId() {
        return this.imageId;
    }
}
