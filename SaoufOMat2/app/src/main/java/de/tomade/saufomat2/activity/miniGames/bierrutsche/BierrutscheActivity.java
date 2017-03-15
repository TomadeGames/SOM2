package de.tomade.saufomat2.activity.miniGames.bierrutsche;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.miniGames.BaseMiniGame;
import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.model.drawable.DynamicImageView;

//TODO: Getränke zähler der trinkenden Spieler erhöhen
//TODO: Spiel endet nicht, wenn es aus dem Hauptspiel gestartet wurde
public class BierrutscheActivity extends BaseMiniGame implements View.OnClickListener {
    private static final String TAG = BierrutscheActivity.class.getSimpleName();
    private static final int TARGET_ACCURACY = 5000;
    private static final int ANIMATION_DURATION = 1500;
    private static final int FALLING_DELAY = 1500;
    private static final int SINGLE_TURN_LIMIT = 3;

    private float beerStartPositionX;
    private float beerStartPositionY;


    private DynamicImageView backgroundImage;
    private ImageView startField;
    private ImageView targetImage;
    private ImageView beerImage;

    private RelativeLayout tutorialPanel;

    private TextView nameText;
    private TextView tutorialText;
    private TextView statisticText;
    private TextView scoreText;

    private int turnCount = 0;
    private int maxTurnCount;

    private Map<Integer, Integer> distances = new HashMap<>();
    private int lastDistance = -1;
    private int[] currentDistances = new int[3];
    private int currentDistance;

    //ScreenSize
    private int screenWidth;

    private BierrutscheState gameState = BierrutscheState.START;
    private float downPositionX;

    private ObjectAnimator fallingGlassX;
    private ObjectAnimator fallingGlassY;
    private ObjectAnimator turningGlass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_bierrutsche);

        if (this.playerList == null) {
            Player player1 = new Player();
            player1.setName(this.getString(R.string.default_player_player1));

            Player player2 = new Player();
            player2.setName(this.getString(R.string.default_player_player2));

            player1.setNextPlayer(player2);
            player1.setLastPlayer(player2);

            player2.setNextPlayer(player1);
            player2.setLastPlayer(player1);

            this.playerList = new ArrayList<>();
            this.playerList.add(player1);
            this.playerList.add(player2);

            this.currentPlayer = player1;
        }

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        this.screenWidth = size.x;

        this.backgroundImage = (DynamicImageView) this.findViewById(R.id.backgroundImage);
        this.backgroundImage.setFullX(true);
        this.startField = (ImageView) this.findViewById(R.id.startImage);
        this.targetImage = (ImageView) this.findViewById(R.id.targetImage);
        this.beerImage = (ImageView) this.findViewById(R.id.beerImage);
        this.tutorialPanel = (RelativeLayout) this.findViewById(R.id.tutorialPanel);
        this.tutorialText = (TextView) this.findViewById(R.id.tutorialText);
        this.statisticText = (TextView) this.findViewById(R.id.statisticText);
        this.scoreText = (TextView) this.findViewById(R.id.accuracyText);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.backgroundImage.getLayoutParams();
        this.backgroundImage.setLayoutParams(params);

        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);
        ImageButton tutorialButton = (ImageButton) this.findViewById(R.id.tutorialButton);

        backButton.setOnClickListener(this);
        tutorialButton.setOnClickListener(this);

        if (this.fromMainGame) {
            TextView backText = (TextView) this.findViewById(R.id.backText);
            backButton.setVisibility(View.GONE);
            backText.setVisibility(View.GONE);
            this.maxTurnCount = SINGLE_TURN_LIMIT * this.playerList.size();
            this.nameText = (TextView) this.findViewById(R.id.nameText);
            this.nameText.setText(this.currentPlayer.getName());
        } else {
            this.findViewById(R.id.namePanel).setVisibility(View.GONE);
        }

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
        int tableScrollDelay = (int) ((1 / (float) accuracy) * ANIMATION_DURATION * 50);
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

        //TODO: Linear Interpolieren
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
                    endRound();
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
                    endRound();
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

    private void endRound() {
        if (this.currentDistance > 100) {
            this.currentDistance = 0;
        }
        this.currentDistances[this.turnCount % SINGLE_TURN_LIMIT] = this.currentDistance;

        this.turnCount++;
        if (this.turnCount % SINGLE_TURN_LIMIT == 0) {
            if (this.turnCount >= this.maxTurnCount) {
                this.endGame();
            }
            int max = this.getMaximum(this.currentDistances);
            this.distances.put(this.currentPlayer.getId(), max);
            this.nextPlayer(max);
        } else {
            this.gameState = BierrutscheState.END_SINGEL_TURN;
        }
        this.scoreText.setText(this.scoreText.getText() + "\n" + this.turnCount % SINGLE_TURN_LIMIT + "/" +
                SINGLE_TURN_LIMIT);
    }

    private int getMaximum(int[] values) {
        int max = 0;
        for (int i : values) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    private void endGame() {
        this.tutorialPanel.setVisibility(View.VISIBLE);
        this.tutorialText.setText(R.string.minigame_bierrutsche_game_over);
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
    }

    private void nextPlayer(int score) {
        this.tutorialPanel.setVisibility(View.VISIBLE);
        this.scoreText.setVisibility(View.INVISIBLE);
        if (this.fromMainGame) {
            int max = 0;
            int id = -1;
            for (Map.Entry<Integer, Integer> entry : this.distances.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    id = entry.getKey();
                }
            }
            this.statisticText.setText(Player.getPlayerById(this.playerList, id).getName() + ": " + max);
            Player lastPlayer = Player.getPlayerById(this.playerList, this.currentPlayer.getId());
            this.nextPlayer();
            this.nameText.setText(this.currentPlayer.getName());
            this.tutorialText.setText(lastPlayer.getName() + ": " + score + "\n\n" + this.currentPlayer.getName() + "" +
                    " ist dran");
        } else {
            if (this.lastDistance > 0) {
                this.tutorialText.setText("Deine Punkte: " + score + "\nLezter Spieler: " + this.lastDistance +
                        "\n\nNächster Spieler ist dran");
            } else {
                this.tutorialText.setText("Deine Punkte: " + score + "\n\nNächster Spieler ist dran");
            }
            this.lastDistance = score;
        }
        this.gameState = BierrutscheState.NEXT_PLAYER;
    }

    private void atStartOnTouch(MotionEvent event) {
        if (this.downPositionX != -1) {
            float x = event.getX();
            if (x > this.startField.getWidth()) {
                x = this.startField.getWidth();
            }
            float deltaX = x - this.downPositionX;
            if (deltaX > 0) {
                this.gameState = BierrutscheState.ANIMATION;
                long eventDuration = event.getEventTime() - event.getDownTime();

                long accuracy = (long) (deltaX / eventDuration * 1000);
                float percent = 100 / (float) TARGET_ACCURACY;
                this.currentDistance = (int) (percent * accuracy);
                Log.d(TAG, "Up: " + x + " deltaX: " + deltaX + " duration: " + eventDuration + " accuracy: " +
                        accuracy + " percent: " + percent);
                this.startAnimation(this.currentDistance);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.downPositionX = event.getX();
                if (this.downPositionX > this.startField.getWidth()) {
                    this.downPositionX = -1;
                }
                Log.d(TAG, "Down: " + this.downPositionX);
                break;
            case MotionEvent.ACTION_UP:
                if (this.tutorialPanel.getVisibility() == View.VISIBLE) {
                    this.tutorialPanel.setVisibility(View.GONE);
                    if (this.gameState != BierrutscheState.END_GAME) {
                        if (this.gameState == BierrutscheState.NEXT_PLAYER) {
                            this.startNextTurn();
                        }
                    }
                } else {
                    switch (this.gameState) {
                        case START:
                            this.atStartOnTouch(event);
                            break;
                        case END_SINGEL_TURN:
                            this.startNextTurn();
                            break;
                        case END_GAME:
                            this.leaveGame();
                            break;
                        default:
                            break;
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (this.tutorialPanel.getVisibility() == View.VISIBLE) {
            this.tutorialPanel.setVisibility(View.GONE);
        } else {
            switch (v.getId()) {
                case R.id.backButton:
                    this.leaveGame();
                    break;
                case R.id.tutorialButton:
                    if (this.tutorialPanel.getVisibility() == View.GONE && this.gameState == BierrutscheState.START) {
                        this.tutorialPanel.setVisibility(View.VISIBLE);
                        this.tutorialText.setText(R.string.minigame_bierrutsche_tutorial);
                    }
                    break;
            }
        }
    }
}
