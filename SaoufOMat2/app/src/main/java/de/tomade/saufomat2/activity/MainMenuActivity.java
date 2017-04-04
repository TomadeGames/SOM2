package de.tomade.saufomat2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.mainGame.MainGameActivity;
import de.tomade.saufomat2.activity.mainGame.task.Task;
import de.tomade.saufomat2.constant.IntentParameter;
import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.persistance.GameValueHelper;
import de.tomade.saufomat2.persistance.sql.DatabaseHelper;

public class MainMenuActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main_menu);

        ImageButton newGameButton = (ImageButton) this.findViewById(R.id.newGameButton);
        final ImageButton loadGameButton = (ImageButton) this.findViewById(R.id.loadGameButton);
        newGameButton.setOnClickListener(this);
        loadGameButton.setOnClickListener(this);
        final RelativeLayout loadGameField = (RelativeLayout) this.findViewById(R.id.loadGameField);

        this.findViewById(R.id.startButton).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (loadGameField.getVisibility() == View.GONE) {
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
                                loadGameField.setVisibility(View.VISIBLE);
                            } else {
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
                    loadGameField.setVisibility(View.GONE);
                }
                return true;
            }
        });

        this.findViewById(R.id.gamesButton).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (loadGameField.getVisibility() == View.GONE) {
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
                    loadGameField.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    private void loadGame() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        GameValueHelper gameValueHelper = new GameValueHelper(this);

        ArrayList<Player> allPlayer = databaseHelper.getAllPlayer();
        ArrayList<Task> allTask = databaseHelper.getAllTasks();
        Player currentPlayer = gameValueHelper.getCurrentPlayer();
        int adCounter = gameValueHelper.getAdCounter();

        Intent intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
        intent.putExtra(IntentParameter.PLAYER_LIST, allPlayer);
        intent.putExtra(IntentParameter.MainGame.ALL_TASKS, allTask);
        intent.putExtra(IntentParameter.CURRENT_PLAYER, currentPlayer);
        intent.putExtra(IntentParameter.MainGame.AD_COUNTER, adCounter);
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }

    private void startNewGame() {
        Intent intent = new Intent(MainMenuActivity.this.getApplicationContext(),
                CreatePlayerActivity.class);
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
