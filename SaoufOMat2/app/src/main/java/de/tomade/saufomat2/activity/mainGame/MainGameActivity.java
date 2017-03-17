package de.tomade.saufomat2.activity.mainGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import de.tomade.saufomat2.activity.mainGame.task.Task;
import de.tomade.saufomat2.constant.IntentParameter;
import de.tomade.saufomat2.constant.MiniGame;
import de.tomade.saufomat2.model.Player;


public class MainGameActivity extends Activity {

    private static final String TAG = MainGameActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams
                .FLAG_FULLSCREEN);

        MainGamePanel panel;
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            ArrayList<Player> players = (ArrayList<Player>) extras.getSerializable(IntentParameter.PLAYER_LIST);
            Player currentPlayer = (Player) extras.getSerializable(IntentParameter.CURRENT_PLAYER);
            panel = new MainGamePanel(this, currentPlayer, players);
            this.setContentView(panel);

        } else {
            //Wenn kein Spieler im Spielermenü eingetragen wurde
            Player p0 = new Player();
            Player p1 = new Player();

            p0.setName("p0");
            p0.setLastPlayer(p1);
            p0.setWeight(80);
            p0.setNextPlayer(p1);
            p0.setIsMan(true);
            p0.setDrinks(0);

            p1.setName("p1");
            p1.setLastPlayer(p0);
            p1.setWeight(85);
            p1.setNextPlayer(p0);
            p1.setIsMan(false);
            p1.setDrinks(0);

            ArrayList<Player> players = new ArrayList<>();
            players.add(p0);
            players.add(p1);
            panel = new MainGamePanel(this, p0, players);
            this.setContentView(panel);

        }
    }

    public void changeToTaskViewWithTask(Task currentTask, ArrayList<Player> player, Player currentPlayer) {
        Intent intent = new Intent(this.getApplicationContext(), TaskViewActivity.class);
        intent.putExtra(IntentParameter.MainGame.CURRENT_TASK, currentTask);
        intent.putExtra(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME, false);
        this.changeView(intent, player, currentPlayer);
    }

    public void changeToTaskViewWithGame(MiniGame miniGame, ArrayList<Player> player, Player currentPlayer) {
        Intent intent = new Intent(this.getApplicationContext(), TaskViewActivity.class);
        intent.putExtra(IntentParameter.MainGame.CURRENT_MINI_GAME, miniGame);
        intent.putExtra(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME, true);
        this.changeView(intent, player, currentPlayer);
    }

    private void changeView(Intent intent, ArrayList<Player> playerList, Player currentPlayer) {

        intent.putExtra(IntentParameter.PLAYER_LIST, playerList);
        intent.putExtra(IntentParameter.CURRENT_PLAYER, currentPlayer);
        this.finish();
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }
}
