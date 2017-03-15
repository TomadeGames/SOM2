package de.tomade.saufomat2.constant;

import java.io.Serializable;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.miniGames.BaseMiniGame;
import de.tomade.saufomat2.activity.miniGames.augensaufen.AugensaufenActivity;
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
    AUGENSAUFEN(AugensaufenActivity.class, R.drawable.augensaufen_screen, R.string.minigame_augensaufen_caption),
    //BIERGEBALLER(BiergeballerActivity.class, R.drawable.biergeballer_screen, R.string.minigame_biergeballer_caption),
    BIERRUTSCHE(BierrutscheActivity.class, R.drawable.circle_of_death_screen, R.string.minigame_bierrutsche_caption),
    //TODO: Bild ändern
    BUSFAHREN(BusfahrenActivity.class, R.drawable.busfahrer_screen, R.string.minigame_busfahren_caption),
    ICH_HAB_NOCH_NIE(IchHabNochNieActivity.class, R.drawable.ich_hab_nie_screen, R.string
            .minigame_ich_hab_noch_nie_caption),
    KINGS(KingsActivity.class, R.drawable.kings_screen, R.string.minigame_kings_caption),
    KISTEN_STAPELN(KistenStapelnActivity.class, R.drawable.kistenstapeln_screen, R.string
            .minigame_kisten_stapeln_caption),
    WERF_DICH_DICHT(WerfDichDichtActivity.class, R.drawable.werf_dich_dicht_screen, R.string
            .minigame_werf_dich_dicht_caption);

    private Class<? extends BaseMiniGame> activity;
    private int screenshotId;
    private int nameId;

    MiniGame(Class<? extends BaseMiniGame> activity, int screenshotId, int nameId) {
        this.activity = activity;
        this.screenshotId = screenshotId;
        this.nameId = nameId;
    }

    public Class<? extends BaseMiniGame> getActivity() {
        return this.activity;
    }

    public int getScreenshotId() {
        return this.screenshotId;
    }

    public int getNameId() {
        return this.nameId;
    }
}
