package com.tomade.saufomat.activity.miniGame;

import android.content.Intent;
import android.os.Bundle;

import com.tomade.saufomat.activity.BasePresenter;
import com.tomade.saufomat.activity.ChooseMiniGameActivity;
import com.tomade.saufomat.activity.mainGame.MainGameActivity;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.constant.MiniGame;
import com.tomade.saufomat.model.player.Player;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

/**
 * Basisklasse für Minispiel-Presenter
 * Created by woors on 03.08.2017.
 */

public class BaseMiniGamePresenter<ACTIVITY extends BaseMiniGameActivity> extends BasePresenter<ACTIVITY> {
    protected ArrayList<Player> playerList;
    protected Player currentPlayer;
    protected boolean fromMainGame;

    public BaseMiniGamePresenter(ACTIVITY activity) {
        super(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle extras = this.activity.getIntent().getExtras();
        if (extras != null) {
            this.fromMainGame = extras.getBoolean(IntentParameter.FROM_MAIN_GAME);
            if (this.fromMainGame) {
                this.playerList = (ArrayList<Player>) extras.getSerializable(IntentParameter.PLAYER_LIST);
                this.currentPlayer = (Player) extras.getSerializable(IntentParameter.CURRENT_PLAYER);
            }
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
            this.nextPlayer();
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
            intent.putExtra(IntentParameter.CURRENT_PLAYER, this.currentPlayer);
        }
        this.activity.finish();
        this.activity.startActivity(intent);
    }

    private MiniGame getThisGame() {
        Set<MiniGame> allMiniGames = EnumSet.allOf(MiniGame.class);
        for (MiniGame miniGame : allMiniGames) {
            if (miniGame.getActivity().equals(this.activity.getClass())) {
                return miniGame;
            }
        }
        throw new IllegalStateException("Activity [" + this.activity.getClass() + "] is not defined in Enum " +
                MiniGame.class
                        .getName());
    }


    public String getCurrentPlayerName() {
        return this.currentPlayer.getName();
    }

    public void increaseCurrentPlayerDrink(int amount) {
        this.currentPlayer.increaseDrinks(amount);
    }

    public boolean isFromMainGame() {
        return this.fromMainGame;
    }

    public int getPlayerAmount() {
        return this.playerList.size();
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }
}
