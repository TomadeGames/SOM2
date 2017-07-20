package com.tomade.saufomat.activity.miniGame.biergeballer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.tomade.saufomat.R;
import com.tomade.saufomat.threading.ThreadedView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Spiellogik von Biergeballer
 * Created by woors on 15.03.2017.
 */

public class BiergeballerPanel extends SurfaceView implements ThreadedView {
    private static final int BEER_WIDTH = 40;
    private static final int BEER_HEIGHT = 80;

    private static Random random = new Random();
    private boolean tutorialShown = false;
    private boolean gameActive = true;

    private int beerSpeed = 1000;
    private int beerSpawnTimeLeft = random.nextInt(1000) + 1000;
    private int beerSpawnTimeRight = random.nextInt(1000) + 1000;

    private long elapsedTime;
    private String avgFps;

    private final int screenWith;
    private final int screenHeight;

    private Bitmap background;
    private List<Bitmap> beersFromRight;
    private List<Bitmap> beersFromLeft;
    private Bitmap leftCrate;
    private Bitmap rightCrate;
    private Bitmap[] leftHearts = new Bitmap[3];
    private Bitmap[] rightHearts = new Bitmap[3];

    private Bitmap tutorialBackground;
    private String tutorialText;
    private boolean tutorialVisible = false;
    private Paint textPaint;

    public BiergeballerPanel(Context context) {
        super(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        this.screenWith = size.x;
        this.screenHeight = size.y;

        this.initContent();
    }

    private void initContent() {
        this.background = BitmapFactory.decodeResource(this.getResources(), R.drawable.slot_machine_background);
        this.background = Bitmap.createScaledBitmap(this.background, this.screenWith, this.screenHeight, true);
        this.beersFromLeft = new ArrayList<>();
        this.beersFromRight = new ArrayList<>();
        this.leftCrate = BitmapFactory.decodeResource(this.getResources(), R.drawable.beer_crate);
        this.rightCrate = BitmapFactory.decodeResource(this.getResources(), R.drawable.beer_crate);
        for (int i = 0; i < 3; i++) {
            this.leftHearts[i] = BitmapFactory.decodeResource(this.getResources(), R.drawable.biergeballer_heart);
            this.rightHearts[i] = BitmapFactory.decodeResource(this.getResources(), R.drawable.biergeballer_heart);
        }
        this.tutorialBackground = BitmapFactory.decodeResource(this.getResources(), R.drawable.popup);
        this.tutorialText = this.getResources().getString(R.string.minigame_biergeballer_tutorial);

        this.textPaint = new Paint();
        this.textPaint.setTextSize(100);
        this.textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Canvas canvas) {
        if (canvas != null) {
            canvas.drawBitmap(this.background, 0, 0, null);
            for (int i = 0; i < 3; i++) {
                canvas.drawBitmap(this.leftHearts[i], 0, 0, null);
                canvas.drawBitmap(this.rightHearts[i], 0, 0, null);
            }
            for (Bitmap beerFromLeft : this.beersFromLeft) {
                canvas.drawBitmap(beerFromLeft, 0, 0, null);
            }
            for (Bitmap beerFromRight : this.beersFromRight) {
                canvas.drawBitmap(beerFromRight, 0, 0, null);
            }
            canvas.drawBitmap(this.leftCrate, 0, 0, null);
            canvas.drawBitmap(this.rightCrate, 0, 0, null);

            if (this.tutorialVisible) {
                canvas.drawBitmap(this.tutorialBackground, 0, 0, null);
                canvas.drawText(this.tutorialText, this.getWidth() - 200, 250, this.textPaint);
            }
        }
    }

    @Override
    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
