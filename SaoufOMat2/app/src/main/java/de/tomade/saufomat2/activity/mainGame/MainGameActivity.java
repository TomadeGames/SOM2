package de.tomade.saufomat2.activity.mainGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import de.tomade.saufomat2.activity.mainGame.task.Task;
import de.tomade.saufomat2.model.Player;


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
            Player p0 = new Player();
            Player p1 = new Player();

            p0.setName("p0");
            p0.setLastPlayerId(p1.getId());
            p0.setWeight(80);
            p0.setNextPlayerId(p1.getId());
            p0.setIsMan(true);
            p0.setDrinks(0);

            p1.setName("p1");
            p1.setLastPlayerId(p0.getId());
            p1.setWeight(85);
            p1.setNextPlayerId(p0.getId());
            p1.setIsMan(false);
            p1.setDrinks(0);

            ArrayList<Player> players = new ArrayList<>();
            players.add(p0);
            players.add(p1);
            int currentPlayer = p0.getId();
            setContentView(new MainGamePanel(this, currentPlayer, players));
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
