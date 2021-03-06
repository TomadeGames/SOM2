package com.tomade.saufomat.activity.mainManue;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.ChooseMiniGameActivity;
import com.tomade.saufomat.activity.CreatePlayerActivity;
import com.tomade.saufomat.activity.SoundProvider;
import com.tomade.saufomat.activity.mainGame.MainGameActivity;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.model.player.Player;
import com.tomade.saufomat.persistance.SaveGameHelper;
import com.tomade.saufomat.persistance.sql.DatabaseHelper;

import java.util.ArrayList;

public class MainMenuActivity extends Activity implements View.OnClickListener, View.OnTouchListener {
    private static final String TAG = MainMenuActivity.class.getSimpleName();
    private RelativeLayout loadGameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main_menu);

        ImageButton newGameButton = this.findViewById(R.id.newGameButton);
        final ImageButton loadGameButton = this.findViewById(R.id.loadGameButton);
        newGameButton.setOnClickListener(this);
        loadGameButton.setOnClickListener(this);
        this.loadGameField = this.findViewById(R.id.loadGameField);

        TextView versionTextView = this.findViewById(R.id.versionTextView);
        String versionName;
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "versionNotFound";
            Log.e(TAG, "Could not find the VersionName", e);
        }
        versionTextView.setText(versionName);
        final TextView tipText = this.findViewById(R.id.tipText);
        tipText.setText(TipLoader.getRandomTip(this));
        final float textSizeChange = 1;
        final long duration = 600;
        /*tipText.post(new Runnable() {
            @Override
            public void run() {
                float textSizeBefore = tipText.getTextSize();
                final ValueAnimator animator = ValueAnimator.ofFloat(textSizeBefore, textSizeBefore + textSizeChange);
                animator.setDuration(duration);
                animator.setInterpolator(new LinearInterpolator());

                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float animatedValue = (float) valueAnimator.getAnimatedValue();
                        tipText.setTextSize(animatedValue);
                    }
                });

                final ValueAnimator smallerAnimator = ValueAnimator.ofFloat(textSizeBefore + textSizeChange,
                        textSizeBefore);
                smallerAnimator.setDuration(duration);
                smallerAnimator.setInterpolator(new LinearInterpolator());
                smallerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float animatedValue = (float) valueAnimator.getAnimatedValue();
                        tipText.setTextSize(animatedValue);
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        smallerAnimator.start();

                    }
                });
                smallerAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animator.start();
                    }
                });
                animator.start();
            }
        });*/

        this.findViewById(R.id.startButton).setOnTouchListener(this);
        this.findViewById(R.id.gamesButton).setOnTouchListener(this);
        this.findViewById(R.id.debugButton).setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.loadGameField.setVisibility(View.GONE);
        return super.onTouchEvent(event);
    }

    private void loadGame() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SaveGameHelper saveGameHelper = new SaveGameHelper(this);

        ArrayList<Player> allPlayer = databaseHelper.getAllPlayer();
        Player currentPlayer = saveGameHelper.getCurrentPlayer();
        int adCounter = saveGameHelper.getAdCounter();

        Intent intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
        intent.putExtra(IntentParameter.PLAYER_LIST, allPlayer);
        intent.putExtra(IntentParameter.MainGame.NEW_GAME, false);
        intent.putExtra(IntentParameter.CURRENT_PLAYER, currentPlayer);
        intent.putExtra(IntentParameter.MainGame.AD_COUNTER, adCounter);
        this.startActivity(intent);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (this.loadGameField.getVisibility() == View.VISIBLE) {
            this.loadGameField.setVisibility(View.GONE);
        } else {
            if (this.doubleBackToExitPressedOnce) {
                this.finishAffinity();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.toast_tap_back_twice_to_close, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainMenuActivity.this.doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    private void startNewGame() {
        Intent intent = new Intent(this, CreatePlayerActivity.class);
        intent.putExtra(IntentParameter.MainGame.NEW_GAME, true);
        this.startActivity(intent);
    }

    boolean soundPlaying = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newGameButton:
                this.startNewGame();
                break;
            case R.id.loadGameButton:
                this.loadGame();
                break;
            case R.id.debugButton:
                if (!this.soundPlaying) {
                    SoundProvider soundProvider = SoundProvider.getInstance();
                    soundProvider.playSoundloop(R.raw.loop, this);
                    this.soundPlaying = true;
                } else {
                    SoundProvider soundProvider = SoundProvider.getInstance();
                    soundProvider.stopSound(R.raw.loop);
                    this.soundPlaying = false;
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.equals(this.findViewById(R.id.gamesButton))) {
            if (this.loadGameField.getVisibility() == View.GONE) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton button = (ImageButton) view;
                        button.setImageResource(R.drawable.minigames_button_pressed);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
                        this.startActivity(intent);
                        view.performClick();
                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton button = (ImageButton) view;
                        button.setImageResource(R.drawable.minigames_button);
                        button.invalidate();
                        break;
                    }
                }
            } else {
                MainMenuActivity.this.loadGameField.setVisibility(View.GONE);
            }
            return true;
        }
        if (view.equals(this.findViewById(R.id.startButton))) {
            if (this.loadGameField.getVisibility() == View.GONE) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton button = (ImageButton) view;
                        button.setImageResource(R.drawable.start_button_pressed);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        SaveGameHelper saveGameHelper = new SaveGameHelper(this);
                        if (saveGameHelper.isGameSaved()) {
                            Log.i(TAG, "Saved Game found");
                            this.loadGameField.setVisibility(View.VISIBLE);
                        } else {
                            Log.i(TAG, "No saved Game found");
                            this.startNewGame();
                        }
                        view.performClick();
                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton button = (ImageButton) view;
                        button.setImageResource(R.drawable.start_button);
                        button.invalidate();
                        break;
                    }
                }
            } else {
                this.loadGameField.setVisibility(View.GONE);
            }
            return true;
        }
        return true;
    }
}
