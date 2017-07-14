package com.tomade.saufomat.activity.mainGame;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NewMainGameActivity extends Activity implements View.OnClickListener {
    private static final String TAG = NewMainGameActivity.class.getSimpleName();
    private static final long LEFT_ICON_ANIMATION_DURATION = 500;
    private static final long MIDDLE_ICON_ANIMATION_DURATION = 350;
    private static final long RIGHT_ICON_ANIMATION_DURATION = 400;
    private static final long ICONS_ANIMATION_DISTANCE = 1000;
    private static final long ICON_STOP_POSITION = 180;
    private static final long ICON_STOP_DURATION = 500;
    private static final int EASY_CHANCE = 4;
    private static final int MEDIUM_CHANCE = 4;
    private static final int HARD_CHANCE = 3;
    private static final int GAME_CHANCE = 1;

    private MainGameState gameState;
    private ImageView leftIcon;
    private ImageView middleIcon;
    private ImageView rightIcon;
    private ImageView[] icons = new ImageView[3];

    private TaskDifficult[] difficults = {TaskDifficult.EASY, TaskDifficult.MEDIUM, TaskDifficult.HARD};

    private Animation leftRollingAnimation;
    private Animation middleRollingAnimation;
    private Animation rightRollingAnimation;

    private boolean leftRolling = false;
    private boolean middleRolling = false;
    private boolean rightRolling = false;
    private ImageView saufOMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.gameState = MainGameState.GAME_START;

        this.setContentView(R.layout.activity_new_main_game);
        this.leftIcon = (ImageView) this.findViewById(R.id.GameIconLeft);
        this.middleIcon = (ImageView) this.findViewById(R.id.GameIconMiddle);
        this.rightIcon = (ImageView) this.findViewById(R.id.GameIconRight);
        this.saufOMeter = (ImageView) this.findViewById(R.id.SaufOMeter);
        this.icons[0] = this.leftIcon;
        this.icons[1] = this.middleIcon;
        this.icons[2] = this.rightIcon;
        View startButton = this.findViewById(R.id.startButton);

        this.initAnimations();

        startButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startButton:
                Log.d(TAG, "startbutton clicked in Gamestate " + this.gameState);
                switch (this.gameState) {
                    case GAME_START:
                        this.startLeftAnimation();
                        this.startMiddleAnimation();
                        this.startRightAnimation();
                        break;
                    case ROLLING_ALL:
                        this.stopRightAnimation();
                        this.gameState = MainGameState.STOP1;
                        break;
                    case STOP1:
                        this.stopMiddleAnimation();
                        this.gameState = MainGameState.STOP2;
                        break;
                    case STOP2:
                        this.stopLeftAnimation();
                        this.gameState = MainGameState.STOP_ALL;
                        break;
                    case STOP_ALL:
                        break;
                }
                break;
        }
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

    private void checkIfAllRolling() {
        if (this.leftRolling && this.middleRolling && this.rightRolling) {
            this.gameState = MainGameState.ROLLING_ALL;
        }
    }

    private void startRightAnimation() {
        Log.d(TAG, "startRightAnimation");
        this.rightIcon.animate().y(ICONS_ANIMATION_DISTANCE).setDuration(500).setListener(new Animator
                .AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.d(TAG, "right moving to Top");
                NewMainGameActivity.this.rightIcon.animate().y(-500).setDuration(0).setListener(new Animator
                        .AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        Log.d(TAG, "right rolling started");
                        NewMainGameActivity.this.rightIcon.animate().setListener(null);
                        NewMainGameActivity.this.rightIcon.startAnimation(
                                NewMainGameActivity.this.rightRollingAnimation);
                        NewMainGameActivity.this.rightRolling = true;
                        checkIfAllRolling();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();
    }

    private void startMiddleAnimation() {
        this.middleIcon.animate().y(ICONS_ANIMATION_DISTANCE).setDuration(500).setListener(new Animator
                .AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                NewMainGameActivity.this.middleIcon.animate().y(-500).setDuration(0).setListener(new Animator
                        .AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        NewMainGameActivity.this.middleIcon.animate().setListener(null);
                        NewMainGameActivity.this.middleIcon.startAnimation(
                                NewMainGameActivity.this.middleRollingAnimation);
                        NewMainGameActivity.this.middleRolling = true;
                        checkIfAllRolling();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();
    }

    private void startLeftAnimation() {
        this.leftIcon.animate().y(ICONS_ANIMATION_DISTANCE).setDuration(500).setListener(new Animator
                .AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                NewMainGameActivity.this.leftIcon.animate().y(-500).setDuration(0).setListener(new Animator
                        .AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        NewMainGameActivity.this.leftIcon.animate().setListener(null);
                        NewMainGameActivity.this.leftIcon.startAnimation(
                                NewMainGameActivity.this.leftRollingAnimation);
                        NewMainGameActivity.this.leftRolling = true;
                        checkIfAllRolling();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();
    }

    private void changeIcon(int viewIndex) {
        final int easyImage = R.drawable.beer_icon;
        final int mediumImage = R.drawable.cocktail_icon;
        final int hardImage = R.drawable.shot_icon;
        final int gameImage = R.drawable.dice_icon;

        int fullChance = EASY_CHANCE + MEDIUM_CHANCE + HARD_CHANCE + GAME_CHANCE;
        Random random = new Random(System.currentTimeMillis());
        ImageView view = this.icons[viewIndex];
        Log.d(TAG, "viewIndex: " + viewIndex + ", icons.length: " + this.icons.length + ", View: " + view);

        int value = random.nextInt(fullChance);
        if (value < EASY_CHANCE) {
            view.setImageResource(easyImage);
            this.difficults[viewIndex] = TaskDifficult.EASY;
        } else if (value < EASY_CHANCE + MEDIUM_CHANCE) {
            view.setImageResource(mediumImage);
            this.difficults[viewIndex] = TaskDifficult.MEDIUM;
        } else if (value < EASY_CHANCE + MEDIUM_CHANCE + HARD_CHANCE) {
            view.setImageResource(hardImage);
            this.difficults[viewIndex] = TaskDifficult.HARD;
        } else {
            view.setImageResource(gameImage);
            this.difficults[viewIndex] = TaskDifficult.GAME;
        }
    }

    private void moveSaufOMeter() {
        DifficultWithSaufOMeterEndFrame difficultWithSaufOMeterEndFrame = MainGameUtils.getCurrentDifficult(
                this.difficults[0], this.difficults[1], this.difficults[2]);

        final int lastFrame = difficultWithSaufOMeterEndFrame.getSaufOMeterEndFrame();
        final TaskDifficult difficult = difficultWithSaufOMeterEndFrame.getDifficult();

        final int[] imageIds = {R.drawable.saufometer1,
                R.drawable.saufometer2,
                R.drawable.saufometer3,
                R.drawable.saufometer4,
                R.drawable.saufometer5,
                R.drawable.saufometer6,
                R.drawable.saufometer7,
                R.drawable.saufometer8,
                R.drawable.saufometer9,
                R.drawable.saufometer10,
                R.drawable.saufometer11,
                R.drawable.saufometer11
        };

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int animationCounter = 0;

            @Override
            public void run() {
                if (this.animationCounter > lastFrame) {
                    timer.cancel();
                }
                NewMainGameActivity.this.saufOMeter.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                if (animationCounter < imageIds.length) {
                                    NewMainGameActivity.this.saufOMeter.setImageResource(imageIds[animationCounter]);
                                }
                            }
                        });
                Log.d(TAG, "animationCounter: " + this.animationCounter);
                this.animationCounter++;
            }
        }, 0, 500);
    }

    private void initAnimations() {
        this.leftRollingAnimation =
                new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, 0.0F,
                        Animation.RELATIVE_TO_PARENT, 0.0F, Animation.RELATIVE_TO_SELF, 5);
        this.leftRollingAnimation.setDuration(LEFT_ICON_ANIMATION_DURATION);
        this.leftRollingAnimation.setRepeatCount(Animation.INFINITE);
        this.leftRollingAnimation.setRepeatMode(Animation.RESTART);
        this.leftRollingAnimation.setInterpolator(new DecelerateInterpolator());
        this.leftRollingAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                changeIcon(0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "moving leftIcon to endPosition");
                NewMainGameActivity.this.leftIcon.animate().y(ICON_STOP_POSITION).setDuration(ICON_STOP_DURATION)
                        .start();
                moveSaufOMeter();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                changeIcon(0);
            }
        });

        this.middleRollingAnimation =
                new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, 0.0F,
                        Animation.RELATIVE_TO_PARENT, 0.0F, Animation.RELATIVE_TO_SELF, 5);
        this.middleRollingAnimation.setDuration(MIDDLE_ICON_ANIMATION_DURATION);
        this.middleRollingAnimation.setRepeatCount(Animation.INFINITE);
        this.middleRollingAnimation.setRepeatMode(Animation.RESTART);
        this.middleRollingAnimation.setInterpolator(new DecelerateInterpolator());
        this.middleRollingAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                changeIcon(1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "moving middleIcon to endposition");
                NewMainGameActivity.this.middleIcon.animate().y(ICON_STOP_POSITION).setDuration(ICON_STOP_DURATION)
                        .start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                changeIcon(1);
            }
        });

        this.rightRollingAnimation =
                new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, 0.0F,
                        Animation.RELATIVE_TO_PARENT, 0.0F, Animation.RELATIVE_TO_SELF, 5);
        this.rightRollingAnimation.setDuration(RIGHT_ICON_ANIMATION_DURATION);
        this.rightRollingAnimation.setRepeatCount(Animation.INFINITE);
        this.rightRollingAnimation.setRepeatMode(Animation.RESTART);
        this.rightRollingAnimation.setInterpolator(new DecelerateInterpolator());
        this.rightRollingAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                changeIcon(2);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "moving rightIcon to endPosition");
                NewMainGameActivity.this.rightIcon.animate().y(ICON_STOP_POSITION).setDuration(ICON_STOP_DURATION)
                        .start();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, "right repeated");
                changeIcon(2);
            }
        });
    }
}
