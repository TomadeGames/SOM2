package com.tomade.saufomat.activity.miniGame.bierrutsche;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.SwipeController;
import com.tomade.saufomat.activity.miniGame.BaseMiniGameActivity;
import com.tomade.saufomat.constant.Direction;
import com.tomade.saufomat.model.drawable.DynamicImageView;

import pl.droidsonroids.gif.GifTextView;

//TODO: aktuelle Punktzahl anzeigen
public class BierrutscheActivity extends BaseMiniGameActivity<BierrutschePresenter> {
    private static final String TAG = BierrutscheActivity.class.getSimpleName();
    private static final int TARGET_ACCURACY = 20000;
    private static final int ANIMATION_DURATION = 1500;
    private static final int FALLING_DELAY = 1500;

    private float beerStartPositionX;
    private float beerStartPositionY;

    private DynamicImageView backgroundImage;
    private GifTextView startField;
    private ImageView targetImage;
    private ImageView beerImage;
    private View tutorialPanel;
    private View tutorialButton;

    private TextView nameText;
    private TextView statisticText;
    private TextView scoreText;
    private TextView tutorialText;

    private BierrutscheState gameState = BierrutscheState.START;

    private ObjectAnimator fallingGlassX;
    private ObjectAnimator fallingGlassY;
    private ObjectAnimator turningGlass;
    private ImageButton backButton;
    private TextView backText;

    private boolean exitTouchDown = false;

    private SwipeController swipeController;

    @Override
    protected void initPresenter() {
        this.presenter = new BierrutschePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_bierrutsche);

        this.backgroundImage = this.findViewById(R.id.backgroundImage);
        this.backgroundImage.setFullX(true);
        this.startField = this.findViewById(R.id.startImage);
        this.targetImage = this.findViewById(R.id.targetImage);
        this.beerImage = this.findViewById(R.id.beerImage);
        this.statisticText = this.findViewById(R.id.statisticText);
        this.scoreText = this.findViewById(R.id.accuracyText);
        this.tutorialButton = this.findViewById(R.id.tutorialButton);
        this.tutorialPanel = this.findViewById(R.id.popupPanel);
        this.tutorialText = this.findViewById(R.id.popupText);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.backgroundImage.getLayoutParams();
        this.backgroundImage.setLayoutParams(params);

        this.backButton = this.findViewById(R.id.backButton);

        this.backButton.setOnClickListener(this);
        this.tutorialButton.setOnClickListener(this);
        this.backText = this.findViewById(R.id.backText);

        if (this.presenter.isFromMainGame()) {
            this.backButton.setVisibility(View.GONE);
            this.backText.setVisibility(View.GONE);
            this.nameText = this.findViewById(R.id.nameText);
            this.nameText.setText(this.presenter.getCurrentPlayerName());
        } else {
            this.findViewById(R.id.namePanel).setVisibility(View.GONE);
        }

        this.swipeController = new SwipeController();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        this.beerStartPositionX = this.beerImage.getX();
        this.beerStartPositionY = this.beerImage.getY();
        Log.d(TAG, "BeerPos Set, x: " + this.beerStartPositionX + " y: " + this.beerStartPositionY);
    }


    private void startAnimation(int accuracy) {
        this.scoreText.setVisibility(View.VISIBLE);
        int maximumLength = this.backgroundImage.getWidth() - this.screenWidth;
        int scrollWidth;
        boolean toFar = false;
        if (accuracy > 100) {
            scrollWidth = maximumLength;
            toFar = true;
        } else {
            scrollWidth = maximumLength * accuracy / 100;
        }


        int tableScrollDistance = this.screenWidth * accuracy / 100 - this.screenWidth / 2;
        int tableScrollDelay = (int) ((1 / (float) accuracy) * ANIMATION_DURATION * 40);
        if (tableScrollDistance < 0) {
            tableScrollDistance = 0;
        }
        if (accuracy <= 50) {
            tableScrollDelay = 0;
        }
        Log.d(TAG, "tableScrollDistance: " + tableScrollDistance + " tableScrollDelay: " + tableScrollDelay);

        ObjectAnimator backgroundX = ObjectAnimator.ofFloat(this.backgroundImage, View.TRANSLATION_X, 0, -scrollWidth);
        backgroundX.setDuration(ANIMATION_DURATION);

        ObjectAnimator startFieldX = ObjectAnimator.ofFloat(this.startField, View.TRANSLATION_X, 0, -scrollWidth);
        startFieldX.setDuration(ANIMATION_DURATION);

        ObjectAnimator targetX = ObjectAnimator.ofFloat(this.targetImage, View.TRANSLATION_X, 0, -tableScrollDistance);
        targetX.setDuration(ANIMATION_DURATION - tableScrollDelay);
        targetX.setStartDelay(tableScrollDelay);

        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, accuracy);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                BierrutscheActivity.this.scoreText.setText(String.valueOf(animation
                        .getAnimatedValue()));
            }
        });
        animator.setEvaluator(new TypeEvaluator<Integer>() {
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return Math.round(startValue + (endValue - startValue) * fraction);
            }
        });
        animator.setDuration(ANIMATION_DURATION);

        if (toFar) {
            this.fallingGlassX = ObjectAnimator.ofFloat(this.beerImage, View.TRANSLATION_X, 0, this.screenWidth);
            this.fallingGlassX.setDuration((long) (ANIMATION_DURATION * 2.5f));

            this.fallingGlassY = ObjectAnimator.ofFloat(this.beerImage, View.TRANSLATION_Y, 0, 600);
            this.fallingGlassY.setDuration(ANIMATION_DURATION / 2);
            this.fallingGlassY.setStartDelay(FALLING_DELAY);

            this.turningGlass = ObjectAnimator.ofFloat(this.beerImage, View.ROTATION, 100);
            this.turningGlass.setDuration(ANIMATION_DURATION / 2);
            this.turningGlass.setStartDelay(FALLING_DELAY);

            backgroundX.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    BierrutscheActivity.this.turningGlass.start();
                    BierrutscheActivity.this.fallingGlassY.start();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            this.fallingGlassY.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    BierrutscheActivity.this.fallingGlassY.cancel();
                    BierrutscheActivity.this.fallingGlassX.cancel();
                    BierrutscheActivity.this.endTurn();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        } else {
            targetX.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    BierrutscheActivity.this.endTurn();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });

        }
        backgroundX.start();
        startFieldX.start();
        targetX.start();
        animator.start();
    }

    private void endTurn() {
        if (this.presenter.endTurn()) {
            this.nextPlayersTurn();
        } else {
            this.gameState = BierrutscheState.END_SINGEL_TURN;
        }
        this.scoreText.setText(this.presenter.getTurnText());
    }

    public void endGame() {
        this.tutorialPanel.setVisibility(View.VISIBLE);
        this.tutorialText.setText(this.getString(R.string.minigame_bierrutsche_game_over) + "\n" + this.presenter
                .getFullScore() + "\n" + this.presenter.getBestPlayerText());
        this.gameState = BierrutscheState.END_GAME;
    }

    private void startNextTurn() {
        this.scoreText.setVisibility(View.INVISIBLE);
        this.beerImage.setX(this.beerStartPositionX);
        this.beerImage.setY(this.beerStartPositionY);
        Log.d(TAG, "BeerPos x: " + this.beerStartPositionX + " y: " + this.beerStartPositionY);
        this.beerImage.setRotation(0);
        this.targetImage.setX(0);
        this.backgroundImage.setX(0);
        this.startField.setX(0);
        this.gameState = BierrutscheState.START;
        this.tutorialButton.setVisibility(View.VISIBLE);
        if (!this.presenter.isFromMainGame()) {
            this.backButton.setVisibility(View.VISIBLE);
            this.backText.setVisibility(View.VISIBLE);
        }
    }

    private void nextPlayersTurn() {
        this.tutorialPanel.setVisibility(View.VISIBLE);
        this.scoreText.setVisibility(View.INVISIBLE);
        if (this.presenter.isFromMainGame()) {
            if (this.gameState != BierrutscheState.END_GAME) {
                this.statisticText.setText(this.presenter.getStartPlayerName() + ": " + this.presenter
                        .getStartDistance());
                this.presenter.nextPlayer();
                this.nameText.setText(this.presenter.getCurrentPlayer().getName());
                this.tutorialText.setText(this.presenter.getFullScore() + "\n" + this.presenter.getCurrentPlayer()
                        .getName() + " ist dran");
            }
        } else {
            if (this.presenter.getLastPlayerDistance() > 0) {
                this.tutorialText.setText("Deine Punkte: " + this.presenter.getCurrentPlayerScore() + "\nLezter " +
                        "Spieler: " + this.presenter
                        .getLastPlayerDistance() + "\n\nNächster Spieler ist dran");
            } else {
                this.tutorialText.setText("Deine Punkte: " + this.presenter.getCurrentPlayerScore() + "\n\nNächster " +
                        "Spieler ist dran");
            }
            this.presenter.nextPlayersTurn();
        }
        if (this.gameState != BierrutscheState.END_GAME) {
            this.gameState = BierrutscheState.NEXT_PLAYER;
        }
    }


    private void atStartOnTouch() {
        if (this.swipeController.getDirectionX() == Direction.RIGHT) {
            this.gameState = BierrutscheState.ANIMATION;
            this.tutorialButton.setVisibility(View.GONE);
            this.backButton.setVisibility(View.GONE);
            this.backText.setVisibility(View.GONE);

            long accuracy = (long) (this.swipeController.getDistanceX() / this.swipeController.getDuration() * 1000);
            float percent = 100 / (float) TARGET_ACCURACY;
            this.presenter.setCurrentDistances((int) (percent * accuracy));
            this.startAnimation(this.presenter.getCurrentDistance());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "touchEvent. GameState is: " + this.gameState);
        this.swipeController.handleSwipe(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (this.tutorialPanel.getVisibility() != View.VISIBLE) {
                switch (this.gameState) {
                    case START:
                        this.atStartOnTouch();
                        break;
                    case END_SINGEL_TURN:
                        this.startNextTurn();
                        break;
                    case NEXT_PLAYER:
                        this.startNextTurn();
                        break;
                    case END_GAME:
                        if (this.exitTouchDown) {
                            this.presenter.leaveGame();
                        }
                        break;
                    default:
                        break;
                }
            } else {
                this.tutorialPanel.setVisibility(View.GONE);
            }
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (this.gameState == BierrutscheState.END_GAME) {
                this.exitTouchDown = true;
            }
        }
        return true;
    }
}
