package com.tomade.saufomat.activity.mainGame.task;

/**
 * Definition von Tokens, die in den Aufgabentexten eingesetzt werden können, um dort Aufgabentexte generieren zu
 * können.
 * Created by woors on 21.08.2017.
 */

public enum TaskTextToken {
    //Ein Token darf nicht mit einem anderen Token anfangen!!!
    //Beispiel: @random und @random_other würden sich überschneiden
    //@random und @other_random nicht
    RANDOM_PLAYER("@random"),
    RANDOM_OTHER_PLAYER("@other_random"),
    LEFT_PLAYER("@left"),
    RIGHT_PLAYER("@right"),
    WITH_MOST_DRINKS("@most_drinks"),
    WITH_LEAST_DRINKS("@least_drinks");

    private String token;

    TaskTextToken(String token) {
        this.token = token;
    }

    /**
     * Gibt den Token im Aufgabentext zurück
     *
     * @return der Token im Aufgabentext
     */
    public String getToken() {
        return this.token;
    }
}
