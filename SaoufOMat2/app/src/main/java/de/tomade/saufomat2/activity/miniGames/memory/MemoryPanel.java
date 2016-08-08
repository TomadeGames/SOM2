package de.tomade.saufomat2.activity.miniGames.memory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.model.drawable.DrawableImage;
import de.tomade.saufomat2.threading.GameLoopThread;
import de.tomade.saufomat2.threading.ThreadedView;

/**
 * Created by woors on 30.05.2016.
 */
public class MemoryPanel extends SurfaceView implements SurfaceHolder.Callback, ThreadedView, MemoryAnimationListener {
    public static final String TAG = MemoryPanel.class.getSimpleName();
    private MemoryField field;
    //Screen
    private int screenWith;
    private int screenHeight;

    //Thread
    private GameLoopThread thread;
    private String avgFps;
    private long elapsedTime;

    Map<String, DrawableImage> drawObjects = new HashMap<>();

    public MemoryPanel(Context context) {
        super(context);
        getHolder().addCallback(this);WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        this.screenWith = size.x;
        this.screenHeight = size.y;

        this.initContent();
        thread = new GameLoopThread(getHolder(), this);
        setFocusable(true);
    }

    private void initContent(){
        Bitmap background = BitmapFactory.decodeResource(this.getResources(), R.drawable.biergeballer_background);
        DrawableImage backgroundImage = new DrawableImage(background, 0, 0, screenWith, screenHeight);
        this.drawObjects.put("background", backgroundImage);

        Bitmap beer = BitmapFactory.decodeResource(this.getResources(), R.drawable.beer_icon);
        Bitmap cocktail = BitmapFactory.decodeResource(this.getResources(), R.drawable.cocktail_icon);
        Bitmap shot = BitmapFactory.decodeResource(this.getResources(), R.drawable.shot_icon);
        Bitmap game = BitmapFactory.decodeResource(this.getResources(), R.drawable.dice_icon);
        Bitmap crate = BitmapFactory.decodeResource(this.getResources(), R.drawable.beer_crate);
        Bitmap backside = BitmapFactory.decodeResource(this.getResources(), R.drawable.busfahren_higher_button);

        this.field = new MemoryField(this, beer, crate, shot, cocktail, game, backside, screenWith, screenHeight);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    float lastX;
    float lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.lastX = event.getX();
                this.lastY = event.getY();
                Log.d(TAG, "Down: " + lastX + "," + lastY);
                break;
            case MotionEvent.ACTION_MOVE:

                field.moveField(-(lastX - event.getX()), -(lastY - event.getY()));
                this.lastX = event.getX();
                this.lastY = event.getY();
                Log.d(TAG, "Move: " + event.getX() + "," +  event.getY());
                break;

        }
        return true;
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
    public void update() {

    }

    @Override
    public void render(Canvas canvas) {
        if (canvas != null) {
            for(DrawableImage b : drawObjects.values()){
                canvas.drawBitmap(b.getBitmap(), b.getX(), b.getY(), null);
            }

            field.draw(canvas);
        }
    }

    @Override
    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    @Override
    public void setAvgFps(String avgFps) {
        this.avgFps = avgFps;
    }

    @Override
    public void upAnimationFinished() {

    }

    @Override
    public void downAnimationFinished() {

    }
}
