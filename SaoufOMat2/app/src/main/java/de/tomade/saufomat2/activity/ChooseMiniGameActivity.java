package de.tomade.saufomat2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.constant.IntentParameter;
import de.tomade.saufomat2.constant.MiniGame;

public class ChooseMiniGameActivity extends Activity implements View.OnClickListener {
    private MiniGame currentGame = MiniGame.BUSFAHREN;
    private ImageButton currentGameButton;
    private TextView gameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_choose_mini_game);

        this.gameText = (TextView) this.findViewById(R.id.gameText);
        ImageButton leftButton = (ImageButton) this.findViewById(R.id.leftButton);
        ImageButton rightButton = (ImageButton) this.findViewById(R.id.rightButton);
        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);
        this.currentGameButton = (ImageButton) this.findViewById(R.id.currentGameButton);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.currentGame = (MiniGame) extras.getSerializable(IntentParameter.LAST_GAME);
            if (this.currentGame == null) {
                throw new NullPointerException("currentGame should not be null");
            }
            switch (this.currentGame) {
                case AUGENSAUFEN:
                    this.currentGameButton.setImageResource(R.drawable.augensaufen_screen);
                    this.gameText.setText(R.string.minigame_augensaufen_caption);
                    break;
                case BIERGEBALLER:
                    this.currentGameButton.setImageResource(R.drawable.biergeballer_screen);
                    this.gameText.setText(R.string.minigame_biergeballer_caption);
                    break;
                case BUSFAHREN:
                    this.currentGameButton.setImageResource(R.drawable.busfahrer_screen);
                    this.gameText.setText(R.string.minigame_busfahren_caption);
                    break;
                case BIERRUTSCHE:
                    this.currentGameButton.setImageResource(R.drawable.circle_of_death_screen);
                    this.gameText.setText(R.string.minigame_bierrutsche_caption);
                    break;
                case ICH_HAB_NOCH_NIE:
                    this.currentGameButton.setImageResource(R.drawable.ich_hab_nie_screen);
                    this.gameText.setText(R.string.minigame_ich_hab_noch_nie_caption);
                    break;
                case KINGS:
                    this.currentGameButton.setImageResource(R.drawable.kings_screen);
                    this.gameText.setText(R.string.minigame_kings_caption);
                    break;
                case KISTEN_STAPELN:
                    this.currentGameButton.setImageResource(R.drawable.kistenstapeln_screen);
                    this.gameText.setText(R.string.minigame_kisten_stapeln_caption);
                    break;
                case WERF_DICH_DICHT:
                    this.currentGameButton.setImageResource(R.drawable.werf_dich_dicht_screen);
                    this.gameText.setText(R.string.minigame_werf_dich_dicht_caption);
                    break;
            }
        }

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
        switch (this.currentGame) {
            case AUGENSAUFEN:
                this.currentGame = MiniGame.WERF_DICH_DICHT;
                break;
            case BIERGEBALLER:
                this.currentGame = MiniGame.AUGENSAUFEN;
                break;
            case BUSFAHREN:
                this.currentGame = MiniGame.BIERGEBALLER;
                break;
            case BIERRUTSCHE:
                this.currentGame = MiniGame.BUSFAHREN;
                break;
            case ICH_HAB_NOCH_NIE:
                this.currentGame = MiniGame.BIERRUTSCHE;
                break;
            case KINGS:
                this.currentGame = MiniGame.ICH_HAB_NOCH_NIE;
                break;
            case KISTEN_STAPELN:
                this.currentGame = MiniGame.KINGS;
                break;
            case WERF_DICH_DICHT:
                this.currentGame = MiniGame.KISTEN_STAPELN;
                break;
            default:
                throw new IllegalStateException("the MiniGame [" + this.currentGame + " ] is not implementet");
        }
        this.currentGameButton.setImageResource(this.currentGame.getScreenshotId());
        this.gameText.setText(this.currentGame.getNameId());
    }

    private void rightButtonPressed() {
        switch (this.currentGame) {
            case AUGENSAUFEN:
                this.currentGame = MiniGame.BIERGEBALLER;
                break;
            case BIERGEBALLER:
                this.currentGame = MiniGame.BUSFAHREN;
                break;
            case BUSFAHREN:
                this.currentGame = MiniGame.BIERRUTSCHE;
                break;
            case BIERRUTSCHE:
                this.currentGame = MiniGame.ICH_HAB_NOCH_NIE;
                break;
            case ICH_HAB_NOCH_NIE:
                this.currentGame = MiniGame.KINGS;
                break;
            case KINGS:
                this.currentGame = MiniGame.KISTEN_STAPELN;
                break;
            case KISTEN_STAPELN:
                this.currentGame = MiniGame.WERF_DICH_DICHT;
                break;
            case WERF_DICH_DICHT:
                this.currentGame = MiniGame.AUGENSAUFEN;
                break;
            default:
                throw new IllegalStateException("the MiniGame [" + this.currentGame + " ] is not implementet");
        }
        this.currentGameButton.setImageResource(this.currentGame.getScreenshotId());
        this.gameText.setText(this.currentGame.getNameId());
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
