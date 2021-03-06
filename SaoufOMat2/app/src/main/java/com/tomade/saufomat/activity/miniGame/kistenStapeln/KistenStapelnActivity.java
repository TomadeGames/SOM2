package com.tomade.saufomat.activity.miniGame.kistenStapeln;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomade.saufomat.DrinkHelper;
import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.miniGame.BaseMiniGameActivity;
import com.tomade.saufomat.activity.miniGame.BaseMiniGamePresenter;

import java.util.ArrayList;
import java.util.List;

public class KistenStapelnActivity extends BaseMiniGameActivity<BaseMiniGamePresenter> {
    private static final String TAG = KistenStapelnActivity.class.getSimpleName();
    private static final int BALANCE_TOLERANCE = 130;
    private float crateStartX;
    private float crateSTartY;
    private int createWidth;
    private int crateHeight;

    private KistenStapelnState gameState = KistenStapelnState.MOVING_CRATE;

    private TextView nextPlayerText;
    private TextView crateCounter;
    private TextView currentPlayerText;
    private RelativeLayout nextPlayerPanel;
    private RelativeLayout allLayout;

    private List<ImageView> towerImageList = new ArrayList<>();
    private ImageView currentCrate;

    private float towerHeight = 0;
    private float targetY;

    private boolean touchDown = false;

    private float balance = 0;

    @Override
    protected void initPresenter() {
        this.presenter = new BaseMiniGamePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_kisten_stapeln);

        this.currentCrate = this.findViewById(R.id.beerCrate0);
        this.towerImageList.add((ImageView) this.findViewById(R.id.targetImage));
        this.nextPlayerText = this.findViewById(R.id.nextPlayerText);
        this.nextPlayerPanel = this.findViewById(R.id.nextPlayerLayout);
        this.allLayout = this.findViewById(R.id.allLayout);
        this.crateCounter = this.findViewById(R.id.crateCounter);
        this.currentPlayerText = this.findViewById(R.id.currentPlayerLabel);


        ImageButton tutorialButton = this.findViewById(R.id.tutorialButton);
        ImageButton backButton = this.findViewById(R.id.backButton);

        if (this.presenter.isFromMainGame()) {
            TextView backText = this.findViewById(R.id.backText);
            backButton.setVisibility(View.GONE);
            backText.setVisibility(View.GONE);
            this.currentPlayerText.setText(this.presenter.getCurrentPlayerName());
        } else {
            this.currentPlayerText.setVisibility(View.GONE);
        }

        backButton.setOnClickListener(this);
        tutorialButton.setOnClickListener(this);
    }

    private float getTargetY() {
        ImageView element = this.towerImageList.get(this.towerImageList.size() - 1);
        return element.getY() - this.currentCrate.getHeight() / 2;
    }

    private void crateFall() {
        float targetY = this.towerImageList.get(this.towerImageList.size() - 1).getY() - this.currentCrate.getHeight();
        float deltaY = targetY - this.currentCrate.getY();
        ObjectAnimator fallingCrate = ObjectAnimator.ofFloat(this.currentCrate, "y", targetY);
        fallingCrate.setDuration((long) deltaY);
        fallingCrate.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                KistenStapelnActivity.this.onCrateLanded();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        fallingCrate.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (this.gameState == KistenStapelnState.MOVING_CRATE) {
                    this.crateStartX = this.currentCrate.getX();
                    this.crateSTartY = this.currentCrate.getY();
                    this.createWidth = this.currentCrate.getWidth();
                    this.crateHeight = this.currentCrate.getHeight();
                    this.targetY = this.getTargetY();
                    this.touchDown = true;
                } else if (this.gameState == KistenStapelnState.END_TURN) {
                    this.startTurn();
                } else if (this.gameState == KistenStapelnState.GAME_END) {
                    this.presenter.leaveGame();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (this.gameState == KistenStapelnState.MOVING_CRATE) {
                    if (this.touchDown) {
                        this.touchDown = false;
                        this.gameState = KistenStapelnState.FALLING_CRATE;
                        this.crateFall();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.gameState == KistenStapelnState.MOVING_CRATE) {
                    if (this.touchDown) {
                        this.currentCrate.setX(event.getRawX() - this.currentCrate.getWidth() / 2);
                        if (event.getRawY() < this.targetY) {
                            this.currentCrate.setY(event.getRawY() - this.currentCrate.getHeight() / 2);
                        }
                    }
                }
                break;
        }
        return true;
    }

    public void onCrateLanded() {
        this.gameState = KistenStapelnState.LANDED;
        float movement = (this.screenWidth / 2 - this.currentCrate.getX() - this.currentCrate.getWidth() / 2) * (this
                .towerImageList.size() * 2);
        this.balance += movement;
        Log.d(TAG, "balance = " + this.balance);
        if (this.checkIfFalling()) {
            this.startTowerFall();
        } else {
            this.endTurn();
        }
    }

    private void endGame() {
        String losingPlayerText = "";
        int drinkCount = this.towerImageList.size() - 1;
        if (this.presenter.isFromMainGame()) {
            losingPlayerText = this.presenter.getCurrentPlayerName() + "\n";
            DrinkHelper.increaseCurrentPlayer(drinkCount, this);
        }

        this.nextPlayerPanel.setVisibility(View.VISIBLE);
        this.nextPlayerText.setText(losingPlayerText + this.getString(R.string.minigame_kisten_stapeln_drink,
                drinkCount));
        this.gameState = KistenStapelnState.GAME_END;
    }

    private void startTowerFall() {
        this.gameState = KistenStapelnState.FALLING_TOWER;
        this.towerImageList.add(this.currentCrate);
        for (ImageView iv : this.towerImageList) {
            if (iv != this.towerImageList.get(0)) {
                ObjectAnimator xAnim = ObjectAnimator.ofFloat(iv, "x", (iv.getX() - this.balance) / 2);
                xAnim.setDuration(1000);
                ObjectAnimator yAnim = ObjectAnimator.ofFloat(iv, "y", this.screenHeight + 1000);
                yAnim.setDuration(1500);
                if (iv == this.towerImageList.get(this.towerImageList.size() - 1)) {
                    yAnim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            KistenStapelnActivity.this.endGame();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
                xAnim.start();
                yAnim.start();
            }
        }
    }

    private void endTurn() {
        this.towerHeight = this.towerImageList.get(this.towerImageList.size() - 1).getY();
        if (this.towerHeight > this.screenHeight / 2) {
            this.towerHeight = 0;
        }
        this.towerImageList.add(this.currentCrate);
        this.nextPlayerPanel.setVisibility(View.VISIBLE);

        if (this.presenter.isFromMainGame()) {
            this.presenter.nextPlayer();
            this.currentPlayerText.setText(this.presenter.getCurrentPlayerName());
            this.nextPlayerText.setText(this.presenter.getCurrentPlayerName());
        }

        this.crateCounter.setText(this.getString(R.string.minigame_kisten_stapeln_crate_count, this.towerImageList
                .size() - 1));
        this.currentCrate = new ImageView(this);
        this.currentCrate.setX(this.crateStartX);
        this.currentCrate.setY(this.crateSTartY);
        this.currentCrate.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                this.createWidth,
                this.crateHeight);
        this.currentCrate.setImageResource(R.drawable.beer_crate);
        this.allLayout.addView(this.currentCrate, lp);
        this.nextPlayerPanel.bringToFront();
        this.gameState = KistenStapelnState.END_TURN;
    }

    private void startTurn() {
        this.nextPlayerPanel.setVisibility(View.GONE);

        if (this.towerHeight != 0) {
            this.gameState = KistenStapelnState.MOVING_TOWER;
            this.moveTowerDown(this.towerHeight);
        } else {
            this.gameState = KistenStapelnState.MOVING_CRATE;
        }
    }

    private void moveTowerDown(float towerHeight) {
        float movingDistance = this.screenHeight - towerHeight;

        for (int i = 0; i < this.towerImageList.size(); i++) {

            ObjectAnimator anim = ObjectAnimator.ofFloat(this.towerImageList.get(i), "y", this.towerImageList.get(i)
                    .getY() + movingDistance);
            anim.setDuration(1000);
            if (i == this.towerImageList.size() - 1) {
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        KistenStapelnActivity.this.gameState = KistenStapelnState.MOVING_CRATE;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
            anim.start();
        }
    }

    private boolean checkIfFalling() {
        return this.balance > BALANCE_TOLERANCE || this.balance < -BALANCE_TOLERANCE;
    }
}
