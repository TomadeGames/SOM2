package com.tomade.saufomat.activity.mainGame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.ActivityWithPlayer;
import com.tomade.saufomat.activity.MainMenuActivity;
import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.TaskDrinkHandler;
import com.tomade.saufomat.activity.mainGame.task.TaskTarget;
import com.tomade.saufomat.activity.mainGame.task.TimedTask;
import com.tomade.saufomat.activity.mainGame.task.taskevent.TaskEvent;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.constant.MiniGame;
import com.tomade.saufomat.model.player.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Die Ansicht, in der die Aufgabe angezeigt wird
 */
public class TaskViewActivity extends Activity implements View.OnClickListener, ActivityWithPlayer {
    private static final String TAG = TaskViewActivity.class.getSimpleName();

    private TextView statisticsText;

    private ArrayList<Player> playerList;
    private Task currentTask;
    private MiniGame miniGame = null;
    private Player currentPlayer;
    private boolean drinkCountShown = false;
    private boolean isGame;
    private int width;
    private int height;
    private InterstitialAd interstitialAd;

    private boolean currentPlayerIsAviable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_task_view);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Point size = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        this.width = size.x;
        this.height = size.y;

        Bundle extras = this.getIntent().getExtras();

        this.playerList = (ArrayList<Player>) extras.getSerializable(IntentParameter.PLAYER_LIST);
        this.currentPlayer = (Player) extras.getSerializable(IntentParameter.CURRENT_PLAYER);

        if (!extras.getBoolean(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME)) {
            this.currentTask = (Task) extras.getSerializable(IntentParameter.MainGame.CURRENT_TASK);
            this.isGame = false;
        } else {
            this.miniGame = (MiniGame) extras.getSerializable(IntentParameter.MainGame.CURRENT_MINI_GAME);
            this.isGame = true;
        }

        this.statisticsText = this.findViewById(R.id.statisticText);
        TextView currentPlayerNameText = this.findViewById(R.id.currentPlayerNameText);
        currentPlayerNameText.setText(this.currentPlayer.getName());
        TextView taskText = this.findViewById(R.id.taskText);
        ImageButton noButton = this.findViewById(R.id.declineButton);
        TextView costText = this.findViewById(R.id.costText);
        int cost = 0;

        if (this.isGame) {
            taskText.setText(String.format("Minispiel:\n%s", this.getString(this.miniGame.getNameId())));
        } else {
            TaskDifficult difficult = this.currentTask.getDifficult();
            String taskTextValue = "";
            switch (difficult) {
                case EASY_WIN:
                case MEDIUM_WIN:
                case HARD_WIN:
                    taskTextValue = this.getString(R.string.maingame_task_jackpot);
                    break;
            }
            taskTextValue += this.currentTask.getParsedText(this.currentPlayer);
            taskText.setText(taskTextValue);
            cost = this.currentTask.getCost();

            if (this.currentTask.getTaskTarget() == TaskTarget.AD) {
                this.interstitialAd = new InterstitialAd(this);
                this.interstitialAd.setAdUnitId(this.getString(R.string.maingame_ad_id));
                this.interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        openMainView();
                    }
                });
                AdRequest adRequest = new AdRequest.Builder().build();
                this.interstitialAd.loadAd(adRequest);
            }
        }

        if (cost <= 0) {
            noButton.setImageResource(R.drawable.gray_button);
            costText.setVisibility(View.INVISIBLE);
        } else {
            costText.setText(this.getString(R.string.maingame_button_decline, this.currentTask.getCost()));
            noButton.setOnClickListener(this);
        }

        this.alcoholButtonPressed();

        ImageButton yesButton = this.findViewById(R.id.acceptButton);
        ImageButton optionsButton = this.findViewById(R.id.optionsButton);
        ImageButton alcoholButton = this.findViewById(R.id.alcoholButton);

        yesButton.setOnClickListener(this);
        optionsButton.setOnClickListener(this);
        alcoholButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acceptButton:
                this.yesButtonPressed();
                break;
            case R.id.declineButton:
                this.noButtonPressed();
                break;
            case R.id.optionsButton:
                this.optionsButtonPressed();
                break;
            case R.id.alcoholButton:
                this.alcoholButtonPressed();
                break;
        }
    }

    private void openTimerView(TimedTask timedTask) {
        Log.i(TAG, "Switching to TimerView");
        Intent intent = new Intent(this, TaskTimerActivity.class);
        intent.putExtra(IntentParameter.CURRENT_PLAYER, this.currentPlayer);
        intent.putExtra(IntentParameter.PLAYER_LIST, this.playerList);
        intent.putExtra(IntentParameter.TaskTimer.TIME, timedTask.getTime());
        intent.putExtra(IntentParameter.TaskTimer.TASK_IF_WON, timedTask.getTaskIfWon());
        intent.putExtra(IntentParameter.TaskTimer.TASK_IF_LOST, timedTask.getTaskIfLost());
        this.finish();
        this.startActivity(intent);
    }

    private void openMainView(TaskEvent newTaskEvent) {
        Log.i(TAG, "Switching to MainView");
        this.currentPlayer = this.currentPlayer.getNextPlayer();
        Intent intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
        intent.putExtra(IntentParameter.CURRENT_PLAYER, this.currentPlayer);
        intent.putExtra(IntentParameter.PLAYER_LIST, this.playerList);
        intent.putExtra(IntentParameter.MainGame.NEW_TASK_EVENT, newTaskEvent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.finish();
        this.startActivity(intent);
    }

    private void openMainView() {
        this.openMainView(null);
    }

    private void yesButtonPressed() {
        Log.d(TAG, "Yes Button Pressed");
        if (this.isGame) {
            Intent intent = new Intent(this, this.miniGame.getActivity());
            intent.putExtra(IntentParameter.FROM_MAIN_GAME, true);
            intent.putExtra(IntentParameter.PLAYER_LIST, this.playerList);
            intent.putExtra(IntentParameter.CURRENT_PLAYER, this.currentPlayer);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            this.finish();
            Log.i(TAG, "Starting Minigame " + this.miniGame);
            this.startActivity(intent);
        } else {
            boolean switchToMainView = true;
            TaskEvent newTaskEvent = null;
            if (this.currentPlayerIsAviable) {
                TaskDrinkHandler.increaseDrinks(this.currentTask, this);

                switch (this.currentTask.getTaskTarget()) {
                    case SWITCH_PLACE_LEFT:
                        this.switchPlayers(this.currentPlayer, this.currentPlayer.getNextPlayer());
                        break;
                    case SWITCH_PLACE_RIGHT:
                        this.switchPlayers(this.currentPlayer, this.currentPlayer.getLastPlayer());
                        break;
                    case AD:
                        switchToMainView = false;
                        if (this.interstitialAd.isLoaded()) {
                            this.interstitialAd.show();
                        } else {
                            this.openMainView();
                        }
                        break;
                }
                if (this.currentTask instanceof TaskEvent) {
                    newTaskEvent = (TaskEvent) this.currentTask;
                    switchToMainView = false;
                    this.openMainView(newTaskEvent);
                }
                if (this.currentTask instanceof TimedTask) {
                    switchToMainView = false;
                    this.openTimerView((TimedTask) this.currentTask);
                }
            }
            if (switchToMainView) {
                this.openMainView();
            }
        }
    }

    private void switchPlayers(Player player1, Player player2) {
        Player p1Next = player1.getNextPlayer();
        Player p1Last = player1.getLastPlayer();
        Player p2Next = player2.getNextPlayer();
        Player p2Last = player2.getLastPlayer();

        if (p2Next != player1) {
            player1.setNextPlayer(p2Next);
            p2Next.setLastPlayer(player1);
        } else {
            player1.setNextPlayer(player2);
            player2.setLastPlayer(player1);
        }

        if (p2Last != player1) {
            player1.setLastPlayer(p2Last);
            p2Last.setNextPlayer(player1);
        } else {
            player1.setLastPlayer(player2);
            player2.setNextPlayer(player1);
        }

        if (p1Next != player2) {
            player2.setNextPlayer(p1Next);
            p1Next.setLastPlayer(player2);
        } else {
            player2.setNextPlayer(player1);
            player1.setLastPlayer(player2);
        }

        if (p1Last != player2) {
            player2.setLastPlayer(p1Last);
            p1Last.setNextPlayer(player2);
        } else {
            player2.setLastPlayer(player1);
            player1.setNextPlayer(player2);
        }
    }

    private void noButtonPressed() {
        Log.i(TAG, "NoButton pressed");
        if (this.currentPlayerIsAviable) {
            this.currentPlayer.setDrinks(this.currentPlayer.getDrinks() + this.currentTask.getCost());
            Log.d(TAG, this.currentPlayer.getName() + "s drinks increased with " + this.currentTask.getCost() + " " +
                    "drinkcount is now " + this.currentPlayer.getDrinks());
        }
        this.openMainView();
    }

    //TODO
    private void optionsButtonPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault)
        );
        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.maingame_options_dialog, null);

        builder.setView(view);
        ImageButton abortButton = view.findViewById(R.id.abortButton);
        ImageButton acceptButton = view.findViewById(R.id.acceptButton);
        ImageButton closeButton = view.findViewById(R.id.closeButton);
        ImageButton addPlayerButton = view.findViewById(R.id.addPlayerButton);
        ImageButton removePlayerButton = view.findViewById(R.id.removePlayerButton);


        AlertDialog dialog = builder.create();
        OptionsHandler optionsHandler = new OptionsHandler(this, dialog);
        abortButton.setOnClickListener(optionsHandler);
        acceptButton.setOnClickListener(optionsHandler);
        closeButton.setOnClickListener(optionsHandler);
        addPlayerButton.setOnClickListener(optionsHandler);
        removePlayerButton.setOnClickListener(optionsHandler);
        dialog.show();
        dialog.getWindow().setLayout(this.width, this.height);
    }

    private void alcoholButtonPressed() {
        this.drinkCountShown = !this.drinkCountShown;
        Player player = this.currentPlayer;
        String statisticValue = "";
        do {
            String playerText;
            if (this.drinkCountShown) {
                playerText = player.getName() + ": " + player.getDrinks();
            } else {
                float alc = this.calculateAlcohol(player.getDrinks(), player.getWeight(), player.getIsMan());
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                playerText = player.getName() + ": " + decimalFormat.format(alc) + " ‰";
            }
            statisticValue += playerText + "\n";
            player = player.getNextPlayer();

        } while (player != this.currentPlayer);

        this.statisticsText.setText(statisticValue);
    }

    private float calculateAlcohol(int drinks, int weight, boolean isMan) {
        float alcPercent = 0.18f;
        int amount = 20;
        float alc = amount * alcPercent * drinks * 0.81f;

        float reducedWight;
        if (isMan) {
            reducedWight = weight * 0.7f;
        } else {
            reducedWight = weight * 0.6f;
        }
        float erg = alc / reducedWight;
        erg -= erg * 0.2f;
        return erg;
    }

    public void closeGame() {
        Log.i(TAG, "Closing Game");
        Intent intent = new Intent(this.getApplicationContext(), MainMenuActivity.class);
        this.finish();
        this.startActivity(intent);
    }

    public void setPlayerList(Player player) {
        this.playerList.clear();
        Player firstPlayer = player;
        do {
            this.playerList.add(player);
            player = player.getNextPlayer();
        } while (player != firstPlayer);

        this.drinkCountShown = !this.drinkCountShown;
        this.alcoholButtonPressed();
    }

    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public boolean arePlayerValid() {
        return true;
    }

    public void nextPlayerFromOptions() {
        this.currentPlayer = this.currentPlayer.getNextPlayer();
        this.currentPlayerIsAviable = false;
    }

    @Override
    public void onBackPressed() {
    }
}
