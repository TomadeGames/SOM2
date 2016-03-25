package de.tomade.saufomat2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.miniGames.IchHabNochNieActivity;
import de.tomade.saufomat2.activity.miniGames.busfahren.BusfahrenActivity;
import de.tomade.saufomat2.model.miniGame;

public class ChooseMiniGameActivity extends Activity implements View.OnClickListener {
    private miniGame currentGame = miniGame.BUSFAHREN;
    private ImageButton currentGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mini_game);

        ImageButton leftButton = (ImageButton) this.findViewById(R.id.leftButton);
        ImageButton rightButton = (ImageButton) this.findViewById(R.id.rightButton);
        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);
        this.currentGameButton = (ImageButton) this.findViewById(R.id.currentGameButton);

        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        this.currentGameButton.setOnClickListener(this);

        initButtons();
    }

    private void initButtons() {
        findViewById(R.id.leftButton).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton) v;
                        view.setImageResource(R.drawable.left_button_pressed);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        leftButtonPressed();

                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        view.setImageResource(R.drawable.left_button);
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        findViewById(R.id.rightButton).setOnTouchListener(new View.OnTouchListener() {

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
        switch (this.currentGame) {
            case AUGENSAUFEN:
                currentGame = miniGame.WERF_DICH_DICHT;
                this.currentGameButton.setImageResource(R.drawable.werf_dich_dicht_screen);
                break;
            case BIERGEBALLER:
                currentGame = miniGame.AUGENSAUFEN;
                this.currentGameButton.setImageResource(R.drawable.augensaufen_screen);
                break;
            case BUSFAHREN:
                currentGame = miniGame.BIERGEBALLER;
                this.currentGameButton.setImageResource(R.drawable.biergeballer_screen);
                break;
            case CIRCLE_OF_DEATH:
                currentGame = miniGame.BUSFAHREN;
                this.currentGameButton.setImageResource(R.drawable.busfahrer_screen);
                break;
            case ICH_HAB_NOCH_NIE:
                currentGame = miniGame.CIRCLE_OF_DEATH;
                this.currentGameButton.setImageResource(R.drawable.circle_of_death_screen);
                break;
            case KINGS:
                currentGame = miniGame.ICH_HAB_NOCH_NIE;
                this.currentGameButton.setImageResource(R.drawable.ich_hab_nie_screen);
                break;
            case KISTEN_STAPELN:
                currentGame = miniGame.KINGS;
                this.currentGameButton.setImageResource(R.drawable.kings_screen);
                break;
            case WERF_DICH_DICHT:
                currentGame = miniGame.KISTEN_STAPELN;
                this.currentGameButton.setImageResource(R.drawable.kistenstapeln_screen);
                break;
        }
    }

    private void rightButtonPressed() {
        switch (this.currentGame) {
            case AUGENSAUFEN:
                currentGame = miniGame.BIERGEBALLER;
                this.currentGameButton.setImageResource(R.drawable.biergeballer_screen);
                break;
            case BIERGEBALLER:
                currentGame = miniGame.BUSFAHREN;
                this.currentGameButton.setImageResource(R.drawable.busfahrer_screen);
                break;
            case BUSFAHREN:
                currentGame = miniGame.CIRCLE_OF_DEATH;
                this.currentGameButton.setImageResource(R.drawable.circle_of_death_screen);
                break;
            case CIRCLE_OF_DEATH:
                currentGame = miniGame.ICH_HAB_NOCH_NIE;
                this.currentGameButton.setImageResource(R.drawable.ich_hab_nie_screen);
                break;
            case ICH_HAB_NOCH_NIE:
                currentGame = miniGame.KINGS;
                this.currentGameButton.setImageResource(R.drawable.kings_screen);
                break;
            case KINGS:
                currentGame = miniGame.KISTEN_STAPELN;
                this.currentGameButton.setImageResource(R.drawable.kistenstapeln_screen);
                break;
            case KISTEN_STAPELN:
                currentGame = miniGame.WERF_DICH_DICHT;
                this.currentGameButton.setImageResource(R.drawable.werf_dich_dicht_screen);
                break;
            case WERF_DICH_DICHT:
                currentGame = miniGame.AUGENSAUFEN;
                this.currentGameButton.setImageResource(R.drawable.augensaufen_screen);
                break;
        }
    }

    private void backButtonPressed() {
        Intent intent = new Intent(this.getApplicationContext(), MainMenuActivity.class);
        this.startActivity(intent);
    }

    private void startGame(Intent intent) {
        intent.putExtra("fromMenue", true);
        this.startActivity(intent);
    }

    //TODO Spiele implementieren und verlinken
    private void currentGameButtonPressed() {
        Intent intent;
        switch (this.currentGame) {
            case AUGENSAUFEN:
                break;
            case BIERGEBALLER:
                break;
            case BUSFAHREN:
                intent = new Intent(this.getApplicationContext(), BusfahrenActivity.class);
                startGame(intent);
                break;
            case CIRCLE_OF_DEATH:
                break;
            case ICH_HAB_NOCH_NIE:
                intent = new Intent(this.getApplicationContext(), IchHabNochNieActivity.class);
                startGame(intent);
                break;
            case KINGS:
                break;
            case KISTEN_STAPELN:
                break;
            case WERF_DICH_DICHT:
                break;
        }
    }
}