package de.tomade.saufomat2.activity.mainGame;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.model.task.Task;


public class MainGameActivity extends Activity {

    private static final String TAG  = MainGameActivity.class.getSimpleName();
    private ArrayList<Player> players;
    private Player currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.players = extras.getParcelableArrayList("player");
            this.currentPlayer = (Player) extras.getSerializable("currentPlayer");
            setContentView(new MainGamePanel(this, currentPlayer, players));
        }
        else{
            //TODO: später entfernen wenn Spieler von Mark übergeben werden
            setContentView(new MainGamePanel(this));
        }
    }

    @Override
    protected void onDestroy(){
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop(){
        Log.d(TAG, "Stopping...");
        super.onStop();
    }
}
