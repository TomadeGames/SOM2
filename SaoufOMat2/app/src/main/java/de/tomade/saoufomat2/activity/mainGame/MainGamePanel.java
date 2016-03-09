package de.tomade.saoufomat2.activity.mainGame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import de.tomade.saoufomat2.R;

/**
 * Created by woors on 09.03.2016.
 */
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainGameThread thread;
    private static final String TAG = MainGamePanel.class.getSimpleName();

    private Icon[] icons;

    private String avgFps;

    public MainGamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Bitmap beerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.beer_icon);
        beerIcon = Bitmap.createScaledBitmap(beerIcon, width / 10,(int)(height / 2.6), true);

        Bitmap cocktailIcon = BitmapFactory.decodeResource(getResources(), R.drawable.cocktail_icon);
        cocktailIcon = Bitmap.createScaledBitmap(cocktailIcon, width / 8, height / 3, true);

        Bitmap shotIcon = BitmapFactory.decodeResource(getResources(), R.drawable.shot_icon);
        shotIcon = Bitmap.createScaledBitmap(shotIcon, width / 11, height / 5, true);
        icons = new Icon[3];
        icons[0] = new Icon(beerIcon, (int)(width/3.3 - width/30) , (int)(height / 2.68));
        icons[1] = new Icon(cocktailIcon, (int)(width/2 - width/30), (int)(height / 2.68));
        icons[2] = new Icon(shotIcon, (int)(width/2.9*2 - width/30), (int)(height / 2.68));

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
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getY() > getHeight() - 50) {
                thread.setRunning(false);
                ((Activity) getContext()).finish();
            } else {
                Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
            }
        }
        return super.onTouchEvent(event);

    }

    public void update() {

    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.slot_machine_background), 0, 0, null);
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


}
