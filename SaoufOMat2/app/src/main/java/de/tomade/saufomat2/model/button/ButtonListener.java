package de.tomade.saufomat2.model.button;

import java.util.EventListener;

/**
 * Created by woors on 09.03.2016.
 */
public interface ButtonListener extends EventListener {
    void onInput(ButtonEvent event);
}
