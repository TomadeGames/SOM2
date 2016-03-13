package de.tomade.saoufomat2.activity.mainGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.Random;

import de.tomade.saoufomat2.R;
import de.tomade.saoufomat2.model.ButtonEvent;
import de.tomade.saoufomat2.model.ButtonListener;
import de.tomade.saoufomat2.model.DrawableButton;
import de.tomade.saoufomat2.model.IconState;
import de.tomade.saoufomat2.model.SlotMachineIcon;
import de.tomade.saoufomat2.model.task.TaskFactory;

/**
 * Created by woors on 09.03.2016.
 */
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private static final int easyChance = 4;
    private static final int mediumChance = 4;
    private static final int hardChance = 3;
    private static final int gameChance = 1;
    private static final String TAG = MainGamePanel.class.getSimpleName();
    private int getIconStopY()
    {
        return (int) (screenHeight/3);
    }

    private MainGameThread thread;

    private SlotMachineIcon[] icons;
    private DrawableButton button;

    private String avgFps;
    private int elapsedTime;

    private int screenWith;
    private int screenHeight;

    private MainGameState gameState = MainGameState.GAME_START;
    private static Random random;

    private TaskFactory taskFactory;

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

    private void initContent(){
        Bitmap beerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.beer_icon);
        Bitmap cocktailIcon = BitmapFactory.decodeResource(getResources(), R.drawable.cocktail_icon);
        Bitmap shotIcon = BitmapFactory.decodeResource(getResources(), R.drawable.shot_icon);
        Bitmap startButton = BitmapFactory.decodeResource(getResources(), R.drawable.start_button);
        Bitmap gameIcon = BitmapFactory.decodeResource(getResources(), R.drawable.dice_icon);
        icons = new SlotMachineIcon[3];
        icons[0] = new SlotMachineIcon(beerIcon, cocktailIcon, shotIcon, gameIcon, (int)(screenWith/3.3 - screenWith/30) , (int)(screenHeight / 2.68), screenWith,screenHeight, IconState.EASY);
        icons[1] = new SlotMachineIcon(beerIcon, cocktailIcon, shotIcon, gameIcon, (int)(screenWith/2 - screenWith/30), (int)(screenHeight / 2.68), screenWith, screenHeight, IconState.EASY);
        icons[2] = new SlotMachineIcon(beerIcon, cocktailIcon, shotIcon, gameIcon, (int)(screenWith/2.9*2 - screenWith/30), (int)(screenHeight / 2.68), screenWith, screenHeight, IconState.EASY);

        button = new DrawableButton(startButton, (int)(screenWith / 1.35), (int)(screenHeight / 1.4), screenWith / 5, screenHeight / 5);
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
        moveIcons();
    }

    private void moveSingleIcon(SlotMachineIcon icon, float speed) {
        int chanceSum = easyChance + mediumChance + hardChance + gameChance;
        icon.setY(icon.getY() + (int) (speed * getElapsedTime()));
        if(icon.getY() > screenHeight){
            icon.setY(0);
            int rnd = this.random.nextInt(chanceSum);
            if(rnd < easyChance) {
                icon.setState(IconState.EASY);
            }
            else if(rnd < easyChance + mediumChance) {
                icon.setState(IconState.MEDIUM);
            }
            else if (rnd < easyChance + mediumChance + hardChance) {
                icon.setState(IconState.HARD);
            }
            else if(rnd < chanceSum) {
                icon.setState(IconState.GAME);
            }
        }
    }

    private void moveIcons(){
        float[] speeds = {1f,1.5f,2f};
        switch (gameState){
            case ROLLING_ALL:
                for(int i = 0; i < 3; i++) {
                   moveSingleIcon(icons[i],speeds[i]);
                }
                break;
            case STOP1:
                icons[0].setY(icons[0].getY() + getIconStopY() - icons[0].getY() / 2);
                for(int i = 1; i < 3; i++) {
                    moveSingleIcon(icons[i], speeds[i]);
                }
                break;
            case STOP2:
                icons[0].setY(icons[0].getY() + getIconStopY() - icons[0].getY() / 2);
                icons[1].setY(icons[1].getY() + getIconStopY() - icons[1].getY() / 2);
                moveSingleIcon(icons[2],speeds[2]);
                break;
            case STOP_ALL:
                for(int i = 1; i < 3; i++) {
                    icons[i].setY(icons[i].getY() + getIconStopY() - icons[i].getY() / 2);;
                }
                break;
        }
    }

    private void startButtonPressed() {
        switch (gameState){
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
            case STOP_ALL:
                gameState = MainGameState.GAME_START;
                break;
            default:
                System.out.println("ERROR: Illegal MainGameState!!!");
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.slot_machine_background), 0, 0, null);
    }

    public void setAvgFps(String avgFps) {
        this.avgFps = avgFps;
    }

    public void render(Canvas canvas) {
        if(canvas != null) {
            canvas.drawColor(Color.BLACK);
            Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.slot_machine_background);
            background = Bitmap.createScaledBitmap(background, canvas.getWidth(), canvas.getHeight(), true);
            canvas.drawBitmap(background, 0, 0, null);

            Bitmap slotMachine = BitmapFactory.decodeResource(getResources(), R.drawable.slot_machine);
            slotMachine = Bitmap.createScaledBitmap(slotMachine, canvas.getWidth(), canvas.getHeight(), true);
            canvas.drawBitmap(slotMachine, 0, 0, null);

            for (SlotMachineIcon icon : icons) {
                icon.draw(canvas);
            }

            button.draw(canvas);
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
