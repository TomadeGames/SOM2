package de.tomade.saufomat2.activity.miniGames.busfahren;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.miniGames.BaseMiniGame;
import de.tomade.saufomat2.model.card.Card;
import de.tomade.saufomat2.model.card.CardValue;

//TODO: wenn aus minispielmenü soll das spiel neu starten und nicht beendet werden. Vieleicht eine Frage ob neu
// gestartet werden soll?
public class BusfahrenActivity extends BaseMiniGame implements View.OnClickListener {
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

    private Card[] cards;
    private ImageView[] cardImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busfahren);

        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);
        if (this.fromMainGame) {
            backButton.setVisibility(View.GONE);
        } else {
            backButton.setOnClickListener(this);
        }

        this.cards = new Card[5];
        this.cardImages = new ImageView[5];
        initCards();

        this.cardImages[0] = (ImageView) this.findViewById(R.id.card1Image);
        this.cardImages[1] = (ImageView) this.findViewById(R.id.card2Image);
        this.cardImages[2] = (ImageView) this.findViewById(R.id.card3Image);
        this.cardImages[3] = (ImageView) this.findViewById(R.id.card4Image);
        this.cardImages[4] = (ImageView) this.findViewById(R.id.card5Image);

        this.leftButton = (ImageButton) this.findViewById(R.id.leftButton);
        this.rightButton = (ImageButton) this.findViewById(R.id.rightButton);
        this.leftText = (TextView) this.findViewById(R.id.leftButtonText);
        this.rightText = (TextView) this.findViewById(R.id.rightButtonText);
        this.taskText = (TextView) this.findViewById(R.id.taskText);
        this.drinkCounterText = (TextView) this.findViewById(R.id.drinkCounterText);
        this.plusText = (TextView) this.findViewById(R.id.drinkCounterPlusText);

        this.tutorial = this.findViewById(R.id.tutorialPanel);
        ImageButton tutorialButton = (ImageButton) this.findViewById(R.id.tutorialButton);

        tutorialButton.setOnClickListener(this);
        this.leftButton.setOnClickListener(this);
        this.rightButton.setOnClickListener(this);
    }

    private void initCards() {
        this.cards[0] = Card.getRandomCard();

        for (int i = 1; i < 5; i++) {

            boolean valid = false;
            while (!valid) {
                valid = true;
                this.cards[i] = Card.getRandomCard();
                for (int j = 0; j < i; j++) {
                    if (this.cards[i].getValue() == this.cards[j].getValue()
                            && this.cards[i].getColor() == this.cards[j].getColor()) {
                        valid = false;
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            if (this.tutorial.getVisibility() == View.VISIBLE) {
                this.tutorial.setVisibility(View.GONE);
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (this.tutorial.getVisibility() == View.GONE) {
            if (this.buttonsClickable) {
                this.buttonsClickable = false;
                switch (v.getId()) {
                    case R.id.leftButton:
                        leftButtonPressed();
                        break;
                    case R.id.rightButton:
                        rightButtonPressed();
                        break;
                    case R.id.backButton:
                        this.currentPlayer.increaseDrinks(this.drinkCount);
                        this.leaveGame();
                        break;
                    case R.id.tutorialButton:
                        this.tutorial.setVisibility(View.VISIBLE);
                        this.buttonsClickable = true;
                        break;
                }
            }
        } else {
            this.tutorial.setVisibility(View.GONE);
        }
    }

    private void nextState() {
        switch (this.gameState) {
            case RED_BLACK:
                this.gameState = BusfahrenState.HIGHER_LOWER;
                this.taskText.setText(R.string.minigame_busfahren_question_second);
                this.leftButton.setImageResource(R.drawable.busfahren_higher_button);
                this.rightButton.setImageResource(R.drawable.busfahren_lower_button);
                this.leftText.setText(R.string.minigame_busfahren_question_second_answer_left);
                this.rightText.setText(R.string.minigame_busfahren_question_second_answer_right);
                break;
            case HIGHER_LOWER:
                this.gameState = BusfahrenState.BETWEEN_NOT_BETWEEN;
                this.taskText.setText(R.string.minigame_busfahren_question_third);
                this.leftButton.setImageResource(R.drawable.bussfahren_between_button);
                this.rightButton.setImageResource(R.drawable.busfahren_not_between_button);
                this.leftText.setText(R.string.minigame_busfahren_question_third_answer_left);
                this.rightText.setText(R.string.minigame_busfahren_question_third_answer_right);
                break;
            case BETWEEN_NOT_BETWEEN:
                this.gameState = BusfahrenState.SAME_NOT_SAME;
                this.taskText.setText(R.string.minigame_busfahren_question_fourth);
                this.leftButton.setImageResource(R.drawable.busfahren_again);
                this.rightButton.setImageResource(R.drawable.busfahren_not_again_button);
                this.leftText.setText(R.string.minigame_busfahren_question_fourth_answer_left);
                this.rightText.setText(R.string.minigame_busfahren_question_fourth_answer_right);
                break;
            case SAME_NOT_SAME:
                this.gameState = BusfahrenState.ACE_NO_ACE;
                this.taskText.setText(R.string.minigame_busfahren_question_last);
                this.leftButton.setImageResource(R.drawable.busfahren_ace_button);
                this.rightButton.setImageResource(R.drawable.busfahren_no_ace_button);
                this.leftText.setText(R.string.minigame_busfahren_question_last_answer_left);
                this.rightText.setText(R.string.minigame_busfahren_question_last_answer_right);
                break;
            case ACE_NO_ACE:
                leaveGame();
                break;
        }
        this.buttonsClickable = true;
    }

    private void startTimer(final boolean correctAnswer) {
        final Handler mHandler = new Handler();
        if (!correctAnswer) {
            this.plusText.setVisibility(View.VISIBLE);
            switch (this.gameState) {
                case RED_BLACK:
                    this.drinkCount += 1;
                    this.plusText.setText("+1");
                    break;
                case HIGHER_LOWER:
                    this.drinkCount += 2;
                    this.plusText.setText("+2");
                    break;
                case BETWEEN_NOT_BETWEEN:
                    this.drinkCount += 3;
                    this.plusText.setText("+3");
                    break;
                case SAME_NOT_SAME:
                    this.drinkCount += 4;
                    this.plusText.setText("+4");
                    break;
                case ACE_NO_ACE:
                    this.drinkCount += 5;
                    this.plusText.setText("+5");
            }
        }
        mHandler.postDelayed(new Runnable() {
            int counter = 0;

            @Override
            public void run() {
                if (this.counter == 5) {
                    if (correctAnswer) {
                        nextState();
                    } else {
                        BusfahrenActivity.this.plusText.setVisibility(View.GONE);
                        BusfahrenActivity.this.plusText.setTextSize(30);
                        restart();
                    }
                } else {
                    if (!correctAnswer) {
                        BusfahrenActivity.this.plusText.setTextSize(30 - this.counter * 10 / 4);
                    }
                }
                this.counter++;
                mHandler.postDelayed(this, 100);
            }
        }, 250);
    }

    private void restart() {
        this.drinkCounterText.setText("" + this.drinkCount);
        initCards();
        this.leftButton.setImageResource(R.drawable.busfahren_red_button);
        this.rightButton.setImageResource(R.drawable.busfahren_balck_button);
        this.leftText.setText(R.string.minigame_busfahren_question_first_answer_left);
        this.rightText.setText(R.string.minigame_busfahren_question_first_answer_right);
        for (ImageView i : this.cardImages) {
            i.setImageResource(R.drawable.rueckseite);
        }
        this.gameState = BusfahrenState.RED_BLACK;
        this.buttonsClickable = true;
    }

    private boolean checkLeftButton() {
        switch (this.gameState) {
            case RED_BLACK:
                this.cardImages[0].setImageResource(this.cards[0].getImageId());
                return this.cards[0].isRed();
            case HIGHER_LOWER:
                this.cardImages[1].setImageResource(this.cards[1].getImageId());
                return this.cards[1].isHigherAs(this.cards[0]);
            case BETWEEN_NOT_BETWEEN:
                this.cardImages[2].setImageResource(this.cards[2].getImageId());
                return this.cards[2].isBetween(this.cards[0], this.cards[1]);
            case SAME_NOT_SAME:
                this.cardImages[3].setImageResource(this.cards[3].getImageId());
                return this.cards[3].getValue() == this.cards[2].getValue();
            case ACE_NO_ACE:
                this.cardImages[4].setImageResource(this.cards[4].getImageId());
                return this.cards[4].getValue() == CardValue.ACE;
        }
        return false;
    }

    public boolean checkRightButton() {
        if (this.gameState == BusfahrenState.HIGHER_LOWER) {
            if (this.cards[1].getValueAsInt() == this.cards[0].getValueAsInt()) {
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
}
