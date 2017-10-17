package com.tomade.saufomat.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tomade.saufomat.R;
import com.tomade.saufomat.constant.Direction;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.constant.MiniGame;

import java.util.EnumSet;

public class ChooseMiniGameActivity extends Activity implements View.OnTouchListener {
    private static final MiniGame[] NOT_CHOOSABLE_MINIGAMES = {MiniGame.BIERRUTSCHE};

    private MiniGame currentGame;
    private ImageButton currentGameButton;
    private TextView gameText;
    private MiniGame[] allGames;
    private int miniGameIndex = 0;

    private SwipeController swipeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_choose_mini_game);
        EnumSet<MiniGame> allGames = EnumSet.allOf(MiniGame.class);
        for (MiniGame notChooseableMiniGame : NOT_CHOOSABLE_MINIGAMES) {
            allGames.remove(notChooseableMiniGame);
        }
        Object[] allGamesAsObjects = allGames.toArray();
        this.allGames = new MiniGame[allGamesAsObjects.length];
        for (int i = 0; i < this.allGames.length; i++) {
            this.allGames[i] = (MiniGame) allGamesAsObjects[i];
        }

        this.currentGame = this.allGames[0];

        this.gameText = this.findViewById(R.id.gameText);
        ImageButton backButton = this.findViewById(R.id.backButton);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.currentGame = (MiniGame) extras.getSerializable(IntentParameter.LAST_GAME);
        }
        this.currentGameButton = this.findViewById(R.id.currentGameButton);
        this.gameText.setText(this.currentGame.getNameId());

        for (int i = 0; i < this.allGames.length; i++) {
            if (this.allGames[i].equals(this.currentGame)) {
                this.miniGameIndex = i;
            }
        }
        this.currentGameButton.setImageResource(this.currentGame.getScreenshotId());
        backButton.setOnTouchListener(this);
        this.currentGameButton.setOnTouchListener(this);
        this.findViewById(R.id.leftButton).setOnTouchListener(this);
        this.findViewById(R.id.rightButton).setOnTouchListener(this);
        this.swipeController = new SwipeController();
    }

    private void leftButtonPressed() {
        this.miniGameIndex--;
        if (this.miniGameIndex < 0) {
            this.miniGameIndex = this.allGames.length - 1;
        }
        this.reloadView();
    }

    private void reloadView() {
        this.currentGame = this.allGames[this.miniGameIndex];
        this.currentGameButton.setImageResource(this.currentGame.getScreenshotId());
        this.gameText.setText(this.currentGame.getNameId());
    }

    private void rightButtonPressed() {
        this.miniGameIndex++;
        if (this.miniGameIndex > this.allGames.length - 1) {
            this.miniGameIndex = 0;
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

    private void handleSwipe(MotionEvent event) {
        if (this.swipeController.handleSwipe(event) && this.swipeController.getDistance() > 10) {
            if (this.swipeController.getDirectionX() == Direction.LEFT) {
                this.rightButtonPressed();
            } else {
                this.leftButtonPressed();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.handleSwipe(event);
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.handleSwipe(motionEvent);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                switch (view.getId()) {
                    case R.id.rightButton:
                        ((ImageButton) view).setImageResource(R.drawable.right_button_pressed);
                        view.invalidate();
                        break;
                    case R.id.leftButton:
                        ((ImageButton) view).setImageResource(R.drawable.left_button_pressed);
                        view.invalidate();
                        break;
                    default:
                        view.invalidate();
                        break;
                }
                break;
            }

            case MotionEvent.ACTION_UP:
                if (ChooseMiniGameActivity.this.swipeController.getDistance() < 10) {
                    switch (view.getId()) {
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

            case MotionEvent.ACTION_CANCEL: {
                switch (view.getId()) {
                    case R.id.rightButton:
                        ((ImageButton) view).setImageResource(R.drawable.right_button);
                        view.invalidate();
                        break;
                    case R.id.leftButton:
                        ((ImageButton) view).setImageResource(R.drawable.left_button);
                        view.invalidate();
                        break;
                    default:
                }
                break;
            }
        }
        return true;
    }
}
