package de.tomade.saufomat2.activity.miniGames.bierrutsche;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.ChooseMiniGameActivity;
import de.tomade.saufomat2.activity.mainGame.MainGameActivity;
import de.tomade.saufomat2.model.MiniGame;
import de.tomade.saufomat2.model.drawable.DynamicImageView;

public class Bierrutsche extends Activity implements View.OnClickListener {
    private static final String TAG = Bierrutsche.class.getSimpleName();
    private final int TARGET_ACCURACY = 50000;
    private final int ANIMATION_DURATION = 1500;
    private final int FALLING_DELAY = 1500;

    private DynamicImageView backgroundImage;
    private ImageView startField;
    private ImageView targetImage;
    private ImageView beerImage;

    private RelativeLayout tutorialPanel;

    private ImageButton backButton;
    private ImageButton tutorialButton;

    private boolean fromMenue = false;

    //ScreenSize
    private int screenWidth;
    private int screenHeight;

    private float downPositionX;

    private ObjectAnimator fallingGlassX;
    private ObjectAnimator fallingGlassY;
    private ObjectAnimator turningGlass;
    private ObjectAnimator targetX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bierrutsche);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.fromMenue = extras.getBoolean("fromMenue");
        }

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        backgroundImage = (DynamicImageView) this.findViewById(R.id.backgroundImage);
        backgroundImage.setFullX(true);
        startField = (ImageView) this.findViewById(R.id.startImage);
        targetImage = (ImageView) this.findViewById(R.id.targetImage);
        beerImage = (ImageView) this.findViewById(R.id.beerImage);
        tutorialPanel = (RelativeLayout) this.findViewById(R.id.tutorialPanel);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) backgroundImage.getLayoutParams();
        backgroundImage.setLayoutParams(params);

        this.backButton = (ImageButton) this.findViewById(R.id.backButton);
        this.tutorialButton = (ImageButton) this.findViewById(R.id.tutorialButton);

        this.backButton.setOnClickListener(this);
        this.tutorialButton.setOnClickListener(this);

        if (!this.fromMenue) {
            this.backButton.setVisibility(View.GONE);
        }
    }


    private void startAnimation(int accuracy) {
        int maximumLength = backgroundImage.getWidth() - screenWidth;
        int scrollWidth;
        boolean toFar = false;
        if (accuracy > 100) {
            scrollWidth = maximumLength;
            toFar = true;
        } else {
            scrollWidth = maximumLength * accuracy / 100;
        }

        ObjectAnimator backgroundX = ObjectAnimator.ofFloat(backgroundImage, View.TRANSLATION_X, 0, -scrollWidth);
        backgroundX.setDuration(ANIMATION_DURATION);

        ObjectAnimator startFieldX = ObjectAnimator.ofFloat(startField, View.TRANSLATION_X, 0, -scrollWidth);
        startFieldX.setDuration(ANIMATION_DURATION);

        targetX = ObjectAnimator.ofFloat(targetImage, View.TRANSLATION_X, 0, -screenWidth / 2);
        targetX.setDuration(ANIMATION_DURATION);
        targetX.setStartDelay(ANIMATION_DURATION - ANIMATION_DURATION / 10);

        if (toFar) {
            this.fallingGlassX = ObjectAnimator.ofFloat(beerImage, View.TRANSLATION_X, 0, screenWidth);
            this.fallingGlassX.setDuration((long) (ANIMATION_DURATION * 2.5f));

            fallingGlassY = ObjectAnimator.ofFloat(beerImage, View.TRANSLATION_Y, 0, 600);
            fallingGlassY.setDuration(ANIMATION_DURATION / 2);
            fallingGlassY.setStartDelay(FALLING_DELAY);

            turningGlass = ObjectAnimator.ofFloat(beerImage, View.ROTATION, 100);
            turningGlass.setDuration(ANIMATION_DURATION / 2);
            turningGlass.setStartDelay(FALLING_DELAY);

            backgroundX.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    fallingGlassX.start();
                    turningGlass.start();
                    fallingGlassY.start();
                    targetX.start();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        backgroundX.start();
        startFieldX.start();
    }

    private void endRound() {

    }

    private void startNextRound() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                this.downPositionX = event.getX();
                if (this.downPositionX > startField.getWidth()) {
                    this.downPositionX = -1;
                }
                Log.d(TAG, "Down: " + this.downPositionX);
                break;
            case MotionEvent.ACTION_UP:
                if (tutorialPanel.getVisibility() == View.VISIBLE) {
                    tutorialPanel.setVisibility(View.GONE);
                } else {
                    if (this.downPositionX != -1) {
                        float x = event.getX();
                        if (x > startField.getWidth()) {
                            x = startField.getWidth();
                        }
                        float deltaX = x - this.downPositionX;
                        if (deltaX > 0) {
                            long eventDuration = event.getEventTime() - event.getDownTime();

                            long accuracy = (long) (deltaX * eventDuration);
                            float percent = 100 / (float) TARGET_ACCURACY;
                            percent = percent * accuracy;
                            Log.d(TAG, "Up: " + x + " deltaX: " + deltaX + " duration: " + eventDuration + " accuracy: " + accuracy + " percent: " + percent);
                            startAnimation((int) percent);
                        }
                    }
                    break;
                }
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        if (tutorialPanel.getVisibility() == View.VISIBLE) {
            tutorialPanel.setVisibility(View.GONE);
        } else {
            switch (v.getId()) {
                case R.id.backButton:
                    if (fromMenue) {
                        Intent intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
                        intent.putExtra("lastGame", MiniGame.BIERRUTSCHE);
                        this.startActivity(intent);
                    } else {
                        Intent intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
                        this.startActivity(intent);
                    }
                    break;
                case R.id.tutorialButton:
                    if (tutorialPanel.getVisibility() == View.GONE) {
                        tutorialPanel.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }
}
