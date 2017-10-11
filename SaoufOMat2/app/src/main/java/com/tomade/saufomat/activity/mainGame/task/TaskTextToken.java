package com.tomade.saufomat.activity.mainGame.task;

/**
 * Definition von Tokens, die in den Aufgabentexten eingesetzt werden können, um dort Aufgabentexte generieren zu
 * können.
 * Created by woors on 21.08.2017.
 */

public enum TaskTextToken {
    RANDOM_PLAYER("@random"),
    RANDOM_OTHER_PLAYER("@other_random"),
    LEFT_PLAYER("@left"),
    RIGHT_PLAYER("@right"),
    WITH_MOST_DRINKS("@most_drinks"),
    WITH_LEAST_DRINKS("@least_drinks"),
    CURRENT_PLAYER("@current_player"),
    MALE("@male"),
    FEMALE("@female");

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
