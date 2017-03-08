package de.tomade.saufomat2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.miniGames.augensaufen.AugensaufenActivity;
import de.tomade.saufomat2.activity.miniGames.biergeballer.BiergeballerActivity;
import de.tomade.saufomat2.activity.miniGames.bierrutsche.BierrutscheActivity;
import de.tomade.saufomat2.activity.miniGames.busfahren.BusfahrenActivity;
import de.tomade.saufomat2.activity.miniGames.ichHabNochNie.IchHabNochNieActivity;
import de.tomade.saufomat2.activity.miniGames.kings.KingsActivity;
import de.tomade.saufomat2.activity.miniGames.kistenStapeln.KistenStapelnActivity;
import de.tomade.saufomat2.activity.miniGames.werfDichDicht.WerfDichDichtActivity;
import de.tomade.saufomat2.constant.MiniGame;

public class ChooseMiniGameActivity extends Activity implements View.OnClickListener {
    private MiniGame currentGame = MiniGame.BUSFAHREN;
    private ImageButton currentGameButton;
    private TextView gameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mini_game);

        this.setGameText((TextView) this.findViewById(R.id.gameText));
        ImageButton leftButton = (ImageButton) this.findViewById(R.id.leftButton);
        ImageButton rightButton = (ImageButton) this.findViewById(R.id.rightButton);
        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);
        this.setCurrentGameButton((ImageButton) this.findViewById(R.id.currentGameButton));

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.setCurrentGame((MiniGame) extras.getSerializable("lastGame"));
            switch (this.getCurrentGame()) {
                case AUGENSAUFEN:
                    this.getCurrentGameButton().setImageResource(R.drawable.augensaufen_screen);
                    this.getGameText().setText(R.string.minigame_augensaufen_caption);
                    break;
                case BIERGEBALLER:
                    this.getCurrentGameButton().setImageResource(R.drawable.biergeballer_screen);
                    this.getGameText().setText(R.string.minigame_biergeballer_caption);
                    break;
                case BUSFAHREN:
                    this.getCurrentGameButton().setImageResource(R.drawable.busfahrer_screen);
                    this.getGameText().setText(R.string.minigame_busfahren_caption);
                    break;
                case BIERRUTSCHE:
                    this.getCurrentGameButton().setImageResource(R.drawable.circle_of_death_screen);
                    this.getGameText().setText(R.string.minigame_bierrutsche_caption);
                    break;
                case ICH_HAB_NOCH_NIE:
                    this.getCurrentGameButton().setImageResource(R.drawable.ich_hab_nie_screen);
                    this.getGameText().setText(R.string.minigame_ich_hab_noch_nie_caption);
                    break;
                case KINGS:
                    this.getCurrentGameButton().setImageResource(R.drawable.kings_screen);
                    this.getGameText().setText(R.string.minigame_kings_caption);
                    break;
                case KISTEN_STAPELN:
                    this.getCurrentGameButton().setImageResource(R.drawable.kistenstapeln_screen);
                    this.getGameText().setText(R.string.minigame_kisten_stapeln_caption);
                    break;
                case WERF_DICH_DICHT:
                    this.getCurrentGameButton().setImageResource(R.drawable.werf_dich_dicht_screen);
                    this.getGameText().setText(R.string.minigame_werf_dich_dicht_caption);
                    break;
            }
        }

        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        this.getCurrentGameButton().setOnClickListener(this);

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
        switch (this.getCurrentGame()) {
            case AUGENSAUFEN:
                setCurrentGame(MiniGame.WERF_DICH_DICHT);
                this.getCurrentGameButton().setImageResource(R.drawable.werf_dich_dicht_screen);
                this.getGameText().setText(R.string.minigame_werf_dich_dicht_caption);
                break;
            case BIERGEBALLER:
                setCurrentGame(MiniGame.AUGENSAUFEN);
                this.getCurrentGameButton().setImageResource(R.drawable.augensaufen_screen);
                this.getGameText().setText(R.string.minigame_augensaufen_caption);
                break;
            case BUSFAHREN:
                setCurrentGame(MiniGame.BIERGEBALLER);
                this.getCurrentGameButton().setImageResource(R.drawable.biergeballer_screen);
                this.getGameText().setText(R.string.minigame_biergeballer_caption);
                break;
            case BIERRUTSCHE:
                setCurrentGame(MiniGame.BUSFAHREN);
                this.getCurrentGameButton().setImageResource(R.drawable.busfahrer_screen);
                this.getGameText().setText(R.string.minigame_busfahren_caption);
                break;
            case ICH_HAB_NOCH_NIE:
                setCurrentGame(MiniGame.BIERRUTSCHE);
                this.getCurrentGameButton().setImageResource(R.drawable.circle_of_death_screen); //TODO Bild ändern
                this.getGameText().setText(R.string.minigame_bierrutsche_caption);
                break;
            case KINGS:
                setCurrentGame(MiniGame.ICH_HAB_NOCH_NIE);
                this.getCurrentGameButton().setImageResource(R.drawable.ich_hab_nie_screen);
                this.getGameText().setText(R.string.minigame_ich_hab_noch_nie_caption);
                break;
            case KISTEN_STAPELN:
                setCurrentGame(MiniGame.KINGS);
                this.getCurrentGameButton().setImageResource(R.drawable.kings_screen);
                this.getGameText().setText(R.string.minigame_kings_caption);
                break;
            case WERF_DICH_DICHT:
                setCurrentGame(MiniGame.KISTEN_STAPELN);
                this.getCurrentGameButton().setImageResource(R.drawable.kistenstapeln_screen);
                this.getGameText().setText(R.string.minigame_kisten_stapeln_caption);
                break;
        }
    }

    private void rightButtonPressed() {
        switch (this.getCurrentGame()) {
            case AUGENSAUFEN:
                setCurrentGame(MiniGame.BIERGEBALLER);
                this.getCurrentGameButton().setImageResource(R.drawable.biergeballer_screen);
                this.getGameText().setText(R.string.minigame_biergeballer_caption);
                break;
            case BIERGEBALLER:
                setCurrentGame(MiniGame.BUSFAHREN);
                this.getCurrentGameButton().setImageResource(R.drawable.busfahrer_screen);
                this.getGameText().setText(R.string.minigame_busfahren_caption);
                break;
            case BUSFAHREN:
                setCurrentGame(MiniGame.BIERRUTSCHE);
                this.getCurrentGameButton().setImageResource(R.drawable.circle_of_death_screen);
                this.getGameText().setText(R.string.minigame_bierrutsche_caption);
                break;
            case BIERRUTSCHE:
                setCurrentGame(MiniGame.ICH_HAB_NOCH_NIE);
                this.getCurrentGameButton().setImageResource(R.drawable.ich_hab_nie_screen);
                this.getGameText().setText(R.string.minigame_ich_hab_noch_nie_caption);
                break;
            case ICH_HAB_NOCH_NIE:
                setCurrentGame(MiniGame.KINGS);
                this.getCurrentGameButton().setImageResource(R.drawable.kings_screen);
                this.getGameText().setText(R.string.minigame_kings_caption);
                break;
            case KINGS:
                setCurrentGame(MiniGame.KISTEN_STAPELN);
                this.getCurrentGameButton().setImageResource(R.drawable.kistenstapeln_screen);
                this.getGameText().setText(R.string.minigame_kisten_stapeln_caption);
                break;
            case KISTEN_STAPELN:
                setCurrentGame(MiniGame.WERF_DICH_DICHT);
                this.getCurrentGameButton().setImageResource(R.drawable.werf_dich_dicht_screen);
                this.getGameText().setText(R.string.minigame_werf_dich_dicht_caption);
                break;
            case WERF_DICH_DICHT:
                setCurrentGame(MiniGame.AUGENSAUFEN);
                this.getCurrentGameButton().setImageResource(R.drawable.augensaufen_screen);
                this.getGameText().setText(R.string.minigame_augensaufen_caption);
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
        switch (this.getCurrentGame()) {
            case AUGENSAUFEN:
                intent = new Intent(this.getApplicationContext(), AugensaufenActivity.class);
                startGame(intent);
                break;
            case BIERGEBALLER:
                intent = new Intent(this.getApplicationContext(), BiergeballerActivity.class);
                startGame(intent);
                break;
            case BUSFAHREN:
                intent = new Intent(this.getApplicationContext(), BusfahrenActivity.class);
                startGame(intent);
                break;
            case BIERRUTSCHE:
                intent = new Intent(this.getApplicationContext(), BierrutscheActivity.class);
                startGame(intent);
                break;
            case ICH_HAB_NOCH_NIE:
                intent = new Intent(this.getApplicationContext(), IchHabNochNieActivity.class);
                startGame(intent);
                break;
            case KINGS:
                intent = new Intent(this.getApplicationContext(), KingsActivity.class);
                startGame(intent);
                break;
            case KISTEN_STAPELN:
                intent = new Intent(this.getApplicationContext(), KistenStapelnActivity.class);
                startGame(intent);
                break;
            case WERF_DICH_DICHT:
                intent = new Intent(this.getApplicationContext(), WerfDichDichtActivity.class);
                startGame(intent);
                break;
        }
    }

    public MiniGame getCurrentGame() {
        return this.currentGame;
    }

    public void setCurrentGame(MiniGame currentGame) {
        this.currentGame = currentGame;
    }

    public ImageButton getCurrentGameButton() {
        return this.currentGameButton;
    }

    public void setCurrentGameButton(ImageButton currentGameButton) {
        this.currentGameButton = currentGameButton;
    }

    public TextView getGameText() {
        return this.gameText;
    }

    public void setGameText(TextView gameText) {
        this.gameText = gameText;
    }
}
