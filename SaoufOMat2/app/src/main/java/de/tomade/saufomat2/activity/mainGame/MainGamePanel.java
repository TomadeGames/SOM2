package de.tomade.saufomat2.activity.mainGame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.model.IconState;
import de.tomade.saufomat2.model.MainGameState;
import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.model.drawable.SaufOMeter;
import de.tomade.saufomat2.model.drawable.SlotMachineIcon;
import de.tomade.saufomat2.model.drawable.button.ButtonEvent;
import de.tomade.saufomat2.model.drawable.button.ButtonListener;
import de.tomade.saufomat2.model.drawable.button.DrawableButton;
import de.tomade.saufomat2.model.task.Task;
import de.tomade.saufomat2.model.task.TaskDifficult;
import de.tomade.saufomat2.model.task.TaskFactory;

/**
 * Created by woors on 09.03.2016.
 */
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private static final int easyChance = 4;
    private static final int mediumChance = 4;
    private static final int hardChance = 3;
    private static final int gameChance = 1;

    private static final int saufometerBlinkTime = 600;
    private static final int saufometerRotateTime = 300;
    private static final int mainViewWaitTime = 7500;
    private static final String TAG = MainGamePanel.class.getSimpleName();
    private static Random random;

    //Thread
    private MainGameThread thread;
    private String avgFps;
    private int elapsedTime;

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

    //Task
    private TaskFactory taskFactory;
    private Task currentTask;

    //SaufOMeter
    private int saufOMeterEndFrame = 0;
    private float saufometerUpdateCounter = saufometerRotateTime;
    private float waitCounter = mainViewWaitTime;
    private float saufometerBlinkingCounter = saufometerBlinkTime;
    private int blinkCounter = 0;

    public MainGamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.random = new Random();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        screenWith = size.x;
        screenHeight = size.y;

        initContent();
        taskFactory = new TaskFactory();


        thread = new MainGameThread(getHolder(), this);
        setFocusable(true);
    }

    public MainGamePanel(Context context, Player currentPlayer, ArrayList<Player> players){
        this(context);
        this.currentPlayer = currentPlayer;
        this.player = players;
    }

    private int getIconStopY() {
        return (int) (screenHeight / 2.8);
    }

    private void initContent() {
        Bitmap beerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.beer_icon);
        Bitmap cocktailIcon = BitmapFactory.decodeResource(getResources(), R.drawable.cocktail_icon);
        Bitmap shotIcon = BitmapFactory.decodeResource(getResources(), R.drawable.shot_icon);
        Bitmap startButton = BitmapFactory.decodeResource(getResources(), R.drawable.start_button);
        Bitmap gameIcon = BitmapFactory.decodeResource(getResources(), R.drawable.dice_icon);
        icons = new SlotMachineIcon[3];
        icons[0] = new SlotMachineIcon(beerIcon, cocktailIcon, shotIcon, gameIcon, (int) (screenWith / 3.3 - screenWith / 30), (int) (screenHeight / 2.68), screenWith, screenHeight, IconState.EASY);
        icons[1] = new SlotMachineIcon(beerIcon, cocktailIcon, shotIcon, gameIcon, (int) (screenWith / 2 - screenWith / 30), (int) (screenHeight / 2.68), screenWith, screenHeight, IconState.EASY);
        icons[2] = new SlotMachineIcon(beerIcon, cocktailIcon, shotIcon, gameIcon, (int) (screenWith / 2.9 * 2 - screenWith / 30), (int) (screenHeight / 2.68), screenWith, screenHeight, IconState.EASY);

        Bitmap[] saufOMeterFrames = new Bitmap[13];
        saufOMeterFrames[0] = BitmapFactory.decodeResource(getResources(), R.drawable.saufometer0);
        saufOMeterFrames[1] = BitmapFactory.decodeResource(getResources(), R.drawable.saufometer1);
        saufOMeterFrames[2] = BitmapFactory.decodeResource(getResources(), R.drawable.saufometer2);
        saufOMeterFrames[3] = BitmapFactory.decodeResource(getResources(), R.drawable.saufometer3);
        saufOMeterFrames[4] = BitmapFactory.decodeResource(getResources(), R.drawable.saufometer4);
        saufOMeterFrames[5] = BitmapFactory.decodeResource(getResources(), R.drawable.saufometer5);
        saufOMeterFrames[6] = BitmapFactory.decodeResource(getResources(), R.drawable.saufometer6);
        saufOMeterFrames[7] = BitmapFactory.decodeResource(getResources(), R.drawable.saufometer7);
        saufOMeterFrames[8] = BitmapFactory.decodeResource(getResources(), R.drawable.saufometer8);
        saufOMeterFrames[9] = BitmapFactory.decodeResource(getResources(), R.drawable.saufometer9);
        saufOMeterFrames[10] = BitmapFactory.decodeResource(getResources(), R.drawable.saufometer10);
        saufOMeterFrames[11] = BitmapFactory.decodeResource(getResources(), R.drawable.saufometer11);
        saufOMeterFrames[12] = BitmapFactory.decodeResource(getResources(), R.drawable.saufometer12);
        this.saufOMeter = new SaufOMeter(saufOMeterFrames, (int) (this.screenWith - this.screenWith / 1.19), (int) (this.screenHeight / 1.15), this.screenHeight / 4, this.screenHeight / 4);

        button = new DrawableButton(startButton, (int) (screenWith / 1.35), (int) (screenHeight / 1.4), screenWith / 5, screenHeight / 5);
        button.addListener(new ButtonListener() {
            @Override
            public void onInput(ButtonEvent event) {
                startButtonPressed();
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                button.checkClick(event.getX(), event.getY());
                break;
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        if (this.gameState == MainGameState.ROLLING_ALL
                || this.gameState == MainGameState.STOP1
                || this.gameState == MainGameState.STOP2
                || this.gameState == MainGameState.STOP_ALL) {
            moveIcons();
        } else if (this.gameState == MainGameState.All_IN_POSITION) {
            TaskDifficult difficult = getCurrentDifficult();
            if (difficult != TaskDifficult.GAME) {
                currentTask = taskFactory.getTask(difficult);
                this.gameState = MainGameState.MOVE_SAUFOMETER;
            } else {
                //TODO:Game starten
            }
        } else if (this.gameState == MainGameState.MOVE_SAUFOMETER) {
            this.saufometerUpdateCounter -= this.elapsedTime;
            if (saufometerUpdateCounter <= 0) {
                saufometerUpdateCounter = saufometerRotateTime;
                this.saufOMeter.setCurrentFrame(this.saufOMeter.getCurrentFrame() + 1);
                if (this.saufOMeter.getCurrentFrame() == this.saufOMeterEndFrame) {
                    this.saufOMeterEndFrame = 1;
                    TaskDifficult diff = getCurrentDifficult();
                    if (diff == TaskDifficult.EASY_WIN
                            || diff == TaskDifficult.MEDIUM_WIN
                            || diff == TaskDifficult.HARD_WIN) {
                        this.gameState = MainGameState.SAUFOMETER_BLINKING;
                    } else {
                        this.gameState = MainGameState.WAITING;
                    }
                }
            }
        } else if (this.gameState == MainGameState.SAUFOMETER_BLINKING) {
            saufometerBlinkingCounter -= this.elapsedTime;
            if (this.saufometerBlinkingCounter <= 0) {
                saufometerBlinkingCounter = saufometerBlinkTime;
                blinkCounter++;
                switch (getCurrentDifficult()) {
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
                }
                if (blinkCounter > 4) {
                    blinkCounter = 0;
                    this.gameState = MainGameState.WAITING;
                }
            }
        } else if (this.gameState == MainGameState.WAITING) {
            this.waitCounter -= this.elapsedTime;
            if (this.waitCounter <= 0) {
                this.waitCounter = this.mainViewWaitTime;
                this.gameState = MainGameState.SHOW_MAIN_VIEW;
                changeToTaskView();
            }
        }
    }

    private void changeToTaskView(){
        Intent intent = new Intent(this.getContext().getApplicationContext(), TaskViewActivity.class);
        intent.putExtra("task", this.currentTask);
        intent.putParcelableArrayListExtra("player", this.player);
        intent.putExtra("currentPlayer", this.currentPlayer);
        this.getContext().startActivity(intent);
    }

    @Nullable
    private TaskDifficult getCurrentDifficult() {
        float difficult = 0;
        int gameCount = 0;
        if (icons[0].getState() == icons[1].getState()
                && icons[0].getState() == icons[2].getState()) {
            switch (icons[0].getState()) {
                case EASY:
                    return TaskDifficult.EASY_WIN;
                case MEDIUM:
                    return TaskDifficult.MEDIUM_WIN;
                case HARD:
                    return TaskDifficult.HARD_WIN;
            }
        }
        for (int i = 0; i < 3; i++) {
            switch (icons[i].getState()) {
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
        if (this.random.nextInt(3) < gameCount) {
            return TaskDifficult.GAME;
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

        switch (tmpDiff) {
            case 0:
                return TaskDifficult.EASY;
            case 1:
                return TaskDifficult.MEDIUM;
            case 2:
                return TaskDifficult.HARD;
        }
        return null;
    }

    private void moveSingleIcon(SlotMachineIcon icon, float speed) {
        int chanceSum = easyChance + mediumChance + hardChance + gameChance;
        icon.setY(icon.getY() + (int) (speed * getElapsedTime()));
        if (icon.getY() > screenHeight) {
            icon.setY(0);
            int rnd = this.random.nextInt(chanceSum);
            if (rnd < easyChance) {
                icon.setState(IconState.EASY);
            } else if (rnd < easyChance + mediumChance) {
                icon.setState(IconState.MEDIUM);
            } else if (rnd < easyChance + mediumChance + hardChance) {
                icon.setState(IconState.HARD);
            } else if (rnd < chanceSum) {
                icon.setState(IconState.GAME);
            }
        }
    }

    private void moveIcons() {
        float[] speeds = {1f, 1.5f, 2f};
        switch (gameState) {
            case ROLLING_ALL:
                for (int i = 0; i < 3; i++) {
                    moveSingleIcon(icons[i], speeds[i]);
                }
                break;
            case STOP1:
                icons[0].setY(icons[0].getY() + (getIconStopY() - icons[0].getY()) / 2);
                for (int i = 1; i < 3; i++) {
                    moveSingleIcon(icons[i], speeds[i]);
                }
                break;
            case STOP2:
                icons[0].setY(icons[0].getY() + (getIconStopY() - icons[0].getY()) / 2);
                icons[1].setY(icons[1].getY() + (getIconStopY() - icons[1].getY()) / 2);
                moveSingleIcon(icons[2], speeds[2]);
                break;
            case STOP_ALL:
                boolean[] iconsOnPosition = {false, false, false};
                for (int i = 0; i < 3; i++) {
                    icons[i].setY(icons[i].getY() + (getIconStopY() - icons[i].getY()) / 2);
                    if (icons[i].getY() < getIconStopY() + getIconStopY() * 0.1
                            && icons[i].getY() > getIconStopY() - getIconStopY() * 0.1) {
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
        }
    }

    private void startButtonPressed() {
        switch (gameState) {
            case GAME_START:
                gameState = MainGameState.ROLLING_ALL;
                break;
            case ROLLING_ALL:
                gameState = MainGameState.STOP1;
                break;
            case STOP1:
                gameState = MainGameState.STOP2;
                break;
            case STOP2:
                gameState = MainGameState.STOP_ALL;
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
            Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.slot_machine_background);
            background = Bitmap.createScaledBitmap(background, canvas.getWidth(), canvas.getHeight(), true);
            canvas.drawBitmap(background, 0, 0, null);

            for (SlotMachineIcon icon : this.icons) {
                icon.draw(canvas);
            }
            Bitmap slotMachine = BitmapFactory.decodeResource(getResources(), R.drawable.slot_machine);
            slotMachine = Bitmap.createScaledBitmap(slotMachine, canvas.getWidth(), canvas.getHeight(), true);
            canvas.drawBitmap(slotMachine, 0, 0, null);

            this.saufOMeter.draw(canvas);

            this.button.draw(canvas);
            // display fps
            displayFps(canvas, avgFps);
        }
    }

    private void displayFps(Canvas canvas, String fps) {
        if (canvas != null && fps != null) {
            Paint paint = new Paint();
            paint.setARGB(255, 255, 255, 255);
            canvas.drawText(fps, this.getWidth() - 50, 20, paint);
        }
    }


    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
