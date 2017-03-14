package de.tomade.saufomat2.activity.miniGames.augensaufen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.ChooseMiniGameActivity;
import de.tomade.saufomat2.activity.miniGames.BaseMiniGame;
import de.tomade.saufomat2.constant.IntentParameter;
import de.tomade.saufomat2.constant.MiniGame;

//TODO: Rundenzähler fehlt noch
public class AugensaufenActivity extends BaseMiniGame implements View.OnClickListener {
    private static final String TAG = AugensaufenActivity.class.getSimpleName();
    private static final int DICE_ROLL_DELAY = 100;
    private static Random random;
    private AugensaufenState gameState = AugensaufenState.START;
    private ImageView diceImage;
    private TextView bottomText;
    private TextView playerText;
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

        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);
        if (this.fromMainGame) {
            backButton.setVisibility(View.GONE);
            TextView backText = (TextView) this.findViewById(R.id.backText);
            backText.setVisibility(View.GONE);
        }
        backButton.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "Touch, state: " + this.gameState + " action: " + event.getAction());
        if (event.getAction() == 0) {
            switch (this.gameState) {
                case START:
                    if (this.turnCount == this.playerList.size()) {
                        this.leaveGame();
                    } else {
                        this.startRolling();
                    }
                    break;
                case ROLLING:
                    this.stopRolling();
                    break;
                case RESULT:
                    this.restart();
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void stopRolling() {
        this.gameState = AugensaufenState.RESULT;
        this.bottomText.setText(this.getString(R.string.minigame_augensaufen_drink_amount, (this.currentDiceIndex +
                1)));
        if (this.fromMainGame) {
            if (this.currentPlayer == null) {
                throw new IllegalStateException("currentPlayer cannot be Null");
            }
            this.currentPlayer.setDrinks(this.currentPlayer.getDrinks() + this.currentDiceIndex + 1);
        }
    }

    private void restart() {
        this.bottomText.setText(R.string.minigame_augensaufen_tap_to_start);
        if (!this.fromMainGame) {
            this.playerText.setText(R.string.minigame_augensaufen_next_player);
        } else {
            this.nextTurn();
            this.turnCount++;

            if (this.turnCount == this.playerList.size()) {
                this.playerText.setText(R.string.minigame_augensaufen_game_over);
            } else {
                this.playerText.setText(this.currentPlayer.getName());
            }
        }
        this.gameState = AugensaufenState.START;
    }

    private void startRolling() {
        this.gameState = AugensaufenState.ROLLING;
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
            Intent intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
            intent.putExtra(IntentParameter.LAST_GAME, MiniGame.AUGENSAUFEN);
            this.startActivity(intent);
        }
    }
}
