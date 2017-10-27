package com.tomade.saufomat.activity.mainGame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.ActivityWithPlayer;
import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.TaskDrinkHandler;
import com.tomade.saufomat.activity.mainGame.task.TaskEvent;
import com.tomade.saufomat.activity.mainGame.task.TaskTarget;
import com.tomade.saufomat.activity.mainGame.task.TimedTask;
import com.tomade.saufomat.activity.mainManue.MainMenuActivity;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.constant.MiniGame;
import com.tomade.saufomat.model.player.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Die Ansicht, in der die Aufgabe angezeigt wird
 */
public class TaskViewActivity extends Activity implements View.OnClickListener, ActivityWithPlayer {
    private static final String TAG = TaskViewActivity.class.getSimpleName();
    private static final String FORMAT = "%02d:%02d:%03d";
    private static final long LEAVEABLE_DELAY = 1000;
    private boolean timerRunning = false;

    private ArrayList<Player> playerList;
    private Task currentTask;
    private MiniGame miniGame = null;
    private Player currentPlayer;
    private boolean drinkCountShown = false;
    private boolean isGame;
    private int width;
    private int height;
    private RelativeLayout submitButtonLayout;
    private TextView submitButtonText;
    private TextView taskText;
    private InterstitialAd interstitialAd;
    private boolean leaveAbleAfterCountdown = false;
    private boolean alcoholShown = false;

    private boolean currentPlayerIsAviable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_new_task_view);
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

        TextView currentPlayerNameText = this.findViewById(R.id.currentPlayerText);
        currentPlayerNameText.setText(this.currentPlayer.getName());
        ((TextView) this.findViewById(R.id.lastPlayerText)).setText(this.currentPlayer.getLastPlayer().getName());
        ((TextView) this.findViewById(R.id.nextPlayerText)).setText(this.currentPlayer.getNextPlayer().getName());
        this.taskText = this.findViewById(R.id.taskText);
        ImageButton noButton = this.findViewById(R.id.declineButton);
        TextView costText = this.findViewById(R.id.declineButtonText);
        this.submitButtonLayout = this.findViewById(R.id.submitButtonLayout);
        this.submitButtonText = this.findViewById(R.id.submitButtonText);
        int cost = 0;

        if (this.isGame) {
            this.taskText.setText(String.format("Minispiel:\n%s", this.getString(this.miniGame.getNameId())));
            this.submitButtonText.setText(R.string.main_game_lets_go);
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
            this.taskText.setText(taskTextValue);
            cost = this.currentTask.getCost();

            if (this.currentTask.getTaskTarget() == TaskTarget.AD) {
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
        }

        if (cost <= 0) {
            this.hideDeclineButton();
        } else {
            costText.setText(this.getString(R.string.maingame_button_decline, this.currentTask.getCost()));
            noButton.setOnClickListener(this);
        }
        ImageButton yesButton = this.findViewById(R.id.submitButton);
        ImageButton optionsButton = this.findViewById(R.id.optionsButton);
        ImageButton alcoholButton = this.findViewById(R.id.alcoholButton);

        if (this.currentTask instanceof TimedTask) {
            this.submitButtonText.setText(R.string.main_game_lets_go);
        }

        yesButton.setOnClickListener(this);
        optionsButton.setOnClickListener(this);
        alcoholButton.setOnClickListener(this);
    }

    private void hideDeclineButton() {
        this.findViewById(R.id.declineButtonLayout).setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams;
        layoutParams = (RelativeLayout.LayoutParams) this.submitButtonLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitButton:
                this.submitButtonPressed();
                break;
            case R.id.declineButton:
                this.declineButtonPressed();
                break;
            case R.id.optionsButton:
                this.optionsButtonPressed();
                break;
            case R.id.alcoholButton:
                this.alcoholButtonPressed();
                break;
        }
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

    private void submitButtonPressed() {
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
        } else if (this.timerRunning || this.leaveAbleAfterCountdown) {
            if (this.timerRunning) {
                this.wonTimer((TimedTask) this.currentTask);
            } else {
                this.openMainView();
            }
        } else {
            boolean switchToMainView = true;
            TaskEvent newTaskEvent;
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
                    this.startTimer();
                }
            }
            if (switchToMainView) {
                this.openMainView();
            }
        }
    }

    private void startTimer() {
        this.submitButtonText.setText("Fertig!");
        this.hideDeclineButton();
        this.timerRunning = true;
        final TimedTask timedTask = (TimedTask) this.currentTask;
        new CountDownTimer(timedTask.getTime(), 50) {
            public void onTick(long millisUntilFinished) {
                if (TaskViewActivity.this.timerRunning) {
                    setTimeView(millisUntilFinished);
                }
            }

            public void onFinish() {
                if (TaskViewActivity.this.timerRunning) {
                    timeOut(timedTask);
                }
            }
        }.start();
    }

    private void setTimeView(long millisUntilFinished) {
        this.taskText.setText("" + String.format(FORMAT, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit
                        .MILLISECONDS.toMinutes(millisUntilFinished)),
                (TimeUnit.MILLISECONDS.toMicros(millisUntilFinished) - TimeUnit.SECONDS.toMicros(TimeUnit.MILLISECONDS
                        .toSeconds(millisUntilFinished))) / 1000));
    }

    private void timeOut(TimedTask timedTask) {
        this.timerRunning = false;
        this.taskText.setText(timedTask.getTaskIfLost().getText());
        TaskDrinkHandler.increaseDrinks(timedTask.getTaskIfLost(), this);
        this.startLeavingCounter();
    }

    private void wonTimer(TimedTask timedTask) {
        this.timerRunning = false;
        this.taskText.setText(timedTask.getTaskIfWon().getText());
        TaskDrinkHandler.increaseDrinks(timedTask.getTaskIfWon(), this);
        this.startLeavingCounter();
    }

    private void startLeavingCounter() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TaskViewActivity.this.leaveAbleAfterCountdown = true;
                TaskViewActivity.this.submitButtonLayout.setVisibility(View.VISIBLE);
                TaskViewActivity.this.submitButtonText.setText("Weiter");
            }
        }, LEAVEABLE_DELAY);
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

    private void declineButtonPressed() {
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
        View taskLayout = this.findViewById(R.id.taskLayout);
        View alcoholLayout = this.findViewById(R.id.alcoholLayout);
        this.alcoholShown = !this.alcoholShown;
        if (this.alcoholShown) {
            taskLayout.setVisibility(View.GONE);
            alcoholLayout.setVisibility(View.VISIBLE);
            TextView alcoholTextView = this.findViewById(R.id.alcoholText);
            StringBuilder alcoholText = new StringBuilder("Getränkezähler\n\n");

            Player player = this.currentPlayer;
            do {
                alcoholText.append(player.getName()).append(" Drinks: ").append(player.getDrinks()).append(", " +
                        "Promille: ").append(new DecimalFormat("0.00").format(this.calculateAlcohol(player))).append
                        ("‰");
                player = player.getNextPlayer();
                if (player != this.currentPlayer) {
                    alcoholText.append("\n");
                }
            } while (player != this.currentPlayer);

            alcoholTextView.setText(alcoholText);
        } else {
            taskLayout.setVisibility(View.VISIBLE);
            alcoholLayout.setVisibility(View.GONE);
        }
    }

    private float calculateAlcohol(Player player) {
        float alcPercent = 0.18f;
        int amount = 20;
        float alc = amount * alcPercent * player.getDrinks() * 0.81f;

        float reducedWight;
        if (player.getIsMan()) {
            reducedWight = player.getWeight() * 0.7f;
        } else {
            reducedWight = player.getWeight() * 0.6f;
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
