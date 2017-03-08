package de.tomade.saufomat2.activity.miniGames.biergeballer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.miniGames.BaseMiniGame;

//TODO: am start werden direkt beim rechten spieler 2 leben abgezogen
//TODO: getränkezähler setzten
//TODO: Spielernamen beim Start einblenden
public class BiergeballerActivity extends BaseMiniGame implements View.OnTouchListener, View.OnClickListener {
    private static final int BEER_WIDTH = 40;
    private static final int BEER_HEIGHT = 80;

    private static Random random = new Random();
    private boolean tutorialShown = false;
    private boolean gameActive = true;

    private int screenWidth;
    private int screenHeight;

    private int beerSpeed = 1000;
    private int beerSpawnTimeLeft = random.nextInt(1000) + 1000;
    private int beerSpawnTimeRight = random.nextInt(1000) + 1000;

    private View cratePlayer0;
    private View cratePlayer1;
    private View[][] hearts = new View[2][3];
    private List<View> beersFromLeft = new ArrayList<>();
    private List<View> beersFromRight = new ArrayList<>();

    private View tutorialPanel;
    private RelativeLayout allLayout;
    private TextView tutorialText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_biergeballer);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;

        this.cratePlayer0 = this.findViewById(R.id.player0Crate);
        this.cratePlayer1 = this.findViewById(R.id.player1Crate);
        this.hearts[0][0] = this.findViewById(R.id.heart0Left);
        this.hearts[0][1] = this.findViewById(R.id.heart1Left);
        this.hearts[0][2] = this.findViewById(R.id.heart2Left);

        this.hearts[1][0] = this.findViewById(R.id.heart0Right);
        this.hearts[1][1] = this.findViewById(R.id.heart1Right);
        this.hearts[1][2] = this.findViewById(R.id.heart2Right);

        this.tutorialPanel = this.findViewById(R.id.tutorialPanel);
        this.tutorialText = (TextView) this.findViewById(R.id.tutorialText);
        this.allLayout = (RelativeLayout) this.findViewById(R.id.allLayout);

        this.findViewById(R.id.backButton).setOnClickListener(this);
        this.findViewById(R.id.tutorialButton).setOnClickListener(this);

        this.tutorialPanel.setOnClickListener(this);
        this.cratePlayer0.setOnTouchListener(this);
        this.cratePlayer1.setOnTouchListener(this);

        final int spawnTimeScale = 50;
        final int speedScale = 100;

        final Handler leftHandler = new Handler();
        leftHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BiergeballerActivity.this.gameActive) {
                    shootBeer(true);
                    BiergeballerActivity.this.beerSpawnTimeLeft -= random.nextInt(BiergeballerActivity.this
                            .beerSpawnTimeLeft / spawnTimeScale);
                    BiergeballerActivity.this.beerSpeed -= random.nextInt(BiergeballerActivity.this.beerSpeed /
                            speedScale);
                }
                leftHandler.postDelayed(this, BiergeballerActivity.this.beerSpawnTimeLeft);
            }
        }, this.beerSpawnTimeLeft);

        final Handler rightHandler = new Handler();
        rightHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BiergeballerActivity.this.gameActive) {
                    shootBeer(false);
                    BiergeballerActivity.this.beerSpawnTimeRight -= random.nextInt(BiergeballerActivity.this
                            .beerSpawnTimeRight / spawnTimeScale);
                    BiergeballerActivity.this.beerSpeed -= random.nextInt(BiergeballerActivity.this.beerSpeed /
                            speedScale);
                }
                rightHandler.postDelayed(this, BiergeballerActivity.this.beerSpawnTimeRight);
            }
        }, this.beerSpawnTimeRight);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        RelativeLayout rlLeft = (RelativeLayout) this.findViewById(R.id.leftScreen);
        rlLeft.setLayoutParams(new RelativeLayout.LayoutParams(this.screenWidth / 2, this.screenHeight));
    }

    private void checkCollision(boolean left) {
        List<View> removeing = new ArrayList<>();
        if (left) {
            for (View v : this.beersFromRight) {
                if (this.cratePlayer0.getX() < v.getX() + v.getWidth() &&
                        this.cratePlayer0.getX() + this.cratePlayer0.getWidth() > v.getX() &&
                        this.cratePlayer0.getY() < v.getY() + v.getHeight() &&
                        this.cratePlayer0.getHeight() + this.cratePlayer0.getY() > v.getY()) {
                    removeing.add(v);
                }
            }
            for (View v : removeing) {
                this.beersFromRight.remove(v);
            }
        } else {
            for (View v : this.beersFromLeft) {
                if (this.cratePlayer0.getX() < v.getX() + v.getWidth() &&
                        this.cratePlayer0.getX() + this.cratePlayer0.getWidth() > v.getX() &&
                        this.cratePlayer0.getY() < v.getY() + v.getHeight() &&
                        this.cratePlayer0.getHeight() + this.cratePlayer0.getY() > v.getY()) {
                    removeing.add(v);
                }
            }
            for (View v : removeing) {
                this.beersFromLeft.remove(v);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (this.tutorialShown) {
            this.tutorialPanel.setVisibility(View.GONE);
            this.tutorialShown = false;
        } else {
            switch (v.getId()) {
                case R.id.player0Crate:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            this.cratePlayer0.setY(event.getRawY());
                            this.checkCollision(true);
                            break;
                    }
                    break;
                case R.id.player1Crate:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            this.cratePlayer1.setY(event.getRawY());
                            this.checkCollision(false);
                            break;
                    }
                    break;
            }
        }
        return true;
    }

    private void shootBeer(boolean fromLeft) {
        final ImageView beer = new ImageView(this);
        beer.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(BEER_WIDTH, BEER_HEIGHT);
        beer.setImageResource(R.drawable.beer_icon);
        this.allLayout.addView(beer, lp);

        if (fromLeft) {
            beer.setRotation(90);
            beer.setX(this.cratePlayer1.getWidth() / 2);
            beer.setY(this.cratePlayer0.getY());
            this.beersFromLeft.add(beer);
            ObjectAnimator leftAnim = ObjectAnimator.ofFloat(beer, "x", this.screenWidth + BEER_WIDTH);
            leftAnim.setInterpolator(new LinearInterpolator());
            leftAnim.setDuration(this.beerSpeed);
            leftAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (BiergeballerActivity.this.beersFromLeft.contains(beer)) {
                        hideHeart(false);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            leftAnim.start();
        } else {
            beer.setRotation(270);
            beer.setX(this.screenWidth - this.cratePlayer1.getWidth() / 2);
            beer.setY(this.cratePlayer1.getY());
            this.beersFromRight.add(beer);
            ObjectAnimator rightAnim = ObjectAnimator.ofFloat(beer, "x", 0 - BEER_WIDTH * 2);
            rightAnim.setInterpolator(new LinearInterpolator());
            rightAnim.setDuration(this.beerSpeed);
            rightAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (BiergeballerActivity.this.beersFromRight.contains(beer)) {
                        hideHeart(false);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            rightAnim.start();
        }
    }

    private void hideHeart(boolean left) {
        for (int i = 2; i > 0; i--) {
            if (left) {
                if (this.hearts[0][i].getVisibility() == View.VISIBLE) {
                    this.hearts[0][i].setVisibility(View.GONE);
                    return;
                }
            } else {
                if (this.hearts[1][i].getVisibility() == View.VISIBLE) {
                    this.hearts[1][i].setVisibility(View.GONE);
                    return;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (this.tutorialShown) {
            this.tutorialPanel.setVisibility(View.GONE);
            this.tutorialShown = false;
        } else {
            switch (v.getId()) {
                case R.id.backButton:
                    this.leaveGame();
                    break;
                case R.id.tutorialButton:
                    if (!this.tutorialShown) {
                        this.tutorialShown = true;
                        this.tutorialText.setText(R.string.minigame_biergeballer_tutorial);
                        this.tutorialPanel.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }
}
