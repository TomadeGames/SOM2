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
import com.tomade.saufomat.activity.miniGame.BaseMiniGameActivity;

public class AugensaufenActivity extends BaseMiniGameActivity<AugensaufenPresenter> {
    private static final String TAG = AugensaufenActivity.class.getSimpleName();
    private static final int DICE_ROLL_DELAY = 100;
    private AugensaufenState gameState = AugensaufenState.START;
    private ImageView diceImage;
    private TextView bottomText;
    private TextView playerText;
    private TextView turnCounterView;
    private int currentDiceIndex = 0;
    private int turnCount = 0;

    @Override
    protected void initPresenter() {
        this.presenter = new AugensaufenPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_augensaufen);
        this.diceImage = this.findViewById(R.id.diceImage);
        this.bottomText = this.findViewById(R.id.bottemLargeText);
        this.playerText = this.findViewById(R.id.playerText);
        this.turnCounterView = this.findViewById(R.id.turnCounter);

        ImageButton backButton = this.findViewById(R.id.backButton);
        if (this.presenter.isFromMainGame()) {
            backButton.setVisibility(View.GONE);
            TextView backText = this.findViewById(R.id.backText);
            backText.setVisibility(View.GONE);
            this.playerText.setText(this.presenter.getCurrentPlayer().getName());
            this.turnCounterView.setText((this.turnCount + 1) + "/" + this.presenter.getPlayerAmount());
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
                    this.presenter.leaveGame();
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void stopRolling() {
        String playerName = "";
        if (this.presenter.isFromMainGame()) {
            playerName = this.presenter.getCurrentPlayer().getName();
        }
        this.playerText.setText(playerName);
        this.gameState = AugensaufenState.RESULT;
        this.bottomText.setText(this.getString(R.string.minigame_augensaufen_drink_amount, this.currentDiceIndex + 1));
        if (this.presenter.isFromMainGame()) {
            this.presenter.getCurrentPlayer().increaseDrinks(this.currentDiceIndex + 1);
        }
    }

    private void restart() {
        this.bottomText.setText(R.string.minigame_augensaufen_tap_to_start);
        if (!this.presenter.isFromMainGame()) {
            this.playerText.setText(R.string.minigame_augensaufen_next_player);
            this.gameState = AugensaufenState.START;
        } else {
            this.turnCount++;

            if (this.turnCount >= this.presenter.getPlayerAmount()) {
                this.bottomText.setText(R.string.minigame_augensaufen_game_over);
                this.playerText.setVisibility(View.GONE);
                this.gameState = AugensaufenState.END;
            } else {
                this.presenter.nextPlayer();
                this.turnCounterView.setText((this.turnCount + 1) + "/" + this.presenter.getPlayerAmount());
                this.playerText.setText(this.presenter.getCurrentPlayer().getName());
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
                    AugensaufenActivity.this.currentDiceIndex = AugensaufenActivity.this.presenter.getRandomNumber();
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
}
