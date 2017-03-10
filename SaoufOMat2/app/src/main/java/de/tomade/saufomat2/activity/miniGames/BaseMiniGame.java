package de.tomade.saufomat2.activity.miniGames;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

import de.tomade.saufomat2.activity.ChooseMiniGameActivity;
import de.tomade.saufomat2.activity.mainGame.MainGameActivity;
import de.tomade.saufomat2.constant.IntentParameter;
import de.tomade.saufomat2.constant.MiniGame;
import de.tomade.saufomat2.model.Player;

/**
 * Basisklasse für Minispiele
 * Created by woors on 07.03.2017.
 */

public abstract class BaseMiniGame extends Activity {
    protected boolean fromMainGame;
    protected ArrayList<Player> playerList;
    protected Player currentPlayer;

    public BaseMiniGame() {
        this.fromMainGame = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.fromMainGame = extras.getBoolean(IntentParameter.FROM_MAIN_GAME);
            if (this.fromMainGame) {
                this.playerList = extras.getParcelableArrayList(IntentParameter.PLAYER_LIST);
                this.currentPlayer = Player.getPlayerById(this.playerList, extras.getInt(IntentParameter
                        .CURRENT_PLAYER_ID));
            }
        }
        Log.d(BaseMiniGame.class.getSimpleName(), "bla");
    }

    /**
     * Setzt den nächsten Spieler als aktuellen Spieler
     */
    protected void nextTurn() {
        this.currentPlayer = Player.getPlayerById(this.playerList, this.currentPlayer.getNextPlayerId());
    }

    /**
     * Setzt den vorherigen Spieler als aktuellen Spieler
     */
    protected void lastTurn() {
        this.currentPlayer = Player.getPlayerById(this.playerList, this.currentPlayer.getLastPlayerId());
    }

    /**
     * Verlässt das Spiel und wechselt zum Minispiel-Menü oder Hauptspiel zurück
     */
    protected void leaveGame() {
        Intent intent;
        if (!this.fromMainGame) {
            intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
            intent.putExtra(IntentParameter.LAST_GAME, this.getThisGame());
        } else {
            intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
            intent.putParcelableArrayListExtra(IntentParameter.PLAYER_LIST, this.playerList);
            intent.putExtra(IntentParameter.CURRENT_PLAYER_ID, this.currentPlayer.getId());
        }
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
}
