package com.tomade.saufomat.activity.mainGame;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.tomade.saufomat.AdService;
import com.tomade.saufomat.MiniGameProvider;
import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.mainGame.task.Task;
import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;
import com.tomade.saufomat.activity.mainGame.task.TaskProvider;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.constant.MiniGame;
import com.tomade.saufomat.model.Player;

import java.util.ArrayList;
import java.util.List;
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

    private static final int AD_LIMIT = 7; //Original 8, erstmal 7
    private static int adCounter = 0;

    private Player currentPlayer;
    private ArrayList<Player> playerList;

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
        AdService.initializeInterstitialAd(this);
        AdService.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                adCounter = 0;
                changeView();
            }
        });
        Bundle extras = this.getIntent().getExtras();
        ArrayList<Player> players = (ArrayList<Player>) extras.getSerializable(IntentParameter.PLAYER_LIST);
        Player currentPlayer = (Player) extras.getSerializable(IntentParameter.CURRENT_PLAYER);
        adCounter = extras.getInt(IntentParameter.MainGame.AD_COUNTER);
        boolean newGame = extras.getBoolean(IntentParameter.MainGame.NEW_GAME);

        if (newGame) {
            TaskProvider taskProvider = new TaskProvider(this);
            taskProvider.resetTasks();
        }
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
            final int WAITING_FRAMES = 3;
            int animationCounter = 0;
            boolean isWaitingTime = false;

            @Override
            public void run() {
                if (!this.isWaitingTime) {
                    NewMainGameActivity.this.saufOMeter.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    NewMainGameActivity.this.saufOMeter.setImageResource(imageIds.get
                                            (animationCounter));
                                }
                            });
                } else if (this.animationCounter - imageIds.size() > this.WAITING_FRAMES) {
                    timer.cancel();
                    if (isMiniGame) {
                        changeToTaskView(new MiniGameProvider(context).getRandomMiniGame());
                    } else {
                        changeToTaskView(new TaskProvider(context).getNextTask(difficult));
                    }
                }
                this.animationCounter++;
                if (this.animationCounter >= imageIds.size() - 1) {
                    Log.d(TAG, "SaufOMeterAnimation canceled");
                    this.isWaitingTime = true;
                }
            }
        }, 0, 250);
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
                moveSaufOMeter();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, "right repeated");
                changeIcon(2);
            }
        });
    }

    private void changeToTaskView(Task task) {
        Intent taskViewIntent = new Intent(this, TaskViewActivity.class);
        taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK, task);
        MainGameUtils.saveGame(this, adCounter, this.currentPlayer, task);
        taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME, false);

        this.changeToTaskView(taskViewIntent);
    }

    private void changeToTaskView(MiniGame miniGame) {
        Intent taskViewIntent = new Intent(this, TaskViewActivity.class);
        taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_MINI_GAME, miniGame);
        taskViewIntent.putExtra(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME, true);
        MainGameUtils.saveGame(this, adCounter, this.currentPlayer, miniGame);

        this.changeToTaskView(taskViewIntent);
    }

    private void changeToTaskView(Intent taskViewIntent) {
        taskViewIntent.putExtra(IntentParameter.PLAYER_LIST, this.playerList);
        taskViewIntent.putExtra(IntentParameter.CURRENT_PLAYER, this.currentPlayer);
        if (adCounter >= AD_LIMIT) {
            adCounter = 0;
            if (!AdService.showAd()) {
                Log.e(TAG, "Ad cannot be shown");
                this.changeView(taskViewIntent);
            }
        } else {
            adCounter++;
            this.changeView(taskViewIntent);
        }
    }

    private void changeView(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(IntentParameter.MainGame.AD_COUNTER, adCounter);
        this.finish();
        this.startActivity(intent);
    }
}
