package de.tomade.saufomat2.activity.miniGames;

import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.tomade.saufomat2.activity.miniGames.augensaufen.AugensaufenActivity;
import de.tomade.saufomat2.activity.miniGames.bierrutsche.BierrutscheActivity;
import de.tomade.saufomat2.activity.miniGames.ichHabNochNie.IchHabNochNieActivity;
import de.tomade.saufomat2.activity.miniGames.kings.KingsActivity;
import de.tomade.saufomat2.activity.miniGames.kistenStapeln.KistenStapelnActivity;
import de.tomade.saufomat2.activity.miniGames.memory.MemoryActivity;
import de.tomade.saufomat2.activity.miniGames.werfDichDicht.WerfDichDichtActivity;
import de.tomade.saufomat2.busfahren.BusfahrenActivity;

/**
 * Created by woors on 22.03.2016.
 */
public enum MiniGame implements Serializable {
    AUGENSAUFEN,
    BIERGEBALLER,
    BIERRUTSCHE,
    BUSFAHREN,
    CIRCLE_OF_DEATH,
    ICH_HAB_NOCH_NIE,
    KINGS,
    KISTEN_STAPELN,
    WERF_DICH_DICHT,
    MEMORY;

    public static Class getMiniGameClass(MiniGame miniGame){
        switch (miniGame) {
            case AUGENSAUFEN:
                return AugensaufenActivity.class;
            case BIERGEBALLER:
                return MemoryActivity.class;
            case BUSFAHREN:
                return BusfahrenActivity.class;
            case BIERRUTSCHE:
                return BierrutscheActivity.class;
            case ICH_HAB_NOCH_NIE:
                return IchHabNochNieActivity.class;
            case KINGS:
                return KingsActivity.class;
            case KISTEN_STAPELN:
                return KistenStapelnActivity.class;
            case WERF_DICH_DICHT:
                return WerfDichDichtActivity.class;
            default:
                throw new IllegalArgumentException("Minigame " + miniGame + " ist noch nicht implementiert");
        }
    }

    public static MiniGame getRandomMiniGame() {
        List<MiniGame> miniGames = new ArrayList<>();
        MiniGame.initList(miniGames);

        Random rnd = new Random(System.currentTimeMillis());
        return miniGames.get(rnd.nextInt(miniGames.size()));
    }

    public static MiniGame getRandomMiniGameAndRemoveFromList(List<MiniGame> list){
        if(list.size() == 0){
            MiniGame.initList(list);
        }
        Random rnd = new Random(System.currentTimeMillis());
        int index = rnd.nextInt(list.size());
        MiniGame miniGame = list.get(index);
        list.remove(index);
        return  miniGame;
    }

    private static void initList(List<MiniGame> miniGames){
        miniGames.add(MiniGame.AUGENSAUFEN);
        miniGames.add(MiniGame.BIERRUTSCHE);
        //TODO Biergeballer
        miniGames.add(MiniGame.BUSFAHREN);
        //TODO Circle of Death
        miniGames.add(MiniGame.ICH_HAB_NOCH_NIE);
        miniGames.add(MiniGame.KINGS);
        miniGames.add(MiniGame.KISTEN_STAPELN);
        miniGames.add(MiniGame.WERF_DICH_DICHT);
        //TODO Memory
    }
}
