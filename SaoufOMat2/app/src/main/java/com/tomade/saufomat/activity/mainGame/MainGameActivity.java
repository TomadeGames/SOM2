package com.tomade.saufomat.activity.mainGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tomade.saufomat.R;
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
    private InterstitialAd interstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.taskViewIntent.setClass(this.getApplicationContext(), TaskViewActivity.class);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams
                .FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.interstitialAd = new InterstitialAd(this);
        this.interstitialAd.setAdUnitId(this.getString(R.string.maingame_ad_id));
        this.interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                adCounter = 0;
                changeView();
            }
        });
        if (adCounter == AD_LIMIT) {
            this.requestInterstitialAd();
        }

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
    }

    private void requestInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        this.interstitialAd.loadAd(adRequest);
    }

    public void changeToTaskViewWithTask(Task currentTask, ArrayList<Player> playerList, Player currentPlayer) {
        this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK, currentTask);
        this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME, false);
        this.taskViewIntent.putExtra(IntentParameter.PLAYER_LIST, playerList);
        this.taskViewIntent.putExtra(IntentParameter.CURRENT_PLAYER, currentPlayer);
        if (adCounter >= AD_LIMIT) {
            this.showAd();
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
        this.showAd();
    }

    private void showAd() {
        if (this.interstitialAd.isLoaded()) {
            this.interstitialAd.show();
        } else {
            this.changeView();
        }
    }

    private void changeView() {
        this.taskViewIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.finish();
        this.startActivity(this.taskViewIntent);
    }

    public int getAdCounter() {
        return adCounter;
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


    @Override
    public void onBackPressed() {
    }
}
