package com.tomade.saufomat.activity.mainGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.ActivityWithPlayer;
import com.tomade.saufomat.activity.mainGame.task.TaskDrinkHandler;
import com.tomade.saufomat.activity.mainGame.task.TimedTask;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.model.player.Player;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TaskTimerActivity extends Activity implements ActivityWithPlayer, View.OnClickListener {
    private static final String FORMAT = "%02d:%02d:%03d";
    private static final long LEAVEABLE_DELAY = 1000;

    private Player currentPlayer;
    private ArrayList<Player> playerList;

    private TimedTask timedTask;

    private TextView timeView;
    private boolean running = true;
    private boolean leaveAble = false;

    private View submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_task_timer);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.timedTask = (TimedTask) extras.getSerializable(IntentParameter.TaskTimer.TIMED_TASK);
            this.currentPlayer = (Player) extras.getSerializable(IntentParameter.CURRENT_PLAYER);
            this.playerList = (ArrayList<Player>) extras.getSerializable(IntentParameter.PLAYER_LIST);
        }

        this.timeView = this.findViewById(R.id.timer);

        new CountDownTimer(this.timedTask.getTime(), 50) {
            public void onTick(long millisUntilFinished) {
                if (TaskTimerActivity.this.running) {
                    setTimeView(millisUntilFinished);
                }
            }

            public void onFinish() {
                if (TaskTimerActivity.this.running) {
                    timeOut();
                }
            }
        }.start();

        this.submitButton = this.findViewById(R.id.submitButton);
        this.submitButton.setOnClickListener(this);
        if (!this.timedTask.isTimerStoppable()) {
            this.submitButton.setVisibility(View.GONE);
        }
    }

    private void setTimeView(long millisUntilFinished) {
        this.timeView.setText("" + String.format(FORMAT, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit
                        .MILLISECONDS.toMinutes(millisUntilFinished)),
                (TimeUnit.MILLISECONDS.toMicros(millisUntilFinished) - TimeUnit.SECONDS.toMicros(TimeUnit.MILLISECONDS
                        .toSeconds(millisUntilFinished))) / 1000));
    }

    private void timeOut() {
        this.running = false;
        this.timeView.setText(this.timedTask.getTaskIfLost().getText());
        TaskDrinkHandler.increaseDrinks(this.timedTask.getTaskIfLost(), this);
        this.startLeavingCounter();
    }

    private void won() {
        this.running = false;
        this.timeView.setText(this.timedTask.getTaskIfWon().getText());
        TaskDrinkHandler.increaseDrinks(this.timedTask.getTaskIfWon(), this);
        this.startLeavingCounter();
    }

    private void startLeavingCounter() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TaskTimerActivity.this.leaveAble = true;
                TaskTimerActivity.this.submitButton.setVisibility(View.VISIBLE);
            }
        }, LEAVEABLE_DELAY);
    }

    private void leaveActivity() {
        this.currentPlayer = this.currentPlayer.getNextPlayer();
        Intent intent = new Intent(this, MainGameActivity.class);
        intent.putExtra(IntentParameter.PLAYER_LIST, this.playerList);
        intent.putExtra(IntentParameter.CURRENT_PLAYER, this.currentPlayer.getNextPlayer());
        this.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitButton:
                if (this.running) {
                    this.won();
                } else if (this.leaveAble) {
                    this.leaveActivity();
                }
                break;
        }
    }

    @Override
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public boolean arePlayerValid() {
        return true;
    }
}
