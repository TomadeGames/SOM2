package de.tomade.saufomat2.activity.miniGames.bierrutsche;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import de.tomade.saufomat2.activity.ChooseMiniGameActivity;
import de.tomade.saufomat2.activity.mainGame.MainGameActivity;
import de.tomade.saufomat2.activity.miniGames.MiniGame;
import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.model.drawable.DynamicImageView;

public class BierrutscheActivity extends Activity implements View.OnClickListener {
    private static final String TAG = BierrutscheActivity.class.getSimpleName();
    private final int TARGET_ACCURACY = 5000;
    private final int ANIMATION_DURATION = 1500;
    private final int FALLING_DELAY = 1500;
    private final int SINGLE_TURN_LIMIT = 3;

    private float beerStartPositionX;
    private float beerStartPositionY;


    private DynamicImageView backgroundImage;
    private ImageView startField;
    private ImageView targetImage;
    private ImageView beerImage;

    private RelativeLayout tutorialPanel;

    private ImageButton backButton;
    private ImageButton tutorialButton;

    private TextView nameText;
    private TextView tutorialText;
    private TextView statisticText;
    private TextView scoreText;

    private int turnCount = 0;
    private int maxTurnCount;

    private boolean fromMenue = false;
    private ArrayList<Player> playerList;
    private int currentPlayerId;
    private Map<Integer, Integer> distances = new HashMap<>();
    private int lastDistance = -1;
    private int[] currentDistances = new int[3];
    private int currentDistance;

    //ScreenSize
    private int screenWidth;
    private int screenHeight;

    private BierrutscheState gameState = BierrutscheState.START;
    private float downPositionX;

