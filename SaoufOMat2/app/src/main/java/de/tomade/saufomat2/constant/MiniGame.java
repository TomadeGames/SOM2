package de.tomade.saufomat2.constant;

import java.io.Serializable;

import de.tomade.saufomat2.activity.miniGames.BaseMiniGame;
import de.tomade.saufomat2.activity.miniGames.augensaufen.AugensaufenActivity;
import de.tomade.saufomat2.activity.miniGames.biergeballer.BiergeballerActivity;
import de.tomade.saufomat2.activity.miniGames.bierrutsche.BierrutscheActivity;
import de.tomade.saufomat2.activity.miniGames.busfahren.BusfahrenActivity;
import de.tomade.saufomat2.activity.miniGames.ichHabNochNie.IchHabNochNieActivity;
import de.tomade.saufomat2.activity.miniGames.kings.KingsActivity;
import de.tomade.saufomat2.activity.miniGames.kistenStapeln.KistenStapelnActivity;
import de.tomade.saufomat2.activity.miniGames.werfDichDicht.WerfDichDichtActivity;

/**
 * Definition der Minispiele
 * Created by woors on 22.03.2016.
 */
public enum MiniGame implements Serializable {
    AUGENSAUFEN(AugensaufenActivity.class),
    BIERGEBALLER(BiergeballerActivity.class),
    BIERRUTSCHE(BierrutscheActivity.class),
    BUSFAHREN(BusfahrenActivity.class),
    ICH_HAB_NOCH_NIE(IchHabNochNieActivity.class),
    KINGS(KingsActivity.class),
    KISTEN_STAPELN(KistenStapelnActivity.class),
    WERF_DICH_DICHT(WerfDichDichtActivity.class);

    private Class<? extends BaseMiniGame> activity;

    MiniGame(Class<? extends BaseMiniGame> activity) {
        this.activity = activity;
    }

    public Class<? extends BaseMiniGame> getActivity() {
        return this.activity;
    }
}
