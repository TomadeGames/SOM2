package com.tomade.saufomat.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.mainGame.MainGameActivity;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.model.Player;
import com.tomade.saufomat.persistance.GameValueHelper;
import com.tomade.saufomat.persistance.sql.DatabaseHelper;

import java.util.ArrayList;

public class MainMenuActivity extends Activity implements View.OnClickListener {
    private static final String TAG = MainMenuActivity.class.getSimpleName();
    private RelativeLayout loadGameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main_menu);

        ImageButton newGameButton = (ImageButton) this.findViewById(R.id.newGameButton);
        final ImageButton loadGameButton = (ImageButton) this.findViewById(R.id.loadGameButton);
        newGameButton.setOnClickListener(this);
        loadGameButton.setOnClickListener(this);
        this.loadGameField = (RelativeLayout) this.findViewById(R.id.loadGameField);

        TextView versionTextView = (TextView) this.findViewById(R.id.versionTextView);
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
                            GameValueHelper gameValueHelper = new GameValueHelper(MainMenuActivity.this);
                            if (gameValueHelper.isGameSaved()) {
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
        GameValueHelper gameValueHelper = new GameValueHelper(this);

        ArrayList<Player> allPlayer = databaseHelper.getAllPlayer();
        Player currentPlayer = gameValueHelper.getCurrentPlayer();
        int adCounter = gameValueHelper.getAdCounter();

        Intent intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
        intent.putExtra(IntentParameter.PLAYER_LIST, allPlayer);
        intent.putExtra(IntentParameter.MainGame.NEW_GAME, false);
        intent.putExtra(IntentParameter.CURRENT_PLAYER, currentPlayer);
        intent.putExtra(IntentParameter.MainGame.AD_COUNTER, adCounter);
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
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
