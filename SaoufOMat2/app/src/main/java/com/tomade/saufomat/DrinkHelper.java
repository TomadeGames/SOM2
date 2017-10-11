package com.tomade.saufomat;

import com.tomade.saufomat.activity.ActivityWithPlayer;
import com.tomade.saufomat.model.player.Player;

/**
 * Hilfsklasse zum Verteilen von Getränken an Spielern
 * Created by woors on 20.07.2017.
 */

public class DrinkHelper {
    /**
     * Erhöht den Getränkezähler für alle Spieler
     *
     * @param increment um diesen Wert wird der Getränkezähler erhöht
     * @param source    die Klasse, die diese Methode aufruft
     */
    public static void increaseAll(int increment, ActivityWithPlayer source) {
        if (source.arePlayerValid()) {
            Player player = source.getCurrentPlayer();
            do {
                player.increaseDrinks(increment);
                player = player.getNextPlayer();
            } while (player != source.getCurrentPlayer());
        }
    }

    /**
     * Erhöht den Getränkezähler für alle ausser einen Spieler
     *
     * @param increment           um diesen Wert wird der Getränkezähler erhöht
     * @param playerWithoutDrinks der Spieler, der nicht trinken muss
     * @param source              die Klasse, die diese Methode aufruft
     */
    public static void increaseAllButOnePlayer(int increment, Player playerWithoutDrinks, ActivityWithPlayer source) {
        if (source.arePlayerValid()) {
            Player player = source.getCurrentPlayer();
            do {
                if (player != playerWithoutDrinks) {
                    player.increaseDrinks(increment);
                }
                player = player.getNextPlayer();
            } while (player != source.getCurrentPlayer());
        }
    }

    /**
     * Erhöht den Getränkezähler für bestimmte Spieler
     *
     * @param increment           um diesen Wert wird der Getränkezähler erhöht
     * @param playerWithoutDrinks die Spieler, die trinken muss
     * @param source              die Klasse, die diese Methode aufruft
     */
    public static void increaseSomePlayer(int increment, Player[] playerWithoutDrinks, ActivityWithPlayer source) {
        if (source.arePlayerValid()) {
            Player player = source.getCurrentPlayer();
            do {
                boolean getDrink = false;
                for (Player playerWithoutDrink : playerWithoutDrinks) {
                    if (player.getId() == playerWithoutDrink.getId()) {
                        getDrink = true;
                    }
                }
                if (getDrink) {
                    player.increaseDrinks(increment);
                }
            } while (player != source.getCurrentPlayer());
        }
    }

    /**
     * Erhöht den Getränkezähler für alle ausser für bestimmte Spieler
     *
     * @param increment           um diesen Wert wird der Getränkezähler erhöht
     * @param playerWithoutDrinks die Spieler, die nicht trinken muss
     * @param source              die Klasse, die diese Methode aufruft
     */
    public static void increaseAllButSomePlayer(int increment, Player[] playerWithoutDrinks, ActivityWithPlayer
            source) {
        if (source.arePlayerValid()) {
            Player player = source.getCurrentPlayer();
            do {
                boolean getDrink = true;
                for (Player playerWithoutDrink : playerWithoutDrinks) {
                    if (player.getId() == playerWithoutDrink.getId()) {
                        getDrink = false;
                    }
                }
                if (getDrink) {
                    player.increaseDrinks(increment);
                }
            } while (player != source.getCurrentPlayer());
        }
    }

    /**
     * Erhöht den Getränkezähler für den vorherigen Spieler
     *
     * @param increment um diesen Wert wird der Getränkezähler erhöht
     * @param source    die Klasse, die diese Methode aufruft
     */
    public static void increaseLeft(int increment, ActivityWithPlayer source) {
        if (source.arePlayerValid()) {
            source.getCurrentPlayer().getLastPlayer().increaseDrinks(increment);
        }
    }

    /**
     * Erhöht den Getränkezähler für den nächsten Spieler
     *
     * @param increment um diesen Wert wird der Getränkezähler erhöht
     * @param source    die Klasse, die diese Methode aufruft
     */
    public static void increaseRight(int increment, ActivityWithPlayer source) {
        if (source.arePlayerValid()) {
            source.getCurrentPlayer().getNextPlayer().increaseDrinks(increment);
        }
    }

    /**
     * Erhöht den Getränkezähler für alle Männer
     *
     * @param increment um diesen Wert wird der Getränkezähler erhöht
     * @param source    die Klasse, die diese Methode aufruft
     */
    public static void increaseMen(int increment, ActivityWithPlayer source) {
        if (source.arePlayerValid()) {
            Player player = source.getCurrentPlayer();
            do {
                if (player.getIsMan()) {
                    player.increaseDrinks(increment);
                }
                player = player.getNextPlayer();
            } while (player != source.getCurrentPlayer());
        }
    }

    /**
     * Erhöht den Getränkezähler für alle Frauen
     *
     * @param increment um diesen Wert wird der Getränkezähler erhöht
     * @param source    die Klasse, die diese Methode aufruft
     */
    public static void increaseWomen(int increment, ActivityWithPlayer source) {
        if (source.arePlayerValid()) {
            Player player = source.getCurrentPlayer();
            do {
                if (!player.getIsMan()) {
                    player.increaseDrinks(increment);
                }
                player = player.getNextPlayer();
            } while (player != source.getCurrentPlayer());
        }
    }

    /**
     * Erhöht den Getränkezähler für einen Spieler und seinen Nachbarn
     *
     * @param increment um diesen Wert wird der Getränkezähler erhöht
     * @param player    der Spieler in der Mitte
     * @param source    die Klasse, die diese Methode aufruft
     */
    public static void increasePlayerWithNeighbours(int increment, Player player, ActivityWithPlayer source) {
        if (source.arePlayerValid()) {
            player.increaseDrinks(increment);
            increaseNeighbours(increment, player, source);
        }
    }

    /**
     * Erhöht den Getränkezähler für Nachbarn eines Spielers
     *
     * @param increment um diesen Wert wird der Getränkezähler erhöht
     * @param player    der Spieler in der Mitte
     * @param source    die Klasse, die diese Methode aufruft
     */
    public static void increaseNeighbours(int increment, Player player, ActivityWithPlayer source) {
        if (source.arePlayerValid()) {
            Player lastPlayer = player.getLastPlayer();
            Player nextPlayer = player.getNextPlayer();
            if (nextPlayer != player) {
                nextPlayer.increaseDrinks(increment);
                if (lastPlayer != nextPlayer) {
                    lastPlayer.increaseDrinks(increment);
                }
            }
        }
    }

    /**
     * Erhöht den Getränkezähler für den Aktuellen Spieler
     *
     * @param increment um diesen Wert wird der Getränkezähler erhöht
     * @param source    die Klasse, die diese Methode aufruft
     */
    public static void increaseCurrentPlayer(int increment, ActivityWithPlayer source) {
        if (source.arePlayerValid()) {
            source.getCurrentPlayer().increaseDrinks(increment);
        }
    }
}
