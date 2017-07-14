package com.tomade.saufomat.activity.mainGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.tomade.saufomat.AdService;
import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskProvider;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.constant.MiniGame;
import com.tomade.saufomat.model.Player;

import java.util.ArrayList;


public class MainGameActivity extends Activity {

    private static final String TAG = MainGameActivity.class.getSimpleName();
    private static final int AD_LIMIT = 7; //Original 8, erstmal 7
    private static int adCounter = 0;

    private final Intent taskViewIntent = new Intent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "MainGameActivity will be created");
        this.taskViewIntent.setClass(this.getApplicationContext(), TaskViewActivity.class);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams
                .FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        AdService.initializeInterstitialAd(this);
        AdService.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                adCounter = 0;
                changeView();
            }
        });

        MainGamePanel panel;

        Bundle extras = this.getIntent().getExtras();
        ArrayList<Player> players = (ArrayList<Player>) extras.getSerializable(IntentParameter.PLAYER_LIST);
        Player currentPlayer = (Player) extras.getSerializable(IntentParameter.CURRENT_PLAYER);
        adCounter = extras.getInt(IntentParameter.MainGame.AD_COUNTER);
        boolean newGame = extras.getBoolean(IntentParameter.MainGame.NEW_GAME);

        if (newGame) {
            TaskProvider taskProvider = new TaskProvider(this);
            taskProvider.resetTasks();
            panel = new MainGamePanel(this, currentPlayer, players);
        } else {
            panel = new MainGamePanel(this, currentPlayer, players);
        }
        this.setContentView(panel);
        Log.i(TAG, "ContentView set");
    }

    public void changeToTaskViewWithTask(Task currentTask, ArrayList<Player> playerList, Player currentPlayer) {
        this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK, currentTask);
        this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME, false);
        this.taskViewIntent.putExtra(IntentParameter.PLAYER_LIST, playerList);
        this.taskViewIntent.putExtra(IntentParameter.CURRENT_PLAYER, currentPlayer);
        if (adCounter >= AD_LIMIT) {
            adCounter = 0;
            if (!AdService.showAd()) {
                Log.e(TAG, "Ad cannot be shown");
                this.changeView();
            }
        } else {
            adCounter++;
            this.changeView();
        }
    }

    public void changeToTaskViewWithGame(MiniGame miniGame, ArrayList<Player> playerList, Player currentPlayer) {
        this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_MINI_GAME, miniGame);
        this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME, true);
        this.taskViewIntent.putExtra(IntentParameter.PLAYER_LIST, playerList);
        this.taskViewIntent.putExtra(IntentParameter.CURRENT_PLAYER, currentPlayer);
        if (adCounter >= AD_LIMIT) {
            if (!AdService.showAd()) {
                this.changeView();
            }
        } else {
            adCounter++;
            this.changeView();
        }
    }


    private void changeView() {
        Log.i(TAG, "Changing to TaskView");
        this.taskViewIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.taskViewIntent.putExtra(IntentParameter.MainGame.AD_COUNTER, adCounter);
        this.finish();
        this.startActivity(this.taskViewIntent);
    }

    public int getAdCounter() {
        return adCounter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onBackPressed() {
    }
}