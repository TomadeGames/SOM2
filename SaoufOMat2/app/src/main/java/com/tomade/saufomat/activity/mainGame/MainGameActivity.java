package com.tomade.saufomat.activity.mainGame;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.BaseActivity;
import com.tomade.saufomat.activity.mainGame.listener.IconAnimationListener;
import com.tomade.saufomat.activity.mainGame.listener.IconAnimatorListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainGameActivity extends BaseActivity<MainGamePresenter> {
    private static final String TAG = MainGameActivity.class.getSimpleName();
    private static final long LEFT_ICON_ANIMATION_DURATION = 400;
    private static final long MIDDLE_ICON_ANIMATION_DURATION = 300;
    private static final long RIGHT_ICON_ANIMATION_DURATION = 250;
    private static final long SAUFOMETER_ANIMATION_TICK_DURATION = 200;
    private static final int SAUFOMETER_WAITING_FRAMES_AMOUNT = 2;
    private static final long ICONS_ANIMATION_DISTANCE = 1000;
    private static final int ROLLING_ANIMATION_DISTANCE = 3;

    private MainGameState gameState;
    private ImageView leftIcon;
    private ImageView middleIcon;
    private ImageView rightIcon;
    private ImageView[] icons = new ImageView[3];
    private IconState[] iconStates = {IconState.EASY, IconState.MEDIUM, IconState.HARD};

    private Animation leftRollingAnimation;
    private Animation middleRollingAnimation;
    private Animation rightRollingAnimation;

    private boolean rollStarted = false;
    private boolean leftRolling = false;
    private boolean middleRolling = false;
    private boolean rightRolling = false;
    private ImageView saufOMeter;

    private float getIconStopPosition() {
        return this.screenHeight / 6;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_new_main_game);

        this.gameState = MainGameState.GAME_START;

        TextView playerNameTextView = this.findViewById(R.id.playerName);
        playerNameTextView.setText(this.presenter.getCurrentPlayer().getName());
        this.leftIcon = this.findViewById(R.id.GameIconLeft);
        this.middleIcon = this.findViewById(R.id.GameIconMiddle);
        this.rightIcon = this.findViewById(R.id.GameIconRight);
        this.saufOMeter = this.findViewById(R.id.SaufOMeter);
        this.icons[0] = this.leftIcon;
        this.icons[1] = this.middleIcon;
        this.icons[2] = this.rightIcon;
        this.moveIconsToCorrectPositions();
        this.initRollingAnimations();
        this.findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTouchUp();
            }
        });
    }

    private void moveIconsToCorrectPositions() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = (int) (this.screenWidth / 6.1f);
        params.topMargin = this.screenHeight / 6;
        this.leftIcon.setLayoutParams(params);

        RelativeLayout.LayoutParams middleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        middleParams.leftMargin = this.screenWidth / 2 + this.icons[1].getWidth() / 2;

        this.middleIcon.setLayoutParams(params);
        this.middleIcon.setX((int) (this.screenWidth / 5.25f));

        RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.leftMargin = middleParams.leftMargin;
        this.rightIcon.setLayoutParams(params);
        this.rightIcon.setX((int) (this.screenWidth / 2.6f));

    }

    private void handleTouchUp() {
        Log.d(TAG, "Touch up in Gamestate " + this.gameState);
        switch (this.gameState) {
            case GAME_START:
                this.startLeftAnimation();
                this.startMiddleAnimation();
                this.startRightAnimation();
                break;
            case ROLLING_ALL:
                if (this.rollStarted) {
                    this.stopLeftAnimation();
                    this.gameState = MainGameState.STOP1;
                }
                break;
            case STOP1:
                this.stopMiddleAnimation();
                this.gameState = MainGameState.STOP2;
                break;
            case STOP2:
                this.stopRightAnimation();
                this.gameState = MainGameState.STOP_ALL;
                break;
            case STOP_ALL:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //TODO: Hebel animation
                break;
            case MotionEvent.ACTION_UP:
                this.handleTouchUp();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void stopLeftAnimation() {
        Log.d(TAG, "left canceled");
        this.leftIcon.clearAnimation();
        this.leftIcon.setAnimation(null);
        this.leftRolling = false;
    }

    private void stopMiddleAnimation() {
        Log.d(TAG, "middle canceled");
        this.middleRollingAnimation.cancel();
        this.middleRollingAnimation.reset();
        this.middleRolling = false;
    }

    private void stopRightAnimation() {
        Log.d(TAG, "right canceled");
        this.rightRollingAnimation.cancel();
        this.rightRollingAnimation.reset();
        this.rightRolling = false;
    }

    public void checkIfAllRolling(IconPosition iconPosition) {
        switch (iconPosition) {
            case LEFT:
                this.leftRolling = true;
                break;
            case MIDDLE:
                this.middleRolling = true;
                break;
            case RIGHT:
                this.rightRolling = true;
                break;
        }
        if (this.leftRolling && this.middleRolling && this.rightRolling) {
            this.gameState = MainGameState.ROLLING_ALL;
        }
    }

    private void startRightAnimation() {
        this.rightIcon.animate().y(ICONS_ANIMATION_DISTANCE).setDuration(500).setListener(new IconAnimatorListener
                (this.rightIcon, this.rightRollingAnimation, IconPosition.RIGHT, this)).start();
    }

    private void startMiddleAnimation() {
        this.middleIcon.animate().y(ICONS_ANIMATION_DISTANCE).setDuration(500).setListener(new IconAnimatorListener
                (this.middleIcon, this.middleRollingAnimation, IconPosition.MIDDLE, this)).start();
    }

    private void startLeftAnimation() {
        this.leftIcon.animate().y(ICONS_ANIMATION_DISTANCE).setDuration(500).setListener(new IconAnimatorListener
                (this.leftIcon, this.leftRollingAnimation, IconPosition.LEFT, this)).start();
    }

    public void changeIcon(IconPosition iconPosition) {
        int viewIndex = -1;
        switch (iconPosition) {
            case LEFT:
                viewIndex = 0;
                break;
            case MIDDLE:
                viewIndex = 1;
                break;
            case RIGHT:
                viewIndex = 2;
                break;

        }
        ImageView view = this.icons[viewIndex];

        IconState newIconState = this.presenter.getRandomIconState();
        this.iconStates[viewIndex] = newIconState;
        view.setImageResource(newIconState.getImageId());
    }

    public void animateSaufOMeter() {
        final int lastFrame = this.presenter.getCurrentDifficult(this.iconStates[0], this.iconStates[1], this
                .iconStates[2]);

        final List<Integer> imageIds = new ArrayList<>();
        if (lastFrame >= 0) {
            imageIds.add(R.drawable.saufometer1);
        }
        if (lastFrame >= 2) {
            imageIds.add(R.drawable.saufometer2);
        }
        if (lastFrame >= 3) {
            imageIds.add(R.drawable.saufometer3);
        }
        if (lastFrame >= 4) {
            imageIds.add(R.drawable.saufometer4);
        }
        if (lastFrame >= 5) {
            imageIds.add(R.drawable.saufometer5);
        }
        if (lastFrame >= 6) {
            imageIds.add(R.drawable.saufometer6);
        }
        if (lastFrame >= 7) {
            imageIds.add(R.drawable.saufometer7);
            if (lastFrame == 7) {
                imageIds.add(R.drawable.saufometer10);
                imageIds.add(R.drawable.saufometer7);
                imageIds.add(R.drawable.saufometer10);
                imageIds.add(R.drawable.saufometer7);
                imageIds.add(R.drawable.saufometer10);
            }
        }
        if (lastFrame >= 8) {
            imageIds.add(R.drawable.saufometer8);
            if (lastFrame == 8) {
                imageIds.add(R.drawable.saufometer11);
                imageIds.add(R.drawable.saufometer8);
                imageIds.add(R.drawable.saufometer11);
                imageIds.add(R.drawable.saufometer8);
                imageIds.add(R.drawable.saufometer11);
            }
        }
        if (lastFrame >= 9) {
            imageIds.add(R.drawable.saufometer9);
            imageIds.add(R.drawable.saufometer12);
            imageIds.add(R.drawable.saufometer9);
            imageIds.add(R.drawable.saufometer12);
            imageIds.add(R.drawable.saufometer9);
            imageIds.add(R.drawable.saufometer12);
        }

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int animationCounter = 0;
            boolean isWaitingTime = false;

            @Override
            public void run() {
                if (!this.isWaitingTime) {
                    MainGameActivity.this.saufOMeter.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (animationCounter < imageIds.size()) {
                                        MainGameActivity.this.saufOMeter.setImageResource(imageIds.get
                                                (animationCounter));
                                    } else {
                                        MainGameActivity.this.saufOMeter.setImageResource(imageIds.get(imageIds
                                                .size() - 1));
                                    }
                                }
                            });
                } else if (this.animationCounter - imageIds.size() > SAUFOMETER_WAITING_FRAMES_AMOUNT) {
                    timer.cancel();
                    MainGameActivity.this.presenter.changeToTaskView();
                }
                this.animationCounter++;
                if (this.animationCounter >= imageIds.size() - 1) {
                    this.isWaitingTime = true;
                }
            }
        }, 0, SAUFOMETER_ANIMATION_TICK_DURATION);
    }

    private Animation initRollingAnimation(long duration, Animation.AnimationListener listener) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_PARENT, 0.0F, Animation.RELATIVE_TO_SELF, ROLLING_ANIMATION_DISTANCE);
        animation.setDuration(duration);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setAnimationListener(listener);
        return animation;
    }

    private void initRollingAnimations() {
        this.leftRollingAnimation = this.initRollingAnimation(LEFT_ICON_ANIMATION_DURATION, new IconAnimationListener
                (this.leftIcon, this.getIconStopPosition(), IconPosition.LEFT, this));

        this.middleRollingAnimation = this.initRollingAnimation(MIDDLE_ICON_ANIMATION_DURATION, new
                IconAnimationListener(this.middleIcon, this.getIconStopPosition(), IconPosition.MIDDLE, this));

        this.rightRollingAnimation = this.initRollingAnimation(RIGHT_ICON_ANIMATION_DURATION, new
                IconAnimationListener(this.rightIcon, this.getIconStopPosition(), IconPosition.RIGHT, this));
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void initPresenter() {
        this.presenter = new MainGamePresenter(this);
    }

    public void rollStarted() {
        this.rollStarted = true;
    }
}
