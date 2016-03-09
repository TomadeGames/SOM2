package de.tomade.saoufomat2.activity.mainGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import de.tomade.saoufomat2.R;
import de.tomade.saoufomat2.activity.model.ButtonEvent;
import de.tomade.saoufomat2.activity.model.ButtonListener;
import de.tomade.saoufomat2.activity.model.DrawableButton;
import de.tomade.saoufomat2.activity.model.Icon;

/**
 * Created by woors on 09.03.2016.
 */
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainGameThread thread;
    private static final String TAG = MainGamePanel.class.getSimpleName();

    private Icon[] icons;
    private DrawableButton button;

    private String avgFps;
    private int elapsedTime;

    private int screenWith;
    private int screenHeight;

    private MainGameState gameState = MainGameState.GAME_START;

    public MainGamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWith = size.x;
        screenHeight = size.y;

        Bitmap beerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.beer_icon);
        Bitmap cocktailIcon = BitmapFactory.decodeResource(getResources(), R.drawable.cocktail_icon);
        Bitmap shotIcon = BitmapFactory.decodeResource(getResources(), R.drawable.shot_icon);
        Bitmap startButton = BitmapFactory.decodeResource(getResources(), R.drawable.start_button);

        icons = new Icon[3];
        icons[0] = new Icon(beerIcon, (int)(screenWith/3.3 - screenWith/30) , (int)(screenHeight / 2.68), screenWith / 10,(int)(screenHeight / 2.6));
        icons[1] = new Icon(cocktailIcon, (int)(screenWith/2 - screenWith/30), (int)(screenHeight / 2.68), screenWith / 8, screenHeight / 3);
        icons[2] = new Icon(shotIcon, (int)(screenWith/2.9*2 - screenWith/30), (int)(screenHeight / 2.68), screenWith / 11, screenHeight / 5);

        button = new DrawableButton(startButton, (int)(screenWith / 1.35), (int)(screenHeight / 1.4), screenWith / 5, screenHeight / 5);
        button.addListener(new ButtonListener() {
            @Override
            public void onInput(ButtonEvent event) {
                startButtonPressed();
            }
        });
        thread = new MainGameThread(getHolder(), this);
        setFocusable(true);
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
        switch (gameState){
            case GAME_START:
                break;
            case ROLLING_ALL:
                moveIcon(icons[0]);
                moveIcon(icons[1]);
                moveIcon(icons[2]);
                break;
            case STOP1:
                moveIcon(icons[1]);
                moveIcon(icons[2]);
                break;
            case STOP2:
                moveIcon(icons[2]);
                break;
            case STOP_ALL:
                break;
            default:
                System.out.println("ERROR: Illegal MainGameState!!!");
                break;
        }
    }

    private void moveIcon(Icon icon){
        icon.setY(icon.getY() + (int)(0.1* getElapsedTime()));
        if(icon.getY() > screenHeight){
            icon.setY(0);
        }
        System.out.println("Gamestate: " + gameState + "iconY: " + icon.getY());
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
        canvas.drawColor(Color.BLACK);
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.slot_machine_background);
        background = Bitmap.createScaledBitmap(background, canvas.getWidth(), canvas.getHeight(), true);
        canvas.drawBitmap(background, 0, 0, null);

        Bitmap slotMachine = BitmapFactory.decodeResource(getResources(), R.drawable.slot_machine);
        slotMachine = Bitmap.createScaledBitmap(slotMachine, canvas.getWidth(), canvas.getHeight(), true);
        canvas.drawBitmap(slotMachine, 0, 0, null);

        for (Icon icon : icons) {
            icon.draw(canvas);
        }
        button.draw(canvas);
        // display fps
        displayFps(canvas, avgFps);
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
