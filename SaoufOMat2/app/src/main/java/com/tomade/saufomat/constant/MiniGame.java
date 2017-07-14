package com.tomade.saufomat.constant;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.miniGames.BaseMiniGame;
import com.tomade.saufomat.activity.miniGames.augensaufen.AugensaufenActivity;
import com.tomade.saufomat.activity.miniGames.bierrutsche.BierrutscheActivity;
import com.tomade.saufomat.activity.miniGames.busfahren.BusfahrenActivity;
import com.tomade.saufomat.activity.miniGames.ichHabNochNie.IchHabNochNieActivity;
import com.tomade.saufomat.activity.miniGames.kings.KingsActivity;
import com.tomade.saufomat.activity.miniGames.kistenStapeln.KistenStapelnActivity;
import com.tomade.saufomat.activity.miniGames.werfDichDicht.WerfDichDichtActivity;

import java.io.Serializable;

/**
 * Definition der Minispiele
 * Created by woors on 22.03.2016.
 */
public enum MiniGame implements Serializable {
    AUGENSAUFEN(AugensaufenActivity.class, R.drawable.augensaufen_screen, R.string.minigame_augensaufen_caption),
    BIERRUTSCHE(BierrutscheActivity.class, R.drawable.bierrutsche_screenshot, R.string.minigame_bierrutsche_caption),
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