package de.tomade.saoufomat2.activity.model;

import java.util.EventObject;

/**
 * Created by woors on 09.03.2016.
 */
public class ButtonEvent extends EventObject {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public ButtonEvent(Object source) {
        super(source);
    }
}
