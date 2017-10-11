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
import com.tomade.saufomat.activity.mainGame.task.SimpleTask;
import com.tomade.saufomat.activity.mainGame.task.TaskDrinkHandler;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.model.player.Player;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TaskTimerActivity extends Activity implements ActivityWithPlayer, View.OnClickListener {
    private static final String FORMAT = "%02d:%02d:%03d";
    private static final long LEAVEABLE_DELAY = 1000;

    private Player currentPlayer;
    private ArrayList<Player> playerList;

    private long time = 1000;
    private SimpleTask taskIfWon;
    private SimpleTask taskIfLost;

    private TextView timeView;
    private boolean running = true;
    private boolean leaveAble = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_task_timer);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.time = extras.getLong(IntentParameter.TaskTimer.TIME);
            this.taskIfWon = (SimpleTask) extras.getSerializable(IntentParameter.TaskTimer.TASK_IF_WON);
            this.taskIfLost = (SimpleTask) extras.getSerializable(IntentParameter.TaskTimer.TASK_IF_LOST);
            this.currentPlayer = (Player) extras.getSerializable(IntentParameter.CURRENT_PLAYER);
            this.playerList = (ArrayList<Player>) extras.getSerializable(IntentParameter.PLAYER_LIST);
        }

        this.timeView = this.findViewById(R.id.timer);

        new CountDownTimer(this.time, 50) {
            public void onTick(long millisUntilFinished) {
                if (TaskTimerActivity.this.running) {
                    setTimeView(millisUntilFinished);
                }
            }

            public void onFinish() {
                if (TaskTimerActivity.this.running) {
                    lost();
                }
            }
        }.start();

        this.findViewById(R.id.submitButton).setOnClickListener(this);
    }

    private void setTimeView(long millisUntilFinished) {
        this.timeView.setText("" + String.format(FORMAT, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit
                        .MILLISECONDS.toMinutes(millisUntilFinished)),
                (TimeUnit.MILLISECONDS.toMicros(millisUntilFinished) - TimeUnit.SECONDS.toMicros(TimeUnit.MILLISECONDS
                        .toSeconds(millisUntilFinished))) / 1000));
    }

    private void lost() {
        this.running = false;
        this.timeView.setText(this.taskIfLost.getText());
        TaskDrinkHandler.increaseDrinks(this.taskIfLost, this);
        this.startLeavingCounter();
    }

    private void won() {
        this.running = false;
        this.timeView.setText(this.taskIfWon.getText());
        TaskDrinkHandler.increaseDrinks(this.taskIfWon, this);
        this.startLeavingCounter();
    }

    private void startLeavingCounter() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TaskTimerActivity.this.leaveAble = true;
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
