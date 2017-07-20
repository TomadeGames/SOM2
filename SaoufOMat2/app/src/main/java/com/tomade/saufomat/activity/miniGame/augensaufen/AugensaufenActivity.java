package com.tomade.saufomat.activity.miniGame.augensaufen;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.miniGame.BaseMiniGame;

import java.util.Random;

public class AugensaufenActivity extends BaseMiniGame implements View.OnClickListener {
    private static final String TAG = AugensaufenActivity.class.getSimpleName();
    private static final int DICE_ROLL_DELAY = 100;
    private static Random random;
    private AugensaufenState gameState = AugensaufenState.START;
    private ImageView diceImage;
    private TextView bottomText;
    private TextView playerText;
    private TextView turnCounterView;
    private int currentDiceIndex = 0;
    private int turnCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_augensaufen);
        random = new Random();

        this.diceImage = (ImageView) this.findViewById(R.id.diceImage);
        this.bottomText = (TextView) this.findViewById(R.id.bottemLargeText);
        this.playerText = (TextView) this.findViewById(R.id.playerText);
        this.turnCounterView = (TextView) this.findViewById(R.id.turnCounter);


        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);
        if (this.fromMainGame) {
            backButton.setVisibility(View.GONE);
            TextView backText = (TextView) this.findViewById(R.id.backText);
            backText.setVisibility(View.GONE);
            this.playerText.setText(this.currentPlayer.getName());
            this.turnCounterView.setText((this.turnCount + 1) + "/" + this.playerList.size());
        } else {
            this.turnCounterView.setVisibility(View.GONE);
        }
        backButton.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "Touch, state: " + this.gameState + " action: " + event.getAction());
        if (event.getAction() == 0) {
            switch (this.gameState) {
                case START:
                    this.startRolling();
                    break;
                case ROLLING:
                    this.stopRolling();
                    break;
                case RESULT:
                    this.restart();
                    break;
                case END:
                    this.leaveGame();
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void stopRolling() {
        String playerName = "";
        if (this.fromMainGame) {
            playerName = this.currentPlayer.getName();
        }
        this.playerText.setText(playerName);
        this.gameState = AugensaufenState.RESULT;
        this.bottomText.setText(this.getString(R.string.minigame_augensaufen_drink_amount, this.currentDiceIndex + 1));
        if (this.fromMainGame) {
            this.currentPlayer.increaseDrinks(this.currentDiceIndex + 1);
        }
    }

    private void restart() {
        this.bottomText.setText(R.string.minigame_augensaufen_tap_to_start);
        if (!this.fromMainGame) {
            this.playerText.setText(R.string.minigame_augensaufen_next_player);
            this.gameState = AugensaufenState.START;
        } else {
            this.turnCount++;

            if (this.turnCount >= this.playerList.size()) {
                this.bottomText.setText(R.string.minigame_augensaufen_game_over);
                this.playerText.setVisibility(View.GONE);
                this.gameState = AugensaufenState.END;
            } else {
                this.nextPlayer();
                this.turnCounterView.setText((this.turnCount + 1) + "/" + this.playerList.size());
                this.playerText.setText(this.currentPlayer.getName());
                this.gameState = AugensaufenState.START;
            }
        }
    }

    private void startRolling() {
        this.gameState = AugensaufenState.ROLLING;
        this.playerText.setText("");
        this.bottomText.setText(this.getString(R.string.minigame_augensaufen_tap_to_stop));
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AugensaufenActivity.this.gameState == AugensaufenState.ROLLING) {
                    AugensaufenActivity.this.currentDiceIndex = random.nextInt(6);
                    switch (AugensaufenActivity.this.currentDiceIndex) {
                        case 0:
                            AugensaufenActivity.this.diceImage.setImageResource(R.drawable.dice1);
                            break;
                        case 1:
                            AugensaufenActivity.this.diceImage.setImageResource(R.drawable.dice2);
                            break;
                        case 2:
                            AugensaufenActivity.this.diceImage.setImageResource(R.drawable.dice3);
                            break;
                        case 3:
                            AugensaufenActivity.this.diceImage.setImageResource(R.drawable.dice4);
                            break;
                        case 4:
                            AugensaufenActivity.this.diceImage.setImageResource(R.drawable.dice5);
                            break;
                        case 5:
                            AugensaufenActivity.this.diceImage.setImageResource(R.drawable.dice6);
                            break;
                    }
                    mHandler.postDelayed(this, DICE_ROLL_DELAY);
                }
            }
        }, DICE_ROLL_DELAY);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            this.leaveGame();
        }
    }
}
