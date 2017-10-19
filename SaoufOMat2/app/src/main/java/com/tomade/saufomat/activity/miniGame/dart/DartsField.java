package com.tomade.saufomat.activity.miniGame.dart;

import static com.tomade.saufomat.activity.mainGame.task.TaskTextToken.LEFT_PLAYER;
import static com.tomade.saufomat.activity.mainGame.task.TaskTextToken.RIGHT_PLAYER;

/**
 * Die möglichen Trefferzonen der Dartscheibe
 * Created by woors on 18.10.2017.
 */

public enum DartsField {
    MIDDLE("Bullseye!", "Alle außer dir trinken 3"),
    RING("Hauptgewinn!", "Du darfst 3 Kurze deiner Wahl verteilen"),
    OUTER_YELLOW("Gelb", "Trink 3 mal"),
    OUTER_GREEN("Grün", RIGHT_PLAYER.getToken() + " trinkt 3"),
    OUTER_BLUE("Blau", LEFT_PLAYER.getToken() + " trinkt 3"),
    INNER_YELLOW("Gelb", "Trink einen"),
    INNER_GREEN("Grün", RIGHT_PLAYER.getToken() + " trinkt einen"),
    INNER_BLUE("Blau", LEFT_PLAYER.getToken() + " trinkt einen"),
    OUT("Daneben!", "Trink 5");

    private String name;
    private String price;

    DartsField(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getPrice() {
        return this.price;
    }

    public String getName() {
        return this.name;
    }
}
