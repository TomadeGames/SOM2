package com.tomade.saufomat.activity.mainGame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.tomade.saufomat.AdService;
import com.tomade.saufomat.MiniGameProvider;
import com.tomade.saufomat.activity.BaseActivity;
import com.tomade.saufomat.activity.BasePresenter;
import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.TaskProvider;
import com.tomade.saufomat.activity.mainGame.task.taskevent.TaskEvent;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.constant.MiniGame;
import com.tomade.saufomat.model.player.Player;
import com.tomade.saufomat.persistance.SaveGameHelper;
import com.tomade.saufomat.persistance.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.Random;

import static com.tomade.saufomat.constant.MainGameIconChances.EASY_CHANCE;
import static com.tomade.saufomat.constant.MainGameIconChances.GAME_CHANCE;
import static com.tomade.saufomat.constant.MainGameIconChances.HARD_CHANCE;
import static com.tomade.saufomat.constant.MainGameIconChances.MEDIUM_CHANCE;

/**
 * Presenter für MainGame
 * Created by woors on 03.08.2017.
 */

public class MainGamePresenter extends BasePresenter {
    private static final String TAG = MainGamePresenter.class.getSimpleName();

    private static final int AD_LIMIT = 7; //Original 8, erstmal 7
    private static int adCounter = 0;

    private Intent taskViewIntent;

    private Player currentPlayer;
    private ArrayList<Player> playerList;
    private TaskDifficult currentDificult;

    private ArrayList<TaskEvent> taskEvents;

    public MainGamePresenter(BaseActivity activity) {
        super(activity);
        this.taskViewIntent = new Intent(this.activity, TaskViewActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle extras = this.activity.getIntent().getExtras();
        this.playerList = (ArrayList<Player>) extras.getSerializable(IntentParameter.PLAYER_LIST);
        this.currentPlayer = (Player) extras.getSerializable(IntentParameter.CURRENT_PLAYER);
        boolean newGame = extras.getBoolean(IntentParameter.MainGame.NEW_GAME);
        TaskEvent newTaskEvent = (TaskEvent) extras.getSerializable(IntentParameter.MainGame.NEW_TASK_EVENT);

        if (newTaskEvent != null) {
            new DatabaseHelper(this.activity).activateTaskEvent(newTaskEvent);
        }

        if (newGame) {
            TaskProvider taskProvider = new TaskProvider(this.activity);
            taskProvider.resetTasks();
            this.taskEvents = new ArrayList<>();
        } else {
            this.taskEvents = new DatabaseHelper(this.activity).getActiveTaskEvents();
        }
        this.initAd();
    }

    public int getCurrentDifficult(IconState leftDifficult, IconState
            middleDifficult, IconState rightDifficult) {
        int saufOMeterEndFrame = 0;

        float difficult = 0;
        int gameCount = 0;
        if (leftDifficult == middleDifficult && middleDifficult == rightDifficult) {
            switch (leftDifficult) {
                case EASY:
                    this.currentDificult = TaskDifficult.EASY_WIN;
                    saufOMeterEndFrame = 7;
                    break;
                case MEDIUM:
                    this.currentDificult = TaskDifficult.MEDIUM_WIN;
                    saufOMeterEndFrame = 8;
                    break;
                case HARD:
                    this.currentDificult = TaskDifficult.HARD_WIN;
                    saufOMeterEndFrame = 9;
                    break;
            }
        }
        IconState[] difficulties = {leftDifficult, middleDifficult, rightDifficult};
        for (int i = 0; i < 3; i++) {
            switch (difficulties[i]) {
                case EASY:
                    difficult += 0.2;
                    break;
                case MEDIUM:
                    difficult += 1.5;
                    break;
                case HARD:
                    difficult += 2.8;
                    break;
                case GAME:
                    gameCount++;
                    break;
            }
        }

        if (leftDifficult == IconState.GAME) {
            gameCount++;
        }
        if (middleDifficult == IconState.GAME) {
            gameCount++;
        }
        if (rightDifficult == IconState.GAME) {
            gameCount++;
        }

        Random random = new Random(System.currentTimeMillis());
        difficult = ((difficult) / (3 - gameCount));
        int tmpDiff = (int) difficult;

        if (random.nextInt(3) < gameCount) {
            this.currentDificult = TaskDifficult.GAME;
        }
        if (difficult >= 0.6) {
            saufOMeterEndFrame++;
        }
        if (difficult >= 0.95) {
            saufOMeterEndFrame++;
        }
        if (difficult >= 1.5) {
            saufOMeterEndFrame++;
        }
        if (difficult >= 2) {
            saufOMeterEndFrame++;
        }
        if (difficult >= 2.2) {
            saufOMeterEndFrame++;
        }

        if (tmpDiff > 2) {
            tmpDiff = 2;
        }
        if (tmpDiff < 0) {
            tmpDiff = 0;
        }
        if (this.currentDificult == null) {
            switch (tmpDiff) {
                case 0:
                    this.currentDificult = TaskDifficult.EASY;
                    break;
                case 1:
                    this.currentDificult = TaskDifficult.MEDIUM;
                    break;
                case 2:
                    this.currentDificult = TaskDifficult.HARD;
                    break;
            }

            if (this.currentDificult == TaskDifficult.EASY_WIN) {
                this.increaseEasyWins();
            } else if (this.currentDificult == TaskDifficult.MEDIUM_WIN) {
                this.increaseMediumWins();
            } else if (this.currentDificult == TaskDifficult.HARD_WIN) {
                this.increaseHardWins();
            }
        }
        return saufOMeterEndFrame;
    }

    public void saveGame(Context context, final int adCounter, final Player currentPlayer, final MiniGame
            currentMiniGame) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.miniGameUsed(currentMiniGame);

        this.saveGame(context, adCounter, currentPlayer, databaseHelper);
    }

