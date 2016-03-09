package de.tomade.saoufomat2.activity.model;

import java.util.EventListener;

/**
 * Created by woors on 09.03.2016.
 */
public interface ButtonListener extends EventListener {
    void onInput(ButtonEvent event);
}
