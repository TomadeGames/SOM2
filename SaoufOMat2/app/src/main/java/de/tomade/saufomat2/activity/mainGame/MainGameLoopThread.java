package de.tomade.saufomat2.activity.mainGame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by woors on 30.03.2016.
 */
public class MainGameLoopThread extends Thread {
    private MainGamePanel view;
    private boolean running = false;
    private long timeThisFrame;
    private long fps;

    private SurfaceHolder surfaceHolder;


    public MainGameLoopThread(SurfaceHolder surfaceHolder, MainGamePanel view) {
        this.view = view;
        this.surfaceHolder = surfaceHolder;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    public boolean getRunning(){
        return running;
    }

    @Override
    public void run() {
        while (running) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            view.update();

            // Draw the frame
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    view.render(canvas);
                }
            }
            finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            this.timeThisFrame = System.currentTimeMillis() - startFrameTime;
            this.view.setElapsedTime(this.timeThisFrame);
            if (timeThisFrame > 0) {
                fps = 1000 / timeThisFrame;
            }

            this.view.setAvgFps("fps: " + fps);
        }
    }
}
