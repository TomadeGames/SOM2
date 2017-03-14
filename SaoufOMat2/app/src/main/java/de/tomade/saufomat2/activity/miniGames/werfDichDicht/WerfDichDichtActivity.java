package de.tomade.saufomat2.activity.miniGames.werfDichDicht;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.miniGames.BaseMiniGame;

//TODO: ZurückButton darf nicht sichtbar sein, wenn man vom Hauptspiel kommt
public class WerfDichDichtActivity extends BaseMiniGame implements View.OnClickListener {
    public static final String TAG = WerfDichDichtActivity.class.getSimpleName();
    private static Random random;
    private static final int DICE_ROLL_DELAY = 100;
    private static final int ANIMATION_DELAY = 100;

    private TextView popupText;
    private ImageView popupImage;
    private ImageView diceImage;
    private ImageView[] glasses = new ImageView[6];
    private View tutorial;

    private int animationCounter = 0;

    private boolean[] isFull = new boolean[6];
    private WerfDichDichtState gameState = WerfDichDichtState.START;
    private int currentDiceIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_werf_dich_dicht);

        random = new Random();

        TextView playerText = (TextView) this.findViewById(R.id.nameText);


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

        if (this.fromMainGame) {
            playerText.setText(this.currentPlayer.getName());
            TextView backText = (TextView) this.findViewById(R.id.backText);
            backButton.setVisibility(View.GONE);
            backText.setVisibility(View.GONE);

        } else {
            playerText.setVisibility(View.GONE);
            ImageView playerPopup = (ImageView) this.findViewById(R.id.nameBackground);
            playerPopup.setVisibility(View.GONE);
        }


        this.tutorial = this.findViewById(R.id.tutorialPanel);
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
            this.popupText.setText(R.string.minigame_werf_dich_dicht_drink);
            if (this.fromMainGame) {
                this.currentPlayer.increaseDrinks(1);
            }
        } else {
            if (!this.fromMainGame) {
                this.popupText.setText(R.string.minigame_werf_dich_dicht_next_player);
            } else {
                this.nextTurn();
                this.popupText.setText(this.getString(R.string.minigame_werf_dich_dicht_next_turn, this.currentPlayer
                        .getName()));
            }
        }
        this.isFull[this.currentDiceIndex] = !this.isFull[this.currentDiceIndex];

        this.gameState = WerfDichDichtState.START;
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
}
