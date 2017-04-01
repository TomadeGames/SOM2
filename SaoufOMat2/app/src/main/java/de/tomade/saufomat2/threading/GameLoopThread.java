package de.tomade.saufomat2.threading;

import android.graphics.Canvas;
import android.os.AsyncTask;
import android.view.SurfaceHolder;

/**
 * Thread für die GameLoop
 * Created by woors on 30.03.2016.
 */
public class GameLoopThread extends AsyncTask<Void, Void, Void> {
    private ThreadedView view;
    private boolean running = false;
    private long fps;

    private final SurfaceHolder surfaceHolder;


    public GameLoopThread(SurfaceHolder surfaceHolder, ThreadedView view) {
        this.view = view;
        this.surfaceHolder = surfaceHolder;
    }

    public void setRunning(boolean run) {
        this.running = run;
    }

    public boolean getRunning() {
        return this.running;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (this.running) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            this.view.update();

            // Draw the frame
            Canvas canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (this.surfaceHolder) {
                    this.view.render(canvas);
                }
            } finally {
                if (canvas != null) {
                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            this.view.setElapsedTime(timeThisFrame);
            if (timeThisFrame > 0) {
                this.fps = 1000 / timeThisFrame;
            }

            this.view.setAvgFps("fps: " + this.fps);
        }
        return null;
    }
}
