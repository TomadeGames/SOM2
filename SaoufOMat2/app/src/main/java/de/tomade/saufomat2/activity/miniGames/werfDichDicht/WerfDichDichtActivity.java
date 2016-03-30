package de.tomade.saufomat2.activity.miniGames.werfDichDicht;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.ChooseMiniGameActivity;
import de.tomade.saufomat2.activity.miniGames.augensaufen.AugensaufenState;
import de.tomade.saufomat2.model.MiniGame;
import de.tomade.saufomat2.model.Player;

public class WerfDichDichtActivity extends Activity implements View.OnClickListener{
    public static final String TAG = WerfDichDichtActivity.class.getSimpleName();
    private static Random random;
    private static final int DICE_ROLL_DELAY = 100;
    private static final int ANIMATION_DELAY = 100;

    private TextView playerText;
    private TextView popupText;
    private ImageView popupImage;
    private ImageView diceImage;
    private ImageView[] glasses = new ImageView[6];
    private View tutorial;

    private List<Player> playerList;
    private int currentPlayerId;
    private boolean fromMenue = false;

    private int animationCounter = 0;

    private boolean[] isFull = new boolean[6];
    private WerfDichDichtState gameState = WerfDichDichtState.START;
    private int currentDiceIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_werf_dich_dicht);

        random = new Random();

        this.playerText = (TextView) this.findViewById(R.id.nameText);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.fromMenue = extras.getBoolean("fromMenue");
            if (!fromMenue) {
                playerList = extras.getParcelableArrayList("player");
                currentPlayerId = extras.getInt("currentPlayerId");
                this.playerText.setText(Player.getPlayerById(playerList, currentPlayerId).getName());
            } else {
                this.playerText.setVisibility(View.GONE);
                ImageView playerPopup = (ImageView) this.findViewById(R.id.nameBackground);
                playerPopup.setVisibility(View.GONE);
            }
        }

        this.diceImage = (ImageView) this.findViewById(R.id.diceImage);
        this.popupImage = (ImageView) this.findViewById(R.id.popupImage);
        this.popupText = (TextView) this.findViewById(R.id.popupText);
        this.glasses[0] = (ImageView) this.findViewById(R.id.glas0Image);
        this.glasses[1] = (ImageView) this.findViewById(R.id.glas1Image);
        this.glasses[2] = (ImageView) this.findViewById(R.id.glas2Image);
        this.glasses[3] = (ImageView) this.findViewById(R.id.glas3Image);
        this.glasses[4] = (ImageView) this.findViewById(R.id.glas4Image);
        this.glasses[5] = (ImageView) this.findViewById(R.id.glas5Image);

        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);
        ImageButton tutorialButton = (ImageButton) this.findViewById(R.id.tutorialButton);
        backButton.setOnClickListener(this);
        tutorialButton.setOnClickListener(this);

        this.tutorial = this.findViewById(R.id.tutorialPanel);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(this.tutorial.getVisibility() == View.GONE) {
            if (event.getAction() == 0) {
                switch (this.gameState) {
                    case START:
                        startRolling();
                        break;
                    case ROLLING:
                        stopRolling();
                        break;
                }
                return true;
            }
        }
        else{
            this.tutorial.setVisibility(View.GONE);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backButton:
                Intent intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
                intent.putExtra("lastGame", MiniGame.WERF_DICH_DICHT);
                this.startActivity(intent);
                break;
            case R.id.tutorialButton:
                this.tutorial.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void startRolling() {
        this.animationCounter = 0;
        this.popupText.setVisibility(View.GONE);
        this.popupImage.setVisibility(View.GONE);
        this.gameState = WerfDichDichtState.ROLLING;
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gameState == WerfDichDichtState.ROLLING) {
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

    private void stopAnimation() {
        this.gameState = WerfDichDichtState.STOP;
        this.popupImage.setVisibility(View.VISIBLE);
        this.popupText.setVisibility(View.VISIBLE);

        if (isFull[currentDiceIndex]) {
            this.popupText.setText("Trink einen");
            if(!fromMenue){
                Player currentPlayer = Player.getPlayerById(playerList, currentPlayerId);
                currentPlayer.setDrinks(currentPlayer.getDrinks() + 1);
            }
        } else {
            if (fromMenue) {
                this.popupText.setText("Nächster Spieler");
            } else {
                Player currentPlayer = Player.getPlayerById(playerList, currentPlayerId);
                this.currentPlayerId = currentPlayer.getNextPlayerId();
                currentPlayer = Player.getPlayerById(playerList, currentPlayer.getNextPlayerId());
                this.popupText.setText(currentPlayer.getName() + " ist dran");
            }
        }
        isFull[currentDiceIndex] = !isFull[currentDiceIndex];

        this.gameState = WerfDichDichtState.START;
    }

    private void stopRolling() {
        this.gameState = WerfDichDichtState.GLASS_ANIMATION;
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gameState == WerfDichDichtState.GLASS_ANIMATION) {
                    switch (animationCounter) {
                        case 0:
                            if (!isFull[currentDiceIndex]) {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_2);
                            } else {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_7);
                            }
                            break;
                        case 1:
                            if (!isFull[currentDiceIndex]) {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_3);
                            } else {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_6);
                            }
                            break;
                        case 2:
                            if (!isFull[currentDiceIndex]) {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_4);
                            } else {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_5);
                            }
                            break;
                        case 3:
                            if (!isFull[currentDiceIndex]) {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_5);
                            } else {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_4);
                            }
                            break;
                        case 4:
                            if (!isFull[currentDiceIndex]) {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_6);
                            } else {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_3);
                            }
                            break;
                        case 5:
                            if (!isFull[currentDiceIndex]) {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_7);
                            } else {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_2);
                            }
                            break;
                        case 6:
                            if (!isFull[currentDiceIndex]) {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_8);
                            } else {
                                glasses[currentDiceIndex].setImageResource(R.drawable.schnapsglas_1);
                            }
                            break;
                        case 7:
                            stopAnimation();
                            break;
                    }
                    animationCounter++;
                    if(animationCounter <= 7) {
                        mHandler.postDelayed(this, WerfDichDichtActivity.ANIMATION_DELAY);
                    }
                }

            }
        }, WerfDichDichtActivity.ANIMATION_DELAY);
    }
}
