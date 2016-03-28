package de.tomade.saufomat2.activity.miniGames.augensaufen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.ChooseMiniGameActivity;
import de.tomade.saufomat2.model.MiniGame;
import de.tomade.saufomat2.model.Player;

public class AugensaufenActivity extends Activity implements View.OnClickListener {
    private static final String TAG = AugensaufenActivity.class.getSimpleName();
    private static final int DICE_ROLL_DELAY = 100;
    private static Random random;
    private AugensaufenState gameState = AugensaufenState.START;
    private ImageView diceImage;
    private TextView bottomText;
    private TextView playerText;
    private boolean fromMenue = false;
    private List<Player> playerList;
    private int currentPlayerId;
    private int currentDiceIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_augensaufen);
        random = new Random();

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.fromMenue = extras.getBoolean("fromMenue");
            if (!fromMenue) {
                playerList = extras.getParcelableArrayList("player");
                currentPlayerId = extras.getInt("currentPlayerId");
            }
        }

        this.diceImage = (ImageView) this.findViewById(R.id.diceImage);
        this.bottomText = (TextView) this.findViewById(R.id.bottemLargeText);
        this.playerText = (TextView) this.findViewById(R.id.playerText);

        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "Touch, state: " + gameState + " action: " + event.getAction());
        if (event.getAction() == 0) {
            switch (this.gameState) {
                case START:
                    startRolling();
                    break;
                case ROLLING:
                    stopRolling();
                    break;
                case RESULT:
                    restart();
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void stopRolling() {
        this.bottomText.setText("Trink " + (this.currentDiceIndex + 1));
        if (!fromMenue) {
            Player currentPlayer = Player.getPlayerById(this.playerList, this.currentPlayerId);
            currentPlayer.setDrinks(currentPlayer.getDrinks() + this.currentDiceIndex + 1);
        }
        this.gameState = AugensaufenState.RESULT;
    }

    private void restart() {
        this.bottomText.setText("Tippen zum Würfeln");
        if (fromMenue) {
            this.playerText.setText("Nächster Spieler");
        } else {
            Player currentPlayer = Player.getPlayerById(this.playerList, this.currentPlayerId);
            this.currentPlayerId = currentPlayer.getNextPlayerId();
            currentPlayer = Player.getPlayerById(this.playerList, this.currentPlayerId);
            this.playerText.setText(currentPlayer.getName());
        }
        this.gameState = AugensaufenState.START;
    }

    private void startRolling() {
        this.gameState = AugensaufenState.ROLLING;
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gameState == AugensaufenState.ROLLING) {
                    currentDiceIndex = random.nextInt(6);
                    switch (currentDiceIndex) {
                        case 0:
                            diceImage.setImageResource(R.drawable.dice1);
                            break;
                        case 1:
                            diceImage.setImageResource(R.drawable.dice2);
                            break;
                        case 2:
                            diceImage.setImageResource(R.drawable.dice3);
                            break;
                        case 3:
                            diceImage.setImageResource(R.drawable.dice4);
                            break;
                        case 4:
                            diceImage.setImageResource(R.drawable.dice5);
                            break;
                        case 5:
                            diceImage.setImageResource(R.drawable.dice6);
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
            intent.putExtra("lastGame", MiniGame.AUGENSAUFEN);
            this.startActivity(intent);
        }
    }
}
