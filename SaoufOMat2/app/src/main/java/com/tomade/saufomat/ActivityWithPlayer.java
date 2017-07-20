package com.tomade.saufomat;

import com.tomade.saufomat.model.Player;

/**
 * Created by woors on 20.07.2017.
 */

public interface ActivityWithPlayer {
    Player getCurrentPlayer();

    boolean arePlayerValid();
}
