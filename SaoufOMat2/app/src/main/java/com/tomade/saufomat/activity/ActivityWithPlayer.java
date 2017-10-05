package com.tomade.saufomat.activity;

import com.tomade.saufomat.model.player.Player;

/**
 * Created by woors on 20.07.2017.
 */

public interface ActivityWithPlayer {
    Player getCurrentPlayer();

    boolean arePlayerValid();
}
