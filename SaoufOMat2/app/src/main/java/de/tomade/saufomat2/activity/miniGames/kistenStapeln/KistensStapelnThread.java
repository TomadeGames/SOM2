package de.tomade.saufomat2.activity.miniGames.kistenStapeln;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import de.tomade.saufomat2.activity.mainGame.MainGamePanel;

/**
 * Created by woors on 12.04.2016.
 */
public class KistensStapelnThread extends Thread{
    private final float FALLING_VELOCITY = 0.0001f;
    private float fallingSpeed = 0;
    private ImageView view;
    private float targetPosition;
    private KistenStapelnActivity source;
    private long timeThisFrame;

    private boolean running = false;

    public KistensStapelnThread(ImageView view, float targetPosition, KistenStapelnActivity source) {
        this.view = view;
        this.targetPosition = targetPosition;
        this.source = source;
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
            long startFrameTime = System.currentTimeMillis();
            if(view.getY() < targetPosition){
                fallingSpeed += FALLING_VELOCITY * timeThisFrame;
                this.view.setY(view.getY() + fallingSpeed);
            }
            else{
                this.source.onCrateLanded();
                this.running = false;
            }
            this.timeThisFrame = System.currentTimeMillis() - startFrameTime;
        }
    }
}
