package com.tomade.saufomat.activity.miniGame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;

import com.tomade.saufomat.activity.BasePresenter;
import com.tomade.saufomat.activity.ChooseMiniGameActivity;
import com.tomade.saufomat.activity.mainGame.MainGameActivity;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.constant.MiniGame;
import com.tomade.saufomat.model.player.Player;
import com.tomade.saufomat.persistance.GameValueHelper;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

/**
 * Basisklasse für Minispiel-Presenter
 * Created by woors on 03.08.2017.
 */

public class BaseMiniGamePresenter<ACTIVITY extends BaseMiniGameActivity> extends BasePresenter<ACTIVITY> {
    private ArrayList<Player> playerList;
    protected Player currentPlayer;
    protected Player currentPlayerAtStart;
    protected boolean fromMainGame;
    private boolean tutorialAlreadySeen = false;

    public BaseMiniGamePresenter(ACTIVITY activity) {
        super(activity);
    }

    @Override
    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        Bundle extras = this.activity.getIntent().getExtras();
        if (extras != null) {
            this.fromMainGame = extras.getBoolean(IntentParameter.FROM_MAIN_GAME);
            if (this.fromMainGame) {
                this.playerList = (ArrayList<Player>) extras.getSerializable(IntentParameter.PLAYER_LIST);
                this.currentPlayer = (Player) extras.getSerializable(IntentParameter.CURRENT_PLAYER);
                this.currentPlayerAtStart = this.currentPlayer;
            }
        }
        this.tutorialAlreadySeen = new GameValueHelper(this.activity).isTutorialSeen(this.getThisGame());
    }

    protected void showTutorialIfFirstStart() {
        if (!this.tutorialAlreadySeen) {
            new GameValueHelper(this.activity).setTutorialSeen(this.getThisGame());
            this.activity.showTutorial();
        }
    }

    /**
     * Setzt den nächsten Spieler als aktuellen Spieler
     */
    public void nextPlayer() {
        this.currentPlayer = this.currentPlayer.getNextPlayer();
    }

    /**
     * Setzt den vorherigen Spieler als aktuellen Spieler
     */
    public void lastPlayer() {
        this.currentPlayer = this.currentPlayer.getLastPlayer();
    }


    /**
     * Verlässt das Spiel und wechselt zum Minispiel-Menü oder Hauptspiel zurück. Dadurch ist auch der nächste
     * Spieler an der Reihe
     */
    public void leaveGame() {
        Intent intent;
        if (!this.fromMainGame) {
            intent = new Intent(this.activity, ChooseMiniGameActivity.class);
        } else {
            intent = new Intent(this.activity, MainGameActivity.class);
        }

        this.leaveGame(intent);
    }

    /**
     * Verlässt das Spiel und wechselt zum Minispiel-Menü oder Hauptspiel zurück. Dadurch ist auch der nächste
     * Spieler an der Reihe.
     *
     * @param intent die Activity die geöffnet werden soll.
     */
    protected void leaveGame(Intent intent) {
        if (!this.fromMainGame) {
            intent.putExtra(IntentParameter.LAST_GAME, this.getThisGame());
        } else {
            intent.putExtra(IntentParameter.PLAYER_LIST, this.playerList);
            intent.putExtra(IntentParameter.CURRENT_PLAYER, this.currentPlayerAtStart.getNextPlayer());
        }
        this.activity.finish();
        this.activity.startActivity(intent);
    }

    /**
     * Gibt das aktuelle Minispiel zurück
     *
     * @return das aktuelle Minispiel
     */
    MiniGame getThisGame() {
        Set<MiniGame> allMiniGames = EnumSet.allOf(MiniGame.class);
        for (MiniGame miniGame : allMiniGames) {
            if (miniGame.getActivity().equals(this.activity.getClass())) {
                return miniGame;
            }
        }
        throw new IllegalStateException("Activity [" + this.activity.getClass() + "] is not defined in Enum " +
                MiniGame.class.getName());
    }

    /**
     * Gibt den Namen des aktuellen Spielers zurück
     *
     * @return der Name des aktuellen Spielers
     */
    public String getCurrentPlayerName() {
        return this.currentPlayer.getName();
    }

    /**
     * Gibt an, ob das Minispiel vom Hauptspiel oder Minispielmenü aufgerufen wurde
     *
     * @return true, wenn es vom Hauptspiel aufgerufen wurde, sonst false
     */
    public boolean isFromMainGame() {
        return this.fromMainGame;
    }

    /**
     * Git die Spielerzahl zurück
     *
     * @return die Spielerzahl
     */
    public int getPlayerCount() {
        return this.playerList.size();
    }

    /**
     * Gibt den aktuellen Spieler zurück
     *
     * @return der aktuelle Spieler
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public Player getCurrentPlayerAtStart() {
        return this.currentPlayerAtStart;
    }
}