    public void saveGame(final Context context, final int adCounter, final Player currentPlayer) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);

        this.saveGame(context, adCounter, currentPlayer, databaseHelper);
    }

    private void saveGame(final Context context, final int adCounter, final Player currentPlayer,
                          DatabaseHelper databaseHelper) {
        Player player = currentPlayer;
        do {
            databaseHelper.updatePlayer(player);
            player = player.getNextPlayer();
        } while (player != currentPlayer);

        databaseHelper.updateTaskEvents(this.taskEvents);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SaveGameHelper saveGameHelper = new SaveGameHelper(context);
                saveGameHelper.saveCurrentPlayer(currentPlayer);
                saveGameHelper.saveAdCounter(adCounter);
                saveGameHelper.saveGameSaved(true);
            }
        }).start();
    }

    private void initAd() {
        AdService.initializeInterstitialAd(this.activity);
        AdService.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                adCounter = 0;
                changeView();
            }
        });
    }

    public void changeToTaskView() {
        TaskEvent firedEvent = null;

        for (TaskEvent taskEvent : this.taskEvents) {
            taskEvent.increaseTaskToEventCounter();
            if (firedEvent == null) {
                if (taskEvent.checkIfEventFired()) {
                    firedEvent = taskEvent;
                }
            }
        }


        //Wenn Event drann ist und kein Minispiel oder Hauptgewinn ist wird es ausgelöst
        if (firedEvent != null && !this.isDifficultWin() && this.currentDificult != TaskDifficult.GAME) {
            Task firedTask = firedEvent.fireEvent();
            if (firedEvent.isFinished()) {
                this.taskEvents.remove(firedEvent);
                new DatabaseHelper(this.activity).deactivateTaskEvent(firedEvent);
            }

            this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK, firedTask);
            this.saveGame(this.activity, adCounter, this.currentPlayer);
            this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME, false);
        } else {
            if (this.currentDificult == TaskDifficult.GAME) {
                MiniGame miniGame = new MiniGameProvider(this.activity).getRandomMiniGame(this.playerList.size());
                this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_MINI_GAME, miniGame);
                this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME, true);
                this.saveGame(this.activity, adCounter, this.currentPlayer, miniGame);
            } else {
                Task task = new TaskProvider(this.activity).getNextTask(this.currentDificult);
                this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK, task);
                this.saveGame(this.activity, adCounter, this.currentPlayer);
                this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME, false);
            }
        }

        new DatabaseHelper(this.activity).updateTaskEvents(this.taskEvents);

        this.taskViewIntent.putExtra(IntentParameter.PLAYER_LIST, this.playerList);
        this.taskViewIntent.putExtra(IntentParameter.CURRENT_PLAYER, this.currentPlayer);
        Log.d(TAG, "AdCounter is " + adCounter + "/" + AD_LIMIT);
        if (adCounter >= AD_LIMIT) {
            adCounter = 0;

            final InterstitialAd interstitialAd = AdService.getInterstitialAd();

            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                        Log.i(TAG, "InterstitialAd successful shown");
                        AdService.requestAd();
                    } else {
                        Log.e(TAG, "Ad cannot be shown");
                        AdService.requestAd();
                        changeView();
                    }
                }
            });
        } else {
            adCounter++;
            Log.d(TAG, "Adcounter increased: " + adCounter);
            this.changeView();
        }
    }

    private void changeView() {
        this.taskViewIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.taskViewIntent.putExtra(IntentParameter.MainGame.AD_COUNTER, adCounter);
        this.activity.finish();
        this.activity.startActivity(this.taskViewIntent);
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void increaseEasyWins() {
        this.currentPlayer.getStatistic().increaseEasyWins();
    }

    public void increaseMediumWins() {
        this.currentPlayer.getStatistic().increaseEasyWins();
    }

    public void increaseHardWins() {
        this.currentPlayer.getStatistic().increaseEasyWins();
    }

    public IconState getRandomIconState() {
        Random random = new Random(System.currentTimeMillis());
        int fullChance = EASY_CHANCE + MEDIUM_CHANCE + HARD_CHANCE + GAME_CHANCE;

        int value = random.nextInt(fullChance);
        if (value < EASY_CHANCE) {
            return IconState.EASY;
        } else if (value < EASY_CHANCE + MEDIUM_CHANCE) {
            return IconState.MEDIUM;
        } else if (value < EASY_CHANCE + MEDIUM_CHANCE + HARD_CHANCE) {
            return IconState.HARD;
        } else {
            return IconState.GAME;
        }
    }

    public boolean isDifficultWin() {
        return this.currentDificult == TaskDifficult.EASY_WIN || this.currentDificult == TaskDifficult.MEDIUM_WIN ||
                this.currentDificult == TaskDifficult.HARD_WIN;
    }
}