    private ObjectAnimator fallingGlassX;
    private ObjectAnimator fallingGlassY;
    private ObjectAnimator turningGlass;
    private ObjectAnimator targetX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bierrutsche);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.fromMenue = extras.getBoolean("fromMenue");
        }

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        backgroundImage = (DynamicImageView) this.findViewById(R.id.backgroundImage);
        backgroundImage.setFullX(true);
        startField = (ImageView) this.findViewById(R.id.startImage);
        targetImage = (ImageView) this.findViewById(R.id.targetImage);
        beerImage = (ImageView) this.findViewById(R.id.beerImage);
        tutorialPanel = (RelativeLayout) this.findViewById(R.id.tutorialPanel);
        tutorialText = (TextView) this.findViewById(R.id.tutorialText);
        statisticText = (TextView) this.findViewById(R.id.statisticText);
        scoreText = (TextView) this.findViewById(R.id.accuracyText);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) backgroundImage.getLayoutParams();
        backgroundImage.setLayoutParams(params);

        this.backButton = (ImageButton) this.findViewById(R.id.backButton);
        this.tutorialButton = (ImageButton) this.findViewById(R.id.tutorialButton);

        this.backButton.setOnClickListener(this);
        this.tutorialButton.setOnClickListener(this);

        if (!this.fromMenue) {
            this.backButton.setVisibility(View.GONE);
            this.playerList = extras.getParcelableArrayList("player");
            this.currentPlayerId = extras.getInt("currentPlayerId");
            this.maxTurnCount = this.SINGLE_TURN_LIMIT * playerList.size();
            nameText = (TextView) this.findViewById(R.id.nameText);
            nameText.setText(Player.getPlayerById(playerList, currentPlayerId).getName());
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
        scoreText.setVisibility(View.VISIBLE);
        int maximumLength = backgroundImage.getWidth() - screenWidth;
        int scrollWidth;
        boolean toFar = false;
        if (accuracy > 100) {
            scrollWidth = maximumLength;
            toFar = true;
        } else {
            scrollWidth = maximumLength * accuracy / 100;
        }


        int tableScrollDistance = screenWidth*accuracy/100 - screenWidth/2;
        int tableScrollDelay = (int)((1/(float)accuracy) * ANIMATION_DURATION * 50);
        if(tableScrollDistance < 0){
            tableScrollDistance = 0;
        }
        if(accuracy <= 50){
            tableScrollDelay = 0;
        }
        Log.d(TAG, "tableScrollDistance: " + tableScrollDistance + " tableScrollDelay: " + tableScrollDelay);

        ObjectAnimator backgroundX = ObjectAnimator.ofFloat(backgroundImage, View.TRANSLATION_X, 0, -scrollWidth);
        backgroundX.setDuration(ANIMATION_DURATION);

        ObjectAnimator startFieldX = ObjectAnimator.ofFloat(startField, View.TRANSLATION_X, 0, -scrollWidth);
        startFieldX.setDuration(ANIMATION_DURATION);

        //TODO: Linear Interpolieren
        targetX = ObjectAnimator.ofFloat(targetImage, View.TRANSLATION_X, 0, -tableScrollDistance);
        targetX.setDuration(ANIMATION_DURATION - tableScrollDelay);
        targetX.setStartDelay(tableScrollDelay);

        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, accuracy);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                scoreText.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        animator.setEvaluator(new TypeEvaluator<Integer>() {
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return Math.round(startValue + (endValue - startValue) * fraction);
            }
        });
        animator.setDuration(ANIMATION_DURATION);

        if (toFar) {
            this.fallingGlassX = ObjectAnimator.ofFloat(beerImage, View.TRANSLATION_X, 0, screenWidth);
            this.fallingGlassX.setDuration((long) (ANIMATION_DURATION * 2.5f));

            fallingGlassY = ObjectAnimator.ofFloat(beerImage, View.TRANSLATION_Y, 0, 600);
            fallingGlassY.setDuration(ANIMATION_DURATION / 2);
            fallingGlassY.setStartDelay(FALLING_DELAY);

            turningGlass = ObjectAnimator.ofFloat(beerImage, View.ROTATION, 100);
            turningGlass.setDuration(ANIMATION_DURATION / 2);
            turningGlass.setStartDelay(FALLING_DELAY);

            backgroundX.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    turningGlass.start();
                    fallingGlassY.start();
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
            fallingGlassY.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    fallingGlassY.cancel();
                    fallingGlassX.cancel();
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
        this.currentDistances[turnCount % SINGLE_TURN_LIMIT] = this.currentDistance;

        turnCount++;
        if (turnCount % SINGLE_TURN_LIMIT == 0) {
            if (turnCount >= this.maxTurnCount) {
                endGame();
            }
            int max = getMaximum(this.currentDistances);
            this.distances.put(this.currentPlayerId, max);
            nextPlayer(max);
        } else {
            gameState = BierrutscheState.END_SINGEL_TURN;
        }
        this.scoreText.setText(this.scoreText.getText() + "\n" + turnCount % SINGLE_TURN_LIMIT + "/" + SINGLE_TURN_LIMIT);
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
        this.tutorialText.setText("Spiel vorbei");
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
        if (!fromMenue) {
            int max = 0;
            int id = -1;
            for (Map.Entry<Integer, Integer> entry : this.distances.entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    id = entry.getKey();
                }
            }
            this.statisticText.setText(Player.getPlayerById(playerList, id).getName() + ": " + max);
            Player currentPlayer = Player.getPlayerById(this.playerList, this.currentPlayerId);
            this.currentPlayerId = currentPlayer.getNextPlayerId();
            Player nextPlayer = Player.getPlayerById(this.playerList, this.currentPlayerId);
            this.nameText.setText(nextPlayer.getName());
            this.tutorialText.setText(currentPlayer.getName() + ": " + score + "\n\n" + nextPlayer.getName() + " ist dran");
        } else {
            if (lastDistance > 0) {
                this.tutorialText.setText("Deine Punkte: " + score + "\nLezter Spieler: " + lastDistance + "\n\nNächster Spieler ist dran");
            } else {
                this.tutorialText.setText("Deine Punkte: " + score + "\n\nNächster Spieler ist dran");
            }
            lastDistance = score;
        }
        this.gameState = BierrutscheState.NEXT_PLAYER;
    }

    private void atStartOnTouch(MotionEvent event) {
        if (this.downPositionX != -1) {
            float x = event.getX();
            if (x > startField.getWidth()) {
                x = startField.getWidth();
            }
            float deltaX = x - this.downPositionX;
            if (deltaX > 0) {
                this.gameState = BierrutscheState.ANIMATION;
                long eventDuration = event.getEventTime() - event.getDownTime();

                long accuracy = (long) (deltaX / eventDuration * 1000);
                float percent = 100 / (float) TARGET_ACCURACY;
                this.currentDistance = (int) (percent * accuracy);
                Log.d(TAG, "Up: " + x + " deltaX: " + deltaX + " duration: " + eventDuration + " accuracy: " + accuracy + " percent: " + percent);
                startAnimation(this.currentDistance);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.downPositionX = event.getX();
                if (this.downPositionX > startField.getWidth()) {
                    this.downPositionX = -1;
                }
                Log.d(TAG, "Down: " + this.downPositionX);
                break;
            case MotionEvent.ACTION_UP:
                if (tutorialPanel.getVisibility() == View.VISIBLE) {
                    tutorialPanel.setVisibility(View.GONE);
                    if (gameState == BierrutscheState.NEXT_PLAYER) {
                        startNextTurn();
                    }
                } else {
                    switch (this.gameState) {
                        case START:
                            atStartOnTouch(event);
                            break;
                        case END_SINGEL_TURN:
                            startNextTurn();
                            break;
                        case END_GAME:
                            leaveGame();
                            break;
                    }
                }
                break;
        }
        return true;
    }

    private void leaveGame() {
        Intent intent;
        if (fromMenue) {
            intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
            intent.putExtra("lastGame", MiniGame.BIERRUTSCHE);
        } else {
            intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
        }
        this.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (tutorialPanel.getVisibility() == View.VISIBLE) {
            tutorialPanel.setVisibility(View.GONE);
        } else {
            switch (v.getId()) {
                case R.id.backButton:
                    if (fromMenue) {
                        Intent intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
                        intent.putExtra("lastGame", MiniGame.BIERRUTSCHE);
                        this.startActivity(intent);
                    } else {
                        Intent intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
                        this.startActivity(intent);
                    }
                    break;
                case R.id.tutorialButton:

                    if (tutorialPanel.getVisibility() == View.GONE && this.gameState == BierrutscheState.START) {
                        tutorialPanel.setVisibility(View.VISIBLE);
                        this.tutorialText.setText(R.string.bierrutsche_tutorial);
                    }
                    break;
            }
        }
    }
}
