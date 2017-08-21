package com.tomade.saufomat.activity.mainGame.task;

import android.util.Log;

import com.tomade.saufomat.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Hilfsklasse um Tokens in den Aufgabentexten zu übersetzen
 * Created by woors on 21.08.2017.
 */

public class TaskParser {
    private static final String TAG = TaskParser.class.getSimpleName();

    /**
     * Wandelt einen Aufgabentext mit Tokens zu einen übersetzten Text um
     *
     * @param taskText      der Aufgabentext
     * @param currentPlayer der Aktuelle Spieler
     * @return der übersetzte Text
     */
    public static String parseText(String taskText, Player currentPlayer) {
        String parsedText = taskText;
        int playerCount = 0;
        Player nextPlayer = currentPlayer;
        do {
            playerCount++;
            nextPlayer = nextPlayer.getNextPlayer();
        } while (nextPlayer != currentPlayer);

        for (TaskTextToken token : TaskTextToken.values()) {
            if (parsedText.contains(token.getToken())) {
                switch (token) {
                    case LEFT_PLAYER:
                        parsedText = parsedText.replace(token.getToken(), currentPlayer.getLastPlayer().getName());
                        break;
                    case RIGHT_PLAYER:
                        parsedText = parsedText.replace(token.getToken(), currentPlayer.getNextPlayer().getName());
                        break;
                    case RANDOM_PLAYER:
                        parsedText = parsedText.replace(token.getToken(),
                                getRandomPlayer(currentPlayer, playerCount).getName());
                        break;
                    case WITH_LEAST_DRINKS:
                        List<Player> playerWithLeastDrinks = getPlayerWithLeastDrinks(currentPlayer);
                        parsedText = parsedText.replace(token.getToken(),
                                getPlayerNamesFromList(playerWithLeastDrinks));
                        break;
                    case WITH_MOST_DRINKS:
                        List<Player> playerWithMostDrinks = getPlayerWithMostDrinks(currentPlayer);
                        parsedText = parsedText.replace(token.getToken(), getPlayerNamesFromList(playerWithMostDrinks));
                        break;
                    case RANDOM_OTHER_PLAYER:
                        parsedText = parsedText.replace(token.getToken(),
                                getRandomOtherPlayer(currentPlayer, playerCount).getName());
                        break;
                    default:
                        throw new IllegalStateException("Token " + token + " [" + token.getToken() + "] is not parsed");
                }
            }
        }
        return parsedText;
    }


    private static String getPlayerNamesFromList(List<Player> players) {
        String allPlayersText = "";
        for (int i = 0; i < players.size(); i++) {
            allPlayersText += players.get(i).getName();
            if (i + 2 < players.size()) {
                allPlayersText += ", ";
            } else if (i + 1 < players.size()) {
                allPlayersText += " und ";
            }
        }
        return allPlayersText;
    }

    private static Player getRandomOtherPlayer(Player currentPlayer, int playerCount) {
        Random random = new Random(System.currentTimeMillis());
        int index = random.nextInt(playerCount - 1);
        int counter = 0;
        Player target = currentPlayer.getNextPlayer();
        while (counter < index) {
            counter++;
            target = target.getNextPlayer();
        }
        return target;
    }

    private static Player getRandomPlayer(Player currentPlayer, int playerCount) {
        Random random = new Random(System.currentTimeMillis());
        int index = random.nextInt(playerCount);
        Log.d(TAG, "random index: " + index + " playerCount: " + playerCount);
        int counter = 0;
        Player target = currentPlayer;
        while (counter < index) {
            counter++;
            target = target.getNextPlayer();
        }
        return target;
    }

    private static List<Player> getPlayerWithMostDrinks(Player currentPlayer) {
        int mostDrinks = 0;
        List<Player> players = new ArrayList<>();

        Player player = currentPlayer;
        do {
            if (player.getDrinks() > mostDrinks) {
                mostDrinks = player.getDrinks();
                players.clear();
                players.add(player);
            } else if (player.getDrinks() == mostDrinks) {
                players.add(player);
            }
            player = player.getNextPlayer();
        } while (player != currentPlayer);
        return players;
    }

    private static List<Player> getPlayerWithLeastDrinks(Player currentPlayer) {
        int leastDrinks = Integer.MAX_VALUE;
        List<Player> players = new ArrayList<>();

        Player player = currentPlayer;
        do {
            if (player.getDrinks() < leastDrinks) {
                leastDrinks = player.getDrinks();
                players.clear();
                players.add(player);
            } else if (player.getDrinks() == leastDrinks) {
                players.add(player);
            }
            player = player.getNextPlayer();
        } while (player != currentPlayer);
        return players;
    }
}
