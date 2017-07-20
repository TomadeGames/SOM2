package com.tomade.saufomat.activity.miniGame.werfDichDicht;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomade.saufomat.DrinkHelper;
import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.miniGame.BaseMiniGame;
import com.tomade.saufomat.persistance.GameValueHelper;

import java.util.Random;

public class WerfDichDichtActivity extends BaseMiniGame implements View.OnClickListener {
    public static final String TAG = WerfDichDichtActivity.class.getSimpleName();
    private static Random random;
    private static final int DICE_ROLL_DELAY = 100;
    private static final int ANIMATION_DELAY = 100;

    private TextView popupText;
    private TextView playerText;
    private TextView turnCounter;
    private ImageView popupImage;
    private ImageView diceImage;
    private ImageView[] glasses = new ImageView[6];
    private View tutorial;

    private int animationCounter = 0;
    private int turnCount = 0;
    private int maxTurns;

    private int shotsClearedInOneTurn = 0;

    private boolean[] isFull = new boolean[6];
    private WerfDichDichtState gameState = WerfDichDichtState.START;
    private int currentDiceIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_werf_dich_dicht);

        random = new Random(System.currentTimeMillis());

        this.playerText = (TextView) this.findViewById(R.id.nameText);
        this.turnCounter = (TextView) this.findViewById(R.id.turnCounter);

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

        this.loadLastGame();

        if (this.fromMainGame) {
            this.playerText.setText(this.currentPlayer.getName());
            TextView backText = (TextView) this.findViewById(R.id.backText);
            backButton.setVisibility(View.GONE);
            backText.setVisibility(View.GONE);
            this.maxTurns = this.playerList.size() * 3;
            if (this.maxTurns > 30) {
                this.maxTurns = this.playerList.size() * 2;
                if (this.maxTurns > 30) {
                    this.maxTurns = this.playerList.size();
                }
            }
            this.turnCounter.setText((this.turnCount + 1) + "/" + this.maxTurns);

        } else {
            this.playerText.setVisibility(View.GONE);
            this.turnCounter.setVisibility(View.GONE);
            ImageView playerPopup = (ImageView) this.findViewById(R.id.nameBackground);
            playerPopup.setVisibility(View.GONE);
        }

        this.tutorial = this.findViewById(R.id.tutorialPanel);
    }

    private void loadLastGame() {
        GameValueHelper gameValueHelper = new GameValueHelper(this);
        this.isFull = gameValueHelper.getSavedWerfDichDichtState();

        for (int i = 0; i < this.isFull.length; i++) {
            if (this.isFull[i]) {
                this.glasses[i].setImageResource(R.drawable.schnapsglas_8);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.tutorial.getVisibility() == View.GONE) {
            if (event.getAction() == 0) {
                switch (this.gameState) {
                    case START:
                        this.startRolling();
                        break;
                    case ROLLING:
                        this.stopRolling();
                        break;
                    case END:
                        this.leaveGame();
                        break;
                    default:
                        break;
                }
                return true;
            }
        } else {
            this.tutorial.setVisibility(View.GONE);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                this.leaveGame();
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
                if (WerfDichDichtActivity.this.gameState == WerfDichDichtState.ROLLING) {
                    WerfDichDichtActivity.this.currentDiceIndex = random.nextInt(6);
                    switch (WerfDichDichtActivity.this.currentDiceIndex) {
                        case 0:
                            WerfDichDichtActivity.this.diceImage.setImageResource(R.drawable.dice1);
                            break;
                        case 1:
                            WerfDichDichtActivity.this.diceImage.setImageResource(R.drawable.dice2);
                            break;
                        case 2:
                            WerfDichDichtActivity.this.diceImage.setImageResource(R.drawable.dice3);
                            break;
                        case 3:
                            WerfDichDichtActivity.this.diceImage.setImageResource(R.drawable.dice4);
                            break;
                        case 4:
                            WerfDichDichtActivity.this.diceImage.setImageResource(R.drawable.dice5);
                            break;
                        case 5:
                            WerfDichDichtActivity.this.diceImage.setImageResource(R.drawable.dice6);
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

        if (this.isFull[this.currentDiceIndex]) {
            this.clearShot();
        } else {
            this.fillShot();
        }
        this.isFull[this.currentDiceIndex] = !this.isFull[this.currentDiceIndex];

        if (this.gameState != WerfDichDichtState.END) {
            this.gameState = WerfDichDichtState.START;
        }
    }

    private void clearShot() {
        this.shotsClearedInOneTurn++;
        if (this.shotsClearedInOneTurn >= 6) {
            if (this.fromMainGame) {
                this.currentPlayer.increaseDrinks(1);
            }
            if (this.turnCount >= this.maxTurns - 1) {
                this.popupText.setText(R.string.minigame_werf_dich_dicht_drink_six_in_last_turn);
                DrinkHelper.increaseAllButOnePlayer(2, this.currentPlayer, this);
                this.gameState = WerfDichDichtState.END;
            } else {
                this.popupText.setText(R.string.minigame_werf_dich_dicht_drink_six);
            }
        } else {
            this.popupText.setText(R.string.minigame_werf_dich_dicht_drink);
            if (this.fromMainGame) {
                this.currentPlayer.increaseDrinks(1);
            }
        }
    }

    private void fillShot() {
        if (!this.fromMainGame) {
            this.popupText.setText(R.string.minigame_werf_dich_dicht_next_player);
        } else {
            this.turnCount++;
            if (this.turnCount >= this.maxTurns) {
                this.gameState = WerfDichDichtState.END;
                this.popupText.setText(R.string.minigame_werf_dich_dicht_game_over);
            } else {
                this.nextPlayer();
                this.shotsClearedInOneTurn = 0;
                this.popupText.setText(this.getString(R.string.minigame_werf_dich_dicht_next_turn, this
                        .currentPlayer
                        .getName()));
                this.turnCounter.setText((this.turnCount + 1) + "/" + this.maxTurns);
                this.playerText.setText(this.currentPlayer.getName());
            }
        }
    }

    private void stopRolling() {
        this.gameState = WerfDichDichtState.GLASS_ANIMATION;
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (WerfDichDichtActivity.this.gameState == WerfDichDichtState.GLASS_ANIMATION) {
                    switch (WerfDichDichtActivity.this.animationCounter) {
                        case 0:
                            if (!WerfDichDichtActivity.this.isFull[WerfDichDichtActivity.this.currentDiceIndex]) {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_2);
                            } else {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_7);
                            }
                            break;
                        case 1:
                            if (!WerfDichDichtActivity.this.isFull[WerfDichDichtActivity.this.currentDiceIndex]) {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_3);
                            } else {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_6);
                            }
                            break;
                        case 2:
                            if (!WerfDichDichtActivity.this.isFull[WerfDichDichtActivity.this.currentDiceIndex]) {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_4);
                            } else {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_5);
                            }
                            break;
                        case 3:
                            if (!WerfDichDichtActivity.this.isFull[WerfDichDichtActivity.this.currentDiceIndex]) {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_5);
                            } else {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_4);
                            }
                            break;
                        case 4:
                            if (!WerfDichDichtActivity.this.isFull[WerfDichDichtActivity.this.currentDiceIndex]) {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_6);
                            } else {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_3);
                            }
                            break;
                        case 5:
                            if (!WerfDichDichtActivity.this.isFull[WerfDichDichtActivity.this.currentDiceIndex]) {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_7);
                            } else {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_2);
                            }
                            break;
                        case 6:
                            if (!WerfDichDichtActivity.this.isFull[WerfDichDichtActivity.this.currentDiceIndex]) {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_8);
                            } else {
                                WerfDichDichtActivity.this.glasses[WerfDichDichtActivity.this.currentDiceIndex]
                                        .setImageResource(R.drawable.schnapsglas_1);
                            }
                            break;
                        case 7:
                            stopAnimation();
                            break;
                    }
                    WerfDichDichtActivity.this.animationCounter++;
                    if (WerfDichDichtActivity.this.animationCounter <= 7) {
                        mHandler.postDelayed(this, WerfDichDichtActivity.ANIMATION_DELAY);
                    }
                }

            }
        }, WerfDichDichtActivity.ANIMATION_DELAY);
    }

    @Override
    protected void leaveGame() {
        if (this.fromMainGame) {
            GameValueHelper gameValueHelper = new GameValueHelper(this);
            gameValueHelper.saveWerfDichDicht(this.isFull);
        }
        super.leaveGame();
    }
}
