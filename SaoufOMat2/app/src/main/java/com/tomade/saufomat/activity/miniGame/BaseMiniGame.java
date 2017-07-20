package com.tomade.saufomat.activity.miniGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.tomade.saufomat.ActivityWithPlayer;
import com.tomade.saufomat.activity.ChooseMiniGameActivity;
import com.tomade.saufomat.activity.mainGame.MainGameActivity;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.constant.MiniGame;
import com.tomade.saufomat.model.Player;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

/**
 * Basisklasse für Minispiele
 * Created by woors on 07.03.2017.
 */

public abstract class BaseMiniGame extends Activity implements ActivityWithPlayer {
    protected boolean fromMainGame;
    protected ArrayList<Player> playerList;
    protected Player currentPlayer;

    public BaseMiniGame() {
        this.fromMainGame = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle extras = this.getIntent().getExtras();
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
    protected void nextPlayer() {
        this.currentPlayer = this.currentPlayer.getNextPlayer();
    }

    /**
     * Setzt den vorherigen Spieler als aktuellen Spieler
     */
    protected void lastPlayer() {
        this.currentPlayer = this.currentPlayer.getLastPlayer();
    }

    /**
     * Verlässt das Spiel und wechselt zum Minispiel-Menü oder Hauptspiel zurück. Dadurch ist auch der nächste
     * Spieler an der Reihe
     */
    protected void leaveGame() {
        Intent intent;
        if (!this.fromMainGame) {
            intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
        } else {
            this.nextPlayer();
            intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
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
        this.finish();
        this.startActivity(intent);
    }

    private MiniGame getThisGame() {
        Set<MiniGame> allMiniGames = EnumSet.allOf(MiniGame.class);
        for (MiniGame miniGame : allMiniGames) {
            if (miniGame.getActivity().equals(this.getClass())) {
                return miniGame;
            }
        }
        throw new IllegalStateException("Activity [" + this.getClass() + "] is not defined in Enum " + MiniGame.class
                .getName());
    }


    @Override
    public void onBackPressed() {
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public boolean arePlayerValid() {
        return this.fromMainGame;
    }

}
