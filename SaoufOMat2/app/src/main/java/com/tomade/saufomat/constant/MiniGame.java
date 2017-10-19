package com.tomade.saufomat.constant;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.miniGame.BaseMiniGameActivity;
import com.tomade.saufomat.activity.miniGame.augensaufen.AugensaufenActivity;
import com.tomade.saufomat.activity.miniGame.bierrutsche.BierrutscheActivity;
import com.tomade.saufomat.activity.miniGame.busfahren.BusfahrenActivity;
import com.tomade.saufomat.activity.miniGame.dart.DartsActivity;
import com.tomade.saufomat.activity.miniGame.ichHabNochNie.IchHabNochNieActivity;
import com.tomade.saufomat.activity.miniGame.kings.KingsActivity;
import com.tomade.saufomat.activity.miniGame.kistenStapeln.KistenStapelnActivity;
import com.tomade.saufomat.activity.miniGame.werfDichDicht.WerfDichDichtActivity;

import java.io.Serializable;

/**
 * Definition der Minispiele
 * Created by woors on 22.03.2016.
 */
public enum MiniGame implements Serializable {
    /**
     * Wenn dieses Enum sich ändert muss DATENBANK_VERSION in DatabaseHelper erhöht werden!
     */
    AUGENSAUFEN(AugensaufenActivity.class, R.drawable.augensaufen_screen, R.string.minigame_augensaufen_caption, R
            .string.minigame_augensaufen_tutorial),
    BIERRUTSCHE(BierrutscheActivity.class, R.drawable.bierrutsche_screenshot, R.string.minigame_bierrutsche_caption,
            R.string.minigame_bierrutsche_tutorial, 2),
    BUSFAHREN(BusfahrenActivity.class, R.drawable.busfahrer_screen, R.string.minigame_busfahren_caption, R.string
            .minigame_busfahren_tutorial),
    DARTS(DartsActivity.class, R.drawable.screen_darts, R.string.minigame_darts_caption, R.string
            .minigame_darts_tutorial),
    ICH_HAB_NOCH_NIE(IchHabNochNieActivity.class, R.drawable.ich_hab_nie_screen, R.string
            .minigame_ich_hab_noch_nie_caption, R.string.minigame_ich_hab_noch_nie_tutorial),
    KINGS(KingsActivity.class, R.drawable.kings_screen, R.string.minigame_kings_caption, R.string
            .minigame_kings_tutorial),
    KISTEN_STAPELN(KistenStapelnActivity.class, R.drawable.kistenstapeln_screen, R.string
            .minigame_kisten_stapeln_caption, R.string.minigame_kisten_stapeln_tutorial),
    WERF_DICH_DICHT(WerfDichDichtActivity.class, R.drawable.werf_dich_dicht_screen, R.string
            .minigame_werf_dich_dicht_caption, R.string.minigame_werf_dich_dicht_tutorial);

    private Class<? extends BaseMiniGameActivity> activity;
    private int screenshotId;
    private int nameId;
    private int tutorialId;
    private int playerLimit;

    MiniGame(Class<? extends BaseMiniGameActivity> activity, int screenshotId, int nameId, int tutorialId, int
            playerLimit) {
        this.activity = activity;
        this.screenshotId = screenshotId;
        this.nameId = nameId;
        this.tutorialId = tutorialId;
        this.playerLimit = playerLimit;
    }

    MiniGame(Class<? extends BaseMiniGameActivity> activity, int screenshotId, int nameId, int tutorialId) {
        this(activity, screenshotId, nameId, tutorialId, 1);
    }

    /**
     * Gibt die Activity zu dem Minispiel zurück
     *
     * @return die Activity zum Minispiel
     */
    public Class<? extends BaseMiniGameActivity> getActivity() {
        return this.activity;
    }

    /**
     * Gibt die ID eines Screenshots zu dem Minispiel zurück
     *
     * @return ID eines Screenshots zum Minispiel
     */
    public int getScreenshotId() {
        return this.screenshotId;
    }

    /**
     * Gibt die ID zu den Namen des Minispiels zurück
     *
     * @return die ID zu den Namen des Minipsiels
     */
    public int getNameId() {
        return this.nameId;
    }

    /**
     * Gibt die ID zu den TutorialString zurück
     *
     * @return die Id zu den TutorialString
     */
    public int getTutorialId() {
        return this.tutorialId;
    }

    /**
     * Gibt die minimale Spieleranzahl für das Minispiel zurück
     *
     * @return die minimale Spieleranzahl
     */
    public int getPlayerLimit() {
        return this.playerLimit;
    }
}
