package de.tomade.saufomat2.activity.mainGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.model.task.Task;


public class MainGameActivity extends Activity {

    private static final String TAG  = MainGameActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            ArrayList<Player> players = extras.getParcelableArrayList("player");
            int currentPlayer = extras.getInt("currentPlayer");
            setContentView(new MainGamePanel(this, currentPlayer, players));
        }
        else{
            //TODO: später entfernen wenn Spieler von Mark übergeben werden
            ArrayList<Player> players = new ArrayList<>();
            Player p0 = new Player();
            p0.setName("TestPlayer0");
            p0.setDrinks(0);
            p0.setGender("m");
            p0.setWeight(70);
            Log.d(TAG, "p0: " + p0.getId());

            Player p1 = new Player();
            p1.setName("TestPlayer1");
            p1.setDrinks(0);
            p1.setGender("w");
            p1.setWeight(75);
            Log.d(TAG, "p1: " + p1.getId());

            Player p2 = new Player();
            p2.setName("TestPlayer2");
            p2.setDrinks(0);
            p2.setGender("m");
            p2.setWeight(80);
            Log.d(TAG, "p2: " + p2.getId());

            Player p3 = new Player();
            p3.setName("TestPlayer3");
            p3.setDrinks(0);
            p3.setGender("w");
            p3.setWeight(65);
            Log.d(TAG, "p3: " + p3.getId());

            p0.setNextPlayerId(p1.getId());
            p0.setLastPlayerId(p3.getId());

            p1.setNextPlayerId(p2.getId());
            p1.setLastPlayerId(p0.getId());

            p2.setNextPlayerId(p3.getId());
            p2.setLastPlayerId(p1.getId());

            p3.setNextPlayerId(p0.getId());
            p3.setLastPlayerId(p3.getId());

            players.add(p0);
            players.add(p1);
            players.add(p2);
            players.add(p3);
            setContentView(new MainGamePanel(this, p0.getId(), players));
        }
    }

    public void changeToTaskView(Task currentTask, ArrayList<Player> player, int currentPlayerId) {
        Intent intent = new Intent(this.getApplicationContext(), TaskViewActivity.class);
        intent.putExtra("task", currentTask);
        intent.putParcelableArrayListExtra("player", player);
        intent.putExtra("currentPlayer", currentPlayerId);
        this.finish();
        this.startActivity(intent);
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
