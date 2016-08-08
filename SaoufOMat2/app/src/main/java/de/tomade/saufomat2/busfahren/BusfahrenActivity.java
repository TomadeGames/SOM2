package de.tomade.saufomat2.busfahren;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.ChooseMiniGameActivity;
import de.tomade.saufomat2.activity.mainGame.MainGameActivity;
import de.tomade.saufomat2.model.MiniGame;
import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.model.card.Card;
import de.tomade.saufomat2.model.card.CardValue;

public class BusfahrenActivity extends Activity implements View.OnClickListener {
    public static final String TAG = BusfahrenActivity.class.getSimpleName();

    private ImageButton leftButton;
    private ImageButton rightButton;
    private TextView leftText;
    private TextView rightText;
    private TextView taskText;
    private TextView drinkCounterText;
    private TextView plusText;
    private View tutorial;

    private int drinkCount = 0;
    private BusfahrenState gameState = BusfahrenState.RED_BLACK;
    private boolean buttonsClickable = true;
    private boolean fromMenue = false;

    private Card[] cards;
    private ImageView[] cardImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busfahren);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.fromMenue = extras.getBoolean("fromMenue");
        }

        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);
        if (!isFromMenue()) {
            backButton.setVisibility(View.GONE);
        } else {
            backButton.setOnClickListener(this);
        }

        this.cards = new Card[5];
        this.cardImages = new ImageView[5];
        initCards();

        this.getCardImages()[0] = (ImageView) this.findViewById(R.id.card1Image);
        this.getCardImages()[1] = (ImageView) this.findViewById(R.id.card2Image);
        this.getCardImages()[2] = (ImageView) this.findViewById(R.id.card3Image);
        this.getCardImages()[3] = (ImageView) this.findViewById(R.id.card4Image);
        this.getCardImages()[4] = (ImageView) this.findViewById(R.id.card5Image);

        this.leftButton = (ImageButton) this.findViewById(R.id.leftButton);
        this.rightButton = (ImageButton) this.findViewById(R.id.rightButton);
        this.leftText = (TextView) this.findViewById(R.id.leftButtonText);
        this.rightText = (TextView) this.findViewById(R.id.rightButtonText);
        this.taskText = (TextView) this.findViewById(R.id.taskText);
        drinkCounterText = (TextView) this.findViewById(R.id.drinkCounterText);
        plusText = (TextView) this.findViewById(R.id.drinkCounterPlusText);

        this.tutorial = this.findViewById(R.id.tutorialPanel);
        ImageButton tutorialButton = (ImageButton) this.findViewById(R.id.tutorialButton);

        tutorialButton.setOnClickListener(this);
        this.getLeftButton().setOnClickListener(this);
        this.getRightButton().setOnClickListener(this);
    }

    private void initCards() {
        this.getCards()[0] = Card.getRandomCard();

        for (int i = 1; i < 5; i++) {

            boolean valid = false;
            while (!valid) {
                valid = true;
                this.getCards()[i] = Card.getRandomCard();
                for (int j = 0; j < i; j++) {
                    if (this.getCards()[i].getValue() == this.getCards()[j].getValue()
                            && this.getCards()[i].getColor() == this.getCards()[j].getColor()) {
                        valid = false;
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            if(this.getTutorial().getVisibility() == View.VISIBLE) {
                this.getTutorial().setVisibility(View.GONE);
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(this.getTutorial().getVisibility() == View.GONE) {
            if (isButtonsClickable()) {
                buttonsClickable = false;
                switch (v.getId()) {
                    case R.id.leftButton:
                        leftButtonPressed();
                        break;
                    case R.id.rightButton:
                        rightButtonPressed();
                        break;
                    case R.id.backButton:
                        leaveGame();
                        break;
                    case R.id.tutorialButton:
                        this.getTutorial().setVisibility(View.VISIBLE);
                        buttonsClickable = true;
                        break;
                }
            }
        }
        else{
            this.getTutorial().setVisibility(View.GONE);
        }
    }

    private void nextState() {
        switch (this.getGameState()) {
            case RED_BLACK:
                this.gameState = BusfahrenState.HIGHER_LOWER;
                this.getTaskText().setText("Ist die nächste Karte höher oder tiefer?");
                this.getLeftButton().setImageResource(R.drawable.busfahren_higher_button);
                this.getRightButton().setImageResource(R.drawable.busfahren_lower_button);
                this.getLeftText().setText("Höher");
                this.getRightText().setText("Tiefer");
                break;
            case HIGHER_LOWER:
                this.gameState = BusfahrenState.BETWEEN_NOT_BETWEEN;
                this.getTaskText().setText("Ist die nächste Karte dazwischen oder nicht dazwischen?");
                this.getLeftButton().setImageResource(R.drawable.bussfahren_between_button);
                this.getRightButton().setImageResource(R.drawable.busfahren_not_between_button);
                this.getLeftText().setText("Dazwischen");
                this.getRightText().setText("Nicht\nDazwischen");
                break;
            case BETWEEN_NOT_BETWEEN:
                this.gameState = BusfahrenState.SAME_NOT_SAME;
                this.getTaskText().setText("Ist die nächste Karte die selbe, wie die Letze?");
                this.getLeftButton().setImageResource(R.drawable.busfahren_again);
                this.getRightButton().setImageResource(R.drawable.busfahren_not_again_button);
                this.getLeftText().setText("Gleich");
                this.getRightText().setText("Nicht\nGleich");
                break;
            case SAME_NOT_SAME:
                this.gameState = BusfahrenState.ACE_NO_ACE;
                this.getTaskText().setText("Ist die nächste Karte ein Ass?");
                this.getLeftButton().setImageResource(R.drawable.busfahren_ace_button);
                this.getRightButton().setImageResource(R.drawable.busfahren_no_ace_button);
                this.getLeftText().setText("Ass");
                this.getRightText().setText("Kein\nAss");
                break;
            case ACE_NO_ACE:
                leaveGame();
                break;
        }
        buttonsClickable = true;
    }

    private void leaveGame() {
        Intent intent;
        if (isFromMenue()) {
            intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
            intent.putExtra("lastGame", MiniGame.BUSFAHREN);
        } else {
            Bundle extras = this.getIntent().getExtras();
            int playerId;
            ArrayList<Player> playerList;
            intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
            if (extras != null) {
                playerList = extras.getParcelableArrayList("player");
                playerId = extras.getInt("currentPlayerId");

                Player p = Player.getPlayerById(playerList, playerId);
                p.increaseDrinks(this.getDrinkCount());

                intent.putParcelableArrayListExtra("player", playerList);
                intent.putExtra("currentPlayerId", p.getNextPlayerId());
            }
        }
        this.startActivity(intent);
    }

    private void startTimer(final boolean correctAnswer) {
        final Handler mHandler = new Handler();
        if (!correctAnswer) {
            this.getPlusText().setVisibility(View.VISIBLE);
            switch (this.getGameState()) {
                case RED_BLACK:
                    drinkCount += 1;
                    this.getPlusText().setText("+1");
                    break;
                case HIGHER_LOWER:
                    drinkCount += 2;
                    this.getPlusText().setText("+2");
                    break;
                case BETWEEN_NOT_BETWEEN:
                    drinkCount += 3;
                    this.getPlusText().setText("+3");
                    break;
                case SAME_NOT_SAME:
                    drinkCount += 4;
                    this.getPlusText().setText("+4");
                    break;
                case ACE_NO_ACE:
                    drinkCount += 5;
                    this.getPlusText().setText("+5");
            }
        }
        mHandler.postDelayed(new Runnable() {
            int counter = 0;

            @Override
            public void run() {
                if (counter == 5) {
                    if (correctAnswer) {
                        nextState();
                    } else {
                        getPlusText().setVisibility(View.GONE);
                        getPlusText().setTextSize(30);
                        restart();
                    }
                } else {
                    if (!correctAnswer) {
                        getPlusText().setTextSize(30 - counter * 10 / 4);
                    }
                }
                counter++;
                mHandler.postDelayed(this, 100);
            }
        }, 250);
    }

    private void restart() {
        getDrinkCounterText().setText("" + getDrinkCount());
        initCards();
        this.getLeftButton().setImageResource(R.drawable.busfahren_red_button);
        this.getRightButton().setImageResource(R.drawable.busfahren_balck_button);
        this.getLeftText().setText("Rot");
        this.getRightText().setText("Schwarz");
        for (ImageView i : getCardImages()) {
            i.setImageResource(R.drawable.rueckseite);
        }
        this.gameState = BusfahrenState.RED_BLACK;
        buttonsClickable = true;
    }

    private boolean checkLeftButton() {
        switch (this.getGameState()) {
            case RED_BLACK:
                this.getCardImages()[0].setImageResource(getCards()[0].getImageId());
                return getCards()[0].isRed();
            case HIGHER_LOWER:
                this.getCardImages()[1].setImageResource(getCards()[1].getImageId());
                return getCards()[1].isHigherAs(getCards()[0]);
            case BETWEEN_NOT_BETWEEN:
                this.getCardImages()[2].setImageResource(getCards()[2].getImageId());
                return getCards()[2].isBetween(getCards()[0], getCards()[1]);
            case SAME_NOT_SAME:
                this.getCardImages()[3].setImageResource(getCards()[3].getImageId());
                return getCards()[3].getValue() == getCards()[2].getValue();
            case ACE_NO_ACE:
                this.getCardImages()[4].setImageResource(getCards()[4].getImageId());
                return getCards()[4].getValue() == CardValue.ACE;
        }
        return false;
    }

    public boolean checkRightButton() {
        if(this.getGameState() == BusfahrenState.HIGHER_LOWER){
            if(this.getCards()[1].getValueAsInt() == this.getCards()[0].getValueAsInt()){
                return false;
            }
        }
        return !this.checkLeftButton();
    }

    private void leftButtonPressed() {
        startTimer(checkLeftButton());
    }

    private void rightButtonPressed() {
        startTimer(checkRightButton());
    }

    public ImageButton getLeftButton() {
        return leftButton;
    }

    public ImageButton getRightButton() {
        return rightButton;
    }

    public TextView getLeftText() {
        return leftText;
    }

    public TextView getRightText() {
        return rightText;
    }

    public TextView getTaskText() {
        return taskText;
    }

    public TextView getDrinkCounterText() {
        return drinkCounterText;
    }

    public TextView getPlusText() {
        return plusText;
    }

    public View getTutorial() {
        return tutorial;
    }

    public int getDrinkCount() {
        return drinkCount;
    }

    public BusfahrenState getGameState() {
        return gameState;
    }

    public boolean isButtonsClickable() {
        return buttonsClickable;
    }

    public boolean isFromMenue() {
        return fromMenue;
    }

    public Card[] getCards() {
        return cards;
    }

    public ImageView[] getCardImages() {
        return cardImages;
    }
}
