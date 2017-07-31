package com.tomade.saufomat.activity.mainGame;

import android.animation.Animator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.tomade.saufomat.AdService;
import com.tomade.saufomat.MiniGameProvider;
import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.TaskProvider;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.constant.MiniGame;
import com.tomade.saufomat.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainGameActivity extends Activity implements View.OnClickListener {
    private static final String TAG = MainGameActivity.class.getSimpleName();
    private static final long LEFT_ICON_ANIMATION_DURATION = 400;
    private static final long MIDDLE_ICON_ANIMATION_DURATION = 300;
    private static final long RIGHT_ICON_ANIMATION_DURATION = 250;
    private static final long SAUFOMETER_ANIMATION_TICK_DURATION = 200;
    private static final int SAUFOMETER_WAITING_FRAMES_AMOUNT = 2;
    private static final long ICONS_ANIMATION_DISTANCE = 1000;
    private static final int ROLLING_ANIMATION_DISTANCE = 3;
    private static final long ICON_STOP_DURATION = 500;

    private static final int AD_LIMIT = 7; //Original 8, erstmal 7
    private static int adCounter = 0;

    private static Random random = new Random(System.currentTimeMillis());

    private Player currentPlayer;
    private ArrayList<Player> playerList;

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

    private Intent taskViewIntent;

    private int screenHeight;
    private int screenWidth;

    private float getIconStopPosition() {
        return this.screenHeight / 6;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_new_main_game);
        AdService.initializeInterstitialAd(this);
        AdService.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                adCounter = 0;
                changeView();
            }
        });
        Bundle extras = this.getIntent().getExtras();
        this.playerList = (ArrayList<Player>) extras.getSerializable(IntentParameter.PLAYER_LIST);
        this.currentPlayer = (Player) extras.getSerializable(IntentParameter.CURRENT_PLAYER);
        boolean newGame = extras.getBoolean(IntentParameter.MainGame.NEW_GAME);

        Point screenSize = MainGameUtils.getScreenSize(this);
        this.screenHeight = screenSize.y;
        this.screenWidth = screenSize.x;

        if (newGame) {
            TaskProvider taskProvider = new TaskProvider(this);
            taskProvider.resetTasks();
        }
        this.gameState = MainGameState.GAME_START;

        this.taskViewIntent = new Intent(this, TaskViewActivity.class);

        TextView playerNameTextView = this.findViewById(R.id.playerName);
        playerNameTextView.setText(this.currentPlayer.getName());
        this.leftIcon = this.findViewById(R.id.GameIconLeft);
        this.middleIcon = this.findViewById(R.id.GameIconMiddle);
        this.rightIcon = this.findViewById(R.id.GameIconRight);
        this.saufOMeter = this.findViewById(R.id.SaufOMeter);
        this.icons[0] = this.leftIcon;
        this.icons[1] = this.middleIcon;
        this.icons[2] = this.rightIcon;
        View startButton = this.findViewById(R.id.startButton);
        this.moveIconsToCorrectPositions();
        this.initAnimations();

        startButton.setOnClickListener(this);
    }

    private void moveIconsToCorrectPositions() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = (int) (this.screenWidth / 6.1f);
        params.topMargin = this.screenHeight / 6;
        this.leftIcon.setLayoutParams(params);

        RelativeLayout.LayoutParams middleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        middleParams.leftMargin = this.screenWidth / 2;

        this.middleIcon.setLayoutParams(params);
        this.middleIcon.setX((int) (this.screenWidth / 5.25f));

        RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.leftMargin = middleParams.leftMargin;
        this.rightIcon.setLayoutParams(params);
        this.rightIcon.setX((int) (this.screenWidth / 2.6f));
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

        int fullChance = MainGameUtils.EASY_CHANCE + MainGameUtils.MEDIUM_CHANCE + MainGameUtils.HARD_CHANCE +
                MainGameUtils.GAME_CHANCE;
        ImageView view = this.icons[viewIndex];

        int value = random.nextInt(fullChance);
        if (value < MainGameUtils.EASY_CHANCE) {
            view.setImageResource(easyImage);
            this.iconStates[viewIndex] = IconState.EASY;
        } else if (value < MainGameUtils.EASY_CHANCE + MainGameUtils.MEDIUM_CHANCE) {
            view.setImageResource(mediumImage);
            this.iconStates[viewIndex] = IconState.MEDIUM;
        } else if (value < MainGameUtils.EASY_CHANCE + MainGameUtils.MEDIUM_CHANCE + MainGameUtils.HARD_CHANCE) {
            view.setImageResource(hardImage);
            this.iconStates[viewIndex] = IconState.HARD;
        } else {
            view.setImageResource(gameImage);
            this.iconStates[viewIndex] = IconState.GAME;
        }
    }

    private void moveSaufOMeter() {
        DifficultWithSaufOMeterEndFrame difficultWithSaufOMeterEndFrame = MainGameUtils.getCurrentDifficult(
                this.iconStates[0], this.iconStates[1], this.iconStates[2]);

        final int lastFrame = difficultWithSaufOMeterEndFrame.getSaufOMeterEndFrame();
        final TaskDifficult difficult = difficultWithSaufOMeterEndFrame.getDifficult();

        if (difficult == TaskDifficult.EASY_WIN) {
            this.currentPlayer.getStatistic().increaseEasyWins();
        } else if (difficult == TaskDifficult.MEDIUM_WIN) {
            this.currentPlayer.getStatistic().increaseMediumWins();
        } else if (difficult == TaskDifficult.HARD_WIN) {
            this.currentPlayer.getStatistic().increaseHardWins();
        }

        final boolean isMiniGame = difficult == TaskDifficult.GAME;

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

        final Context context = this;
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
                    if (isMiniGame) {
                        changeToTaskView(new MiniGameProvider(context).getRandomMiniGame());
                    } else {
                        changeToTaskView(new TaskProvider(context).getNextTask(difficult));
                    }
                }
                this.animationCounter++;
                if (this.animationCounter >= imageIds.size() - 1) {
                    this.isWaitingTime = true;
                }
            }
        }, 0, SAUFOMETER_ANIMATION_TICK_DURATION);
    }

    private void initAnimations() {
        this.leftRollingAnimation =
                new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, 0.0F,
                        Animation.RELATIVE_TO_PARENT, 0.0F, Animation.RELATIVE_TO_SELF, ROLLING_ANIMATION_DISTANCE);
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
                MainGameActivity.this.leftIcon.animate().y(getIconStopPosition()).setDuration(ICON_STOP_DURATION)
                        .start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                changeIcon(0);
            }
        });

        this.middleRollingAnimation =
                new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, 0.0F,
                        Animation.RELATIVE_TO_PARENT, 0.0F, Animation.RELATIVE_TO_SELF, ROLLING_ANIMATION_DISTANCE);
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
                MainGameActivity.this.middleIcon.animate().y(getIconStopPosition()).setDuration(ICON_STOP_DURATION)
                        .start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                changeIcon(1);
            }
        });

        this.rightRollingAnimation =
                new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, 0.0F,
                        Animation.RELATIVE_TO_PARENT, 0.0F, Animation.RELATIVE_TO_SELF, ROLLING_ANIMATION_DISTANCE);
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

    private void changeToTaskView(Task task) {
        this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK, task);
        MainGameUtils.saveGame(this, adCounter, this.currentPlayer, task);
        this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME, false);

        this.changeToTaskView();
    }

    private void changeToTaskView(MiniGame miniGame) {
        this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_MINI_GAME, miniGame);
        this.taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME, true);
        MainGameUtils.saveGame(this, adCounter, this.currentPlayer, miniGame);

        this.changeToTaskView();
    }

    private void changeToTaskView() {
        this.taskViewIntent.putExtra(IntentParameter.PLAYER_LIST, this.playerList);
        this.taskViewIntent.putExtra(IntentParameter.CURRENT_PLAYER, this.currentPlayer);
        Log.d(TAG, "AdCounter is " + adCounter + "/" + AD_LIMIT);
        if (adCounter >= AD_LIMIT) {
            adCounter = 0;

            final InterstitialAd interstitialAd = AdService.getInterstitialAd();

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                        Log.i(TAG, "InterstitialAd successful shown");
                        AdService.requestAd();
                    } else {
                        Log.e(TAG, "Ad cannot be shown");
                        AdService.requestAd();
                        changeView();
                    }
                }
            });
        } else {
            adCounter++;
            Log.d(TAG, "Adcounter increased: " + adCounter);
            this.changeView();
        }
    }

    private void changeView() {
        this.taskViewIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.taskViewIntent.putExtra(IntentParameter.MainGame.AD_COUNTER, adCounter);
        this.finish();
        this.startActivity(this.taskViewIntent);
    }

    @Override
    public void onBackPressed() {
    }
}
