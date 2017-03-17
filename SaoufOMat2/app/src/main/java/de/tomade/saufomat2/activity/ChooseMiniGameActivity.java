package de.tomade.saufomat2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.EnumSet;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.constant.IntentParameter;
import de.tomade.saufomat2.constant.MiniGame;

public class ChooseMiniGameActivity extends Activity implements View.OnClickListener {
    private MiniGame currentGame = MiniGame.BIERRUTSCHE;
    private ImageButton currentGameButton;
    private TextView gameText;
    private MiniGame[] allGames;
    private int minigameIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_choose_mini_game);

        Object[] allGamesAsObjects = EnumSet.allOf(MiniGame.class).toArray();
        this.allGames = new MiniGame[allGamesAsObjects.length];
        for (int i = 0; i < this.allGames.length; i++) {
            this.allGames[i] = (MiniGame) allGamesAsObjects[i];
        }


        this.gameText = (TextView) this.findViewById(R.id.gameText);
        ImageButton leftButton = (ImageButton) this.findViewById(R.id.leftButton);
        ImageButton rightButton = (ImageButton) this.findViewById(R.id.rightButton);
        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.currentGame = (MiniGame) extras.getSerializable(IntentParameter.LAST_GAME);
        }
        this.currentGameButton = (ImageButton) this.findViewById(R.id.currentGameButton);
        this.gameText.setText(this.currentGame.getNameId());

        for (int i = 0; i < this.allGames.length; i++) {
            if (this.allGames[i].equals(this.currentGame)) {
                this.minigameIndex = i;
            }
        }
        this.currentGameButton.setImageResource(this.currentGame.getScreenshotId());
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        this.currentGameButton.setOnClickListener(this);

        this.initButtons();
    }

    private void initButtons() {
        this.findViewById(R.id.leftButton).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton imageButton = (ImageButton) view;
                        imageButton.setImageResource(R.drawable.left_button_pressed);
                        imageButton.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        leftButtonPressed();

                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton imageButton = (ImageButton) view;
                        imageButton.setImageResource(R.drawable.left_button);
                        imageButton.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        this.findViewById(R.id.rightButton).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton) v;
                        view.setImageResource(R.drawable.right_button_pressed);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        rightButtonPressed();

                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        view.setImageResource(R.drawable.right_button);
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftButton:
                this.leftButtonPressed();
                break;
            case R.id.rightButton:
                this.rightButtonPressed();
                break;
            case R.id.backButton:
                this.backButtonPressed();
                break;
            case R.id.currentGameButton:
                this.currentGameButtonPressed();
                break;
        }
    }

    private void leftButtonPressed() {
        this.minigameIndex--;
        if (this.minigameIndex < 0) {
            this.minigameIndex = this.allGames.length - 1;
        }
        this.reloadView();
    }

    private void reloadView() {
        this.currentGame = this.allGames[this.minigameIndex];
        this.currentGameButton.setImageResource(this.currentGame.getScreenshotId());
        this.gameText.setText(this.currentGame.getNameId());
    }

    private void rightButtonPressed() {
        this.minigameIndex++;
        if (this.minigameIndex > this.allGames.length - 1) {
            this.minigameIndex = 0;
        }
        this.reloadView();
    }

    private void backButtonPressed() {
        Intent intent = new Intent(this.getApplicationContext(), MainMenuActivity.class);
        this.startActivity(intent);
    }

    private void currentGameButtonPressed() {
        Intent intent = new Intent(this.getApplicationContext(), this.currentGame.getActivity());
        intent.putExtra(IntentParameter.FROM_MAIN_GAME, false);
        this.startActivity(intent);
    }
}
