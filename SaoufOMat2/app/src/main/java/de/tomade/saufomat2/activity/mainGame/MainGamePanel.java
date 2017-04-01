package de.tomade.saufomat2.activity.mainGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

import de.tomade.saufomat2.MiniGameProvider;
import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.mainGame.task.Task;
import de.tomade.saufomat2.activity.mainGame.task.TaskDifficult;
import de.tomade.saufomat2.constant.MiniGame;
import de.tomade.saufomat2.factory.TaskFactory;
import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.model.button.ButtonEvent;
import de.tomade.saufomat2.model.button.ButtonListener;
import de.tomade.saufomat2.model.button.DrawableButton;
import de.tomade.saufomat2.model.drawable.SaufOMeter;
import de.tomade.saufomat2.model.drawable.SlotMachineIcon;
import de.tomade.saufomat2.threading.GameLoopThread;
import de.tomade.saufomat2.threading.ThreadedView;

/**
 * Die Logik der SaufOMat ansicht
 * <p>
 * Created by woors on 09.03.2016.
 */
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback, ThreadedView {
    private static final int EASY_CHANCE = 4;
    private static final int MEDIUM_CHANCE = 4;
    private static final int HARD_CHANCE = 3;
    private static final int GAME_CHANCE = 1;

    private static final int SAUFOMETER_BLINK_TIME = 300;
    private static final int SAUFOMETER_ROTATE_TIME = 100;
    private static final int MAIN_VIEW_WAIT_TIME = 1000;
    private static final String TAG = MainGamePanel.class.getSimpleName();
    private static Random random;

    //Thread
    private GameLoopThread thread;
    private String avgFps;
    private long elapsedTime;

    //Player
    private Player currentPlayer;
    private ArrayList<Player> player;

    //Images
    private SlotMachineIcon[] icons;
    private DrawableButton button;
    private SaufOMeter saufOMeter;

    //ScreenSize
    private int screenWith;
    private int screenHeight;

    private MainGameState gameState = MainGameState.GAME_START;

    private Bitmap background;
    private Bitmap slotMachine;
    private Paint currentPlayerTextPaint;

    //Task
    private TaskFactory taskFactory;
    private Task currentTask;
    private TaskDifficult currentDifficult = TaskDifficult.UNDEFINED;

    //MiniGames
    private MiniGame currentMiniGame;
    private MiniGameProvider miniGameProvider;

    //SaufOMeter
    private int saufOMeterEndFrame = 1;
    private float saufometerUpdateCounter = SAUFOMETER_ROTATE_TIME;
    private float waitCounter = MAIN_VIEW_WAIT_TIME;
    private float saufometerBlinkingCounter = SAUFOMETER_BLINK_TIME;
    private int blinkCounter = 0;
    private boolean framesSet = false;

    public MainGamePanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MainGamePanel(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    public MainGamePanel(Context context, Player currentPlayer, ArrayList<Player> players) {
        super(context);
        this.getHolder().addCallback(this);
        random = new Random();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        this.screenWith = size.x;
        this.screenHeight = size.y;

        this.miniGameProvider = new MiniGameProvider();

        this.initContent();
        this.taskFactory = new TaskFactory();

        this.player = players;
        this.currentPlayer = currentPlayer;

        this.thread = new GameLoopThread(this.getHolder(), this);
        this.setFocusable(true);
    }

    private int getIconStopY() {
        return (int) (this.screenHeight / 2.8);
    }

    private void initContent() {
        this.background = BitmapFactory.decodeResource(this.getResources(), R.drawable.slot_machine_background);
        this.background = Bitmap.createScaledBitmap(this.background, this.screenWith, this.screenHeight, true);

        this.slotMachine = BitmapFactory.decodeResource(this.getResources(), R.drawable.slot_machine);
        this.slotMachine = Bitmap.createScaledBitmap(this.slotMachine, this.screenWith, this.screenHeight, true);

        this.currentPlayerTextPaint = new Paint();
        this.currentPlayerTextPaint.setTextSize(100);
        this.currentPlayerTextPaint.setTextAlign(Paint.Align.CENTER);

        Bitmap beerIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.beer_icon);
        Bitmap cocktailIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.cocktail_icon);
        Bitmap shotIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.shot_icon);
        Bitmap startButton = BitmapFactory.decodeResource(this.getResources(), R.drawable.start_button);
        Bitmap gameIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.dice_icon);
        this.icons = new SlotMachineIcon[3];
        this.icons[0] = new SlotMachineIcon(beerIcon, cocktailIcon, shotIcon, gameIcon, (int) (this.screenWith / 3.3
                - this.screenWith / 30), (int) (this.screenHeight / 2.68), this.screenWith, this.screenHeight,
                IconState.EASY);
        this.icons[1] = new SlotMachineIcon(beerIcon, cocktailIcon, shotIcon, gameIcon, this.screenWith / 2 - this
                .screenWith / 30, (int) (this.screenHeight / 2.68), this.screenWith, this.screenHeight, IconState
                .MEDIUM);
        this.icons[2] = new SlotMachineIcon(beerIcon, cocktailIcon, shotIcon, gameIcon, (int) (this.screenWith / 2.9
                * 2 - this.screenWith / 30), (int) (this.screenHeight / 2.68), this.screenWith, this.screenHeight,
                IconState.HARD);

        Bitmap[] saufOMeterFrames = new Bitmap[13];
        saufOMeterFrames[0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.saufometer0);
        saufOMeterFrames[1] = BitmapFactory.decodeResource(this.getResources(), R.drawable.saufometer1);
        saufOMeterFrames[2] = BitmapFactory.decodeResource(this.getResources(), R.drawable.saufometer2);
        saufOMeterFrames[3] = BitmapFactory.decodeResource(this.getResources(), R.drawable.saufometer3);
        saufOMeterFrames[4] = BitmapFactory.decodeResource(this.getResources(), R.drawable.saufometer4);
        saufOMeterFrames[5] = BitmapFactory.decodeResource(this.getResources(), R.drawable.saufometer5);
        saufOMeterFrames[6] = BitmapFactory.decodeResource(this.getResources(), R.drawable.saufometer6);
        saufOMeterFrames[7] = BitmapFactory.decodeResource(this.getResources(), R.drawable.saufometer7);
        saufOMeterFrames[8] = BitmapFactory.decodeResource(this.getResources(), R.drawable.saufometer8);
        saufOMeterFrames[9] = BitmapFactory.decodeResource(this.getResources(), R.drawable.saufometer9);
        saufOMeterFrames[10] = BitmapFactory.decodeResource(this.getResources(), R.drawable.saufometer10);
        saufOMeterFrames[11] = BitmapFactory.decodeResource(this.getResources(), R.drawable.saufometer11);
        saufOMeterFrames[12] = BitmapFactory.decodeResource(this.getResources(), R.drawable.saufometer12);
        this.saufOMeter = new SaufOMeter(saufOMeterFrames, (int) (this.screenWith - this.screenWith / 1.19), (int)
                (this.screenHeight / 1.15), this.screenHeight / 4, this.screenHeight / 4);

        this.setButton(new DrawableButton(startButton, (int) (this.screenWith / 1.35), (int) (this.screenHeight /
                1.4), this.screenWith / 5, this.screenHeight / 5));
        this.getButton().addListener(new ButtonListener() {
            @Override
            public void onInput(ButtonEvent event) {
                MainGamePanel.this.startButtonPressed();
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.thread.setRunning(true);
        if (this.thread.getStatus() == AsyncTask.Status.PENDING) {
            this.thread.execute();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.getButton().checkClick(event.getX(), event.getY());
                break;
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        if (this.gameState == MainGameState.ROLLING_ALL
                || this.gameState == MainGameState.STOP1
                || this.gameState == MainGameState.STOP2
                || this.gameState == MainGameState.STOP_ALL) {
            this.moveIcons();
        } else if (this.gameState == MainGameState.All_IN_POSITION) {
            this.getCurrentDifficult();
            if (this.currentDifficult != TaskDifficult.GAME) {
                this.currentTask = this.taskFactory.getTask(this.currentDifficult);
                this.currentMiniGame = null;
                this.gameState = MainGameState.MOVE_SAUFOMETER;
            } else {
                this.currentMiniGame = this.miniGameProvider.getRandomMiniGameAndRemoveFromList();
                this.gameState = MainGameState.MOVE_SAUFOMETER;
                this.currentTask = null;

            }
        } else if (this.gameState == MainGameState.MOVE_SAUFOMETER) {
            this.moveSaufometer();
        } else if (this.gameState == MainGameState.SAUFOMETER_BLINKING) {
            this.blinkSaufometer();
        } else if (this.gameState == MainGameState.WAITING) {
            this.waitCounter -= this.elapsedTime;
            if (this.waitCounter <= 0) {
                this.waitCounter = MAIN_VIEW_WAIT_TIME;
                this.gameState = MainGameState.SHOW_MAIN_VIEW;
                this.changeToTaskView();
            }
        }
    }

    private void moveSaufometer() {
        this.saufometerUpdateCounter -= this.elapsedTime;
        if (this.saufometerUpdateCounter <= 0) {
            this.saufometerUpdateCounter = SAUFOMETER_ROTATE_TIME;
            this.saufOMeter.setCurrentFrame(this.saufOMeter.getCurrentFrame() + 1);
            Log.d(TAG, "endFrame: " + this.saufOMeterEndFrame + ", currFrame: " + this.saufOMeter.getCurrentFrame());
            if (this.saufOMeter.getCurrentFrame() >= this.saufOMeterEndFrame) {
                this.saufOMeterEndFrame = 1;
                if (this.currentDifficult == TaskDifficult.EASY_WIN
                        || this.currentDifficult == TaskDifficult.MEDIUM_WIN
                        || this.currentDifficult == TaskDifficult.HARD_WIN) {
                    this.gameState = MainGameState.SAUFOMETER_BLINKING;
                } else {
                    this.gameState = MainGameState.WAITING;
                }
            }
        }
    }

    private void blinkSaufometer() {
        this.saufometerBlinkingCounter -= this.elapsedTime;
        if (this.saufometerBlinkingCounter <= 0) {
            this.saufometerBlinkingCounter = SAUFOMETER_BLINK_TIME;
            this.blinkCounter++;
            switch (this.currentDifficult) {
                case EASY_WIN:
                    if (this.saufOMeter.getCurrentFrame() == 7) {
                        this.saufOMeter.setCurrentFrame(10);
                    } else {
                        this.saufOMeter.setCurrentFrame(7);
                    }
                    break;
                case MEDIUM_WIN:
                    if (this.saufOMeter.getCurrentFrame() == 8) {
                        this.saufOMeter.setCurrentFrame(11);
                    } else {
                        this.saufOMeter.setCurrentFrame(8);
                    }
                    break;
                case HARD_WIN:
                    if (this.saufOMeter.getCurrentFrame() == 9) {
                        this.saufOMeter.setCurrentFrame(12);
                    } else {
                        this.saufOMeter.setCurrentFrame(9);
                    }
                    break;
                default:
                    break;
            }
            if (this.blinkCounter > 4) {
                this.blinkCounter = 0;
                this.gameState = MainGameState.WAITING;
            }
        }
    }

    private void changeToTaskView() {
        this.thread.setRunning(false);
        final MainGameActivity currActivity = (MainGameActivity) this.getContext();
        currActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!MainGamePanel.this.currentDifficult.equals(TaskDifficult.GAME)) {
                    currActivity.changeToTaskViewWithTask(MainGamePanel.this.currentTask, MainGamePanel.this.player,
                            MainGamePanel.this.currentPlayer);
                } else {
                    currActivity.changeToTaskViewWithGame(MainGamePanel.this.currentMiniGame, MainGamePanel.this
                            .player, MainGamePanel.this.currentPlayer);
                }
            }
        });
    }

    private void getCurrentDifficult() {
        float difficult = 0;
        int gameCount = 0;
        if (this.icons[0].getState() == this.icons[1].getState()
                && this.icons[0].getState() == this.icons[2].getState()) {
            switch (this.icons[0].getState()) {
                case EASY:
                    this.saufOMeterEndFrame = 7;
                    this.currentDifficult = TaskDifficult.EASY_WIN;
                    return;
                case MEDIUM:
                    this.saufOMeterEndFrame = 8;
                    this.currentDifficult = TaskDifficult.MEDIUM_WIN;
                    return;
                case HARD:
                    this.saufOMeterEndFrame = 9;
                    this.currentDifficult = TaskDifficult.HARD_WIN;
                    return;
                default:
                    break;
            }
        }
        for (int i = 0; i < 3; i++) {
            switch (this.icons[i].getState()) {
                case EASY:
                    difficult += 0.2;
                    break;
                case MEDIUM:
                    difficult += 1.4;
                    break;
                case HARD:
                    difficult += 2.8;
                    break;
                case GAME:
                    gameCount++;
                    break;
            }
        }
        difficult = ((difficult) / (3 - gameCount));
        int tmpDiff = (int) difficult;
        if (!this.framesSet) {
            this.framesSet = true;
            if (random.nextInt(3) < gameCount) {
                this.currentDifficult = TaskDifficult.GAME;
                return;
            }
            if (difficult > 0.6) {
                this.saufOMeterEndFrame++;
            }
            if (difficult > 0.95) {
                this.saufOMeterEndFrame++;
            }
            if (difficult > 1.5) {
                this.saufOMeterEndFrame++;
            }
            if (difficult > 2) {
                this.saufOMeterEndFrame++;
            }
            if (difficult > 2.2) {
                this.saufOMeterEndFrame++;
            }
        }
        switch (tmpDiff) {
            case 0:
                this.currentDifficult = TaskDifficult.EASY;
                return;
            case 1:
                this.currentDifficult = TaskDifficult.MEDIUM;
                return;
            case 2:
                this.currentDifficult = TaskDifficult.HARD;
                return;
        }
        this.currentDifficult = TaskDifficult.UNDEFINED;
    }

    private void moveSingleIcon(SlotMachineIcon icon, float speed) {
        int chanceSum = EASY_CHANCE + MEDIUM_CHANCE + HARD_CHANCE + GAME_CHANCE;
        icon.setY(icon.getY() + (int) (speed * this.elapsedTime));
        if (icon.getY() > this.screenHeight) {
            icon.setY(0);
            int rnd = random.nextInt(chanceSum);
            if (rnd < EASY_CHANCE) {
                icon.setState(IconState.EASY);
            } else if (rnd < EASY_CHANCE + MEDIUM_CHANCE) {
                icon.setState(IconState.MEDIUM);
            } else if (rnd < EASY_CHANCE + MEDIUM_CHANCE + HARD_CHANCE) {
                icon.setState(IconState.HARD);
            } else if (rnd < chanceSum) {
                icon.setState(IconState.GAME);
            }
        }
    }

    private void moveIcons() {
        float[] speeds = {3.5f, 5f, 6.5f};
        switch (this.gameState) {
            case ROLLING_ALL:
                for (int i = 0; i < 3; i++) {
                    this.moveSingleIcon(this.icons[i], speeds[i]);
                }
                break;
            case STOP1:
                this.icons[0].setY(this.icons[0].getY() + (this.getIconStopY() - this.icons[0].getY()) / 2);
                for (int i = 1; i < 3; i++) {
                    this.moveSingleIcon(this.icons[i], speeds[i]);
                }
                break;
            case STOP2:
                this.icons[0].setY(this.icons[0].getY() + (this.getIconStopY() - this.icons[0].getY()) / 2);
                this.icons[1].setY(this.icons[1].getY() + (this.getIconStopY() - this.icons[1].getY()) / 2);
                this.moveSingleIcon(this.icons[2], speeds[2]);
                break;
            case STOP_ALL:
                boolean[] iconsOnPosition = {false, false, false};
                for (int i = 0; i < 3; i++) {
                    this.icons[i].setY(this.icons[i].getY() + (this.getIconStopY() - this.icons[i].getY()) / 2);
                    if (this.icons[i].getY() < this.getIconStopY() + this.getIconStopY() * 0.01
                            && this.icons[i].getY() > this.getIconStopY() - this.getIconStopY() * 0.01) {
                        iconsOnPosition[i] = true;
                    }
                }
                boolean positionReached = true;
                for (int i = 0; i < 3; i++)
                    if (!iconsOnPosition[i]) {
                        positionReached = false;
                    }
                if (positionReached) {
                    this.gameState = MainGameState.All_IN_POSITION;
                }
                break;
            default:
                break;
        }
    }

    private void startButtonPressed() {
        switch (this.gameState) {
            case GAME_START:
                this.gameState = MainGameState.ROLLING_ALL;
                break;
            case ROLLING_ALL:
                this.gameState = MainGameState.STOP1;
                break;
            case STOP1:
                this.gameState = MainGameState.STOP2;
                break;
            case STOP2:
                this.gameState = MainGameState.STOP_ALL;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    public void setAvgFps(String avgFps) {
        this.avgFps = avgFps;
    }

    public void render(Canvas canvas) {
        if (canvas != null) {
            canvas.drawBitmap(this.background, 0, 0, null);

            for (SlotMachineIcon icon : this.icons) {
                icon.draw(canvas);
            }
            canvas.drawBitmap(this.slotMachine, 0, 0, null);

            canvas.drawText(this.currentPlayer.getName(), canvas.getWidth() / 2, this.screenHeight / 10, this
                    .currentPlayerTextPaint);

            this.saufOMeter.draw(canvas);

            this.getButton().draw(canvas);
            if (this.avgFps != null) {
                canvas.drawText(this.avgFps, this.getWidth() - 200, 250, this.currentPlayerTextPaint);
            }
        }
    }


    public long getElapsedTime() {
        return this.elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public DrawableButton getButton() {
        return this.button;
    }

    public void setButton(DrawableButton button) {
        this.button = button;
    }
}
