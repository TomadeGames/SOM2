package de.tomade.saufomat2;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.tomade.saufomat2.constant.MiniGame;

/**
 * Verwaltungsklasse für für Minigames
 * Created by woors on 07.03.2017.
 */

public class MiniGameProvider {

    private List<MiniGame> miniGames;

    public MiniGameProvider() {
        this.miniGames = new ArrayList<>();
        this.resetList();
    }

    public static MiniGame getRandomMiniGame() {
        Set<MiniGame> games = EnumSet.allOf(MiniGame.class);
        MiniGame[] miniGames = (MiniGame[]) games.toArray();

        Random rnd = new Random(System.currentTimeMillis());
        return miniGames[rnd.nextInt(miniGames.length)];
    }

    public MiniGame getRandomMiniGameAndRemoveFromList() {
        if (this.miniGames.size() == 0) {
            this.resetList();
        }
        Random rnd = new Random(System.currentTimeMillis());
        int index = rnd.nextInt(this.miniGames.size());
        MiniGame miniGame = this.miniGames.get(index);
        this.miniGames.remove(index);
        return miniGame;
    }

    private void resetList() {
        Set<MiniGame> games = EnumSet.allOf(MiniGame.class);
        this.miniGames.clear();
        this.miniGames = new ArrayList<>();
        this.miniGames.addAll(games);
    }
}
