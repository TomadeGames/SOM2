package com.tomade.saufomat.activity.mainGame;

import android.animation.Animator;
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
    private static final long ICON_STOP_DURATION = 500;

    private MainGameState gameState;
    private ImageView leftIcon;
    private ImageView middleIcon;
    private ImageView rightIcon;
    private ImageView[] icons = new ImageView[3];
    private IconState[] iconStates = {IconState.EASY, IconState.MEDIUM, IconState.HARD};

    private Animation leftRollingAnimation;
    private Animation middleRollingAnimation;
    private Animation rightRollingAnimation;

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
                this.stopLeftAnimation();
                this.gameState = MainGameState.STOP1;
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
                MainGameActivity.this.rightIcon.animate().y(-500).setDuration(0).setListener(new Animator
                        .AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        Log.d(TAG, "right rolling started");
                        MainGameActivity.this.rightIcon.animate().setListener(null);
                        MainGameActivity.this.rightIcon.startAnimation(
                                MainGameActivity.this.rightRollingAnimation);
                        MainGameActivity.this.rightRolling = true;
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
                MainGameActivity.this.middleIcon.animate().y(-500).setDuration(0).setListener
                        (new Animator
                                .AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                MainGameActivity.this.middleIcon.animate().setListener(null);
                                MainGameActivity.this.middleIcon.startAnimation(
                                        MainGameActivity.this.middleRollingAnimation);
                                MainGameActivity.this.middleRolling = true;
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
                MainGameActivity.this.leftIcon.animate().y(-500).setDuration(0).setListener(new Animator
                        .AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        MainGameActivity.this.leftIcon.animate().setListener(null);
                        MainGameActivity.this.leftIcon.startAnimation(
                                MainGameActivity.this.leftRollingAnimation);
                        MainGameActivity.this.leftRolling = true;
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

        ImageView view = this.icons[viewIndex];

        IconState newIconState = this.presenter.getRandomIconState();
        this.iconStates[viewIndex] = newIconState;
        if (newIconState == IconState.EASY) {
            view.setImageResource(easyImage);
        } else if (newIconState == IconState.MEDIUM) {
            view.setImageResource(mediumImage);
        } else if (newIconState == IconState.HARD) {
            view.setImageResource(hardImage);
        } else {
            view.setImageResource(gameImage);
        }
    }

    private void moveSaufOMeter() {
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
        this.leftRollingAnimation = this.initRollingAnimation(LEFT_ICON_ANIMATION_DURATION, new Animation
                .AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                changeIcon(0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "moving leftIcon to endPosition");
                MainGameActivity.this.leftIcon.animate().y(getIconStopPosition()).setDuration(ICON_STOP_DURATION)
                        .start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                changeIcon(0);
            }
        });

        this.middleRollingAnimation = this.initRollingAnimation(MIDDLE_ICON_ANIMATION_DURATION, new Animation
                .AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                changeIcon(1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "moving middleIcon to endposition");
                MainGameActivity.this.middleIcon.animate().y(getIconStopPosition()).setDuration(ICON_STOP_DURATION)
                        .start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                changeIcon(1);
            }
        });

        this.rightRollingAnimation = this.initRollingAnimation(RIGHT_ICON_ANIMATION_DURATION, new Animation
                .AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                changeIcon(2);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "moving rightIcon to endPosition");
                MainGameActivity.this.rightIcon.animate().y(getIconStopPosition()).setDuration(ICON_STOP_DURATION)
                        .start();
                moveSaufOMeter();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                changeIcon(2);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void initPresenter() {
        this.presenter = new MainGamePresenter(this);
    }
}
