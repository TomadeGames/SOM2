package com.tomade.saufomat.activity;

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
import com.tomade.saufomat.activity.mainGame.MainGameActivity;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.model.player.Player;
import com.tomade.saufomat.persistance.SaveGameHelper;
import com.tomade.saufomat.persistance.sql.DatabaseHelper;

import java.util.ArrayList;

public class MainMenuActivity extends Activity implements View.OnClickListener {
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

        this.findViewById(R.id.startButton).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MainMenuActivity.this.loadGameField.getVisibility() == View.GONE) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            ImageButton view = (ImageButton) v;
                            view.setImageResource(R.drawable.start_button_pressed);
                            v.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            SaveGameHelper saveGameHelper = new SaveGameHelper(MainMenuActivity.this);
                            if (saveGameHelper.isGameSaved()) {
                                Log.i(TAG, "Saved Game found");
                                MainMenuActivity.this.loadGameField.setVisibility(View.VISIBLE);
                            } else {
                                Log.i(TAG, "No saved Game found");
                                startNewGame();
                            }
                        case MotionEvent.ACTION_CANCEL: {
                            ImageButton view = (ImageButton) v;
                            view.setImageResource(R.drawable.start_button);
                            view.invalidate();
                            break;
                        }
                    }
                } else {
                    MainMenuActivity.this.loadGameField.setVisibility(View.GONE);
                }
                return true;
            }
        });

        this.findViewById(R.id.gamesButton).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MainMenuActivity.this.loadGameField.getVisibility() == View.GONE) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            ImageButton view = (ImageButton) v;
                            view.setImageResource(R.drawable.minigames_button_pressed);
                            v.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:

                            Intent intent = new Intent(MainMenuActivity.this.getApplicationContext(),
                                    ChooseMiniGameActivity.class);
                            MainMenuActivity.this.startActivity(intent);

                        case MotionEvent.ACTION_CANCEL: {
                            ImageButton view = (ImageButton) v;
                            view.setImageResource(R.drawable.minigames_button);
                            view.invalidate();
                            break;
                        }
                    }
                } else {
                    MainMenuActivity.this.loadGameField.setVisibility(View.GONE);
                }
                return true;
            }
        });
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

    private void startNewGame() {
        Intent intent = new Intent(this, CreatePlayerActivity.class);
        intent.putExtra(IntentParameter.MainGame.NEW_GAME, true);
        MainMenuActivity.this.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newGameButton:
                this.startNewGame();
                break;
            case R.id.loadGameButton:
                this.loadGame();
                break;
        }
    }
}
