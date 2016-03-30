package de.tomade.saufomat2.activity.miniGames.busfahren;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.ChooseMiniGameActivity;
import de.tomade.saufomat2.activity.mainGame.MainGameActivity;
import de.tomade.saufomat2.model.MiniGame;
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
    private View tutorail;

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
        if (!fromMenue) {
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
        drinkCounterText = (TextView) this.findViewById(R.id.drinkCounterText);
        plusText = (TextView) this.findViewById(R.id.drinkCounterPlusText);

        this.tutorail = this.findViewById(R.id.tutorialPanel);
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
            if(this.tutorail.getVisibility() == View.VISIBLE) {
                this.tutorail.setVisibility(View.GONE);
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(this.tutorail.getVisibility() == View.GONE) {
            if (buttonsClickable) {
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
                        this.tutorail.setVisibility(View.VISIBLE);
                        buttonsClickable = true;
                        break;
                }
            }
        }
        else{
            this.tutorail.setVisibility(View.GONE);
        }
    }

    private void nextState() {
        switch (this.gameState) {
            case RED_BLACK:
                this.gameState = BusfahrenState.HIGHER_LOWER;
                this.taskText.setText("Ist die nächste Karte höher oder tiefer?");
                this.leftButton.setImageResource(R.drawable.busfahren_higher_button);
                this.rightButton.setImageResource(R.drawable.busfahren_lower_button);
                this.leftText.setText("Höher");
                this.rightText.setText("Tiefer");
                break;
            case HIGHER_LOWER:
                this.gameState = BusfahrenState.BETWEEN_NOT_BETWEEN;
                this.taskText.setText("Ist die nächste Karte dazwischen oder nicht dazwischen?");
                this.leftButton.setImageResource(R.drawable.bussfahren_between_button);
                this.rightButton.setImageResource(R.drawable.busfahren_not_between_button);
                this.leftText.setText("Dazwischen");
                this.rightText.setText("Nicht\nDazwischen");
                break;
            case BETWEEN_NOT_BETWEEN:
                this.gameState = BusfahrenState.SAME_NOT_SAME;
                this.taskText.setText("Ist die nächste Karte die selbe, wie die Letze?");
                this.leftButton.setImageResource(R.drawable.busfahren_again);
                this.rightButton.setImageResource(R.drawable.busfahren_not_again_button);
                this.leftText.setText("Gleich");
                this.rightText.setText("Nicht\nGleich");
                break;
            case SAME_NOT_SAME:
                this.gameState = BusfahrenState.ACE_NO_ACE;
                this.taskText.setText("Ist die nächste Karte ein Ass?");
                this.leftButton.setImageResource(R.drawable.busfahren_ace_button);
                this.rightButton.setImageResource(R.drawable.busfahren_no_ace_button);
                this.leftText.setText("Ass");
                this.rightText.setText("Kein\nAss");
                break;
            case ACE_NO_ACE:
                leaveGame();
                break;
        }
        buttonsClickable = true;
    }

    private void leaveGame() {
        Intent intent;
        if (fromMenue) {
            intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
            intent.putExtra("lastGame", MiniGame.BUSFAHREN);
        } else {
            intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
        }
        this.startActivity(intent);
    }

    private void startTimer(final boolean correctAnswer) {
        final Handler mHandler = new Handler();
        if (!correctAnswer) {
            this.plusText.setVisibility(View.VISIBLE);
            switch (this.gameState) {
                case RED_BLACK:
                    drinkCount += 1;
                    this.plusText.setText("+1");
                    break;
                case HIGHER_LOWER:
                    drinkCount += 2;
                    this.plusText.setText("+2");
                    break;
                case BETWEEN_NOT_BETWEEN:
                    drinkCount += 3;
                    this.plusText.setText("+3");
                    break;
                case SAME_NOT_SAME:
                    drinkCount += 4;
                    this.plusText.setText("+4");
                    break;
                case ACE_NO_ACE:
                    drinkCount += 5;
                    this.plusText.setText("+5");
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
                        plusText.setVisibility(View.GONE);
                        plusText.setTextSize(30);
                        restart();
                    }
                } else {
                    if (!correctAnswer) {
                        plusText.setTextSize(30 - counter * 10 / 4);
                    }
                }
                counter++;
                mHandler.postDelayed(this, 100);
            }
        }, 250);
    }

    private void restart() {
        drinkCounterText.setText("" + drinkCount);
        initCards();
        this.leftButton.setImageResource(R.drawable.busfahren_red_button);
        this.rightButton.setImageResource(R.drawable.busfahren_balck_button);
        this.leftText.setText("Rot");
        this.rightText.setText("Schwarz");
        for (ImageView i : cardImages) {
            i.setImageResource(R.drawable.rueckseite);
        }
        this.gameState = BusfahrenState.RED_BLACK;
        buttonsClickable = true;
    }

    private boolean checkLeftButton() {
        switch (this.gameState) {
            case RED_BLACK:
                this.cardImages[0].setImageResource(cards[0].getImageId());
                return cards[0].isRed();
            case HIGHER_LOWER:
                this.cardImages[1].setImageResource(cards[1].getImageId());
                return cards[1].isHigherAs(cards[0]);
            case BETWEEN_NOT_BETWEEN:
                this.cardImages[2].setImageResource(cards[2].getImageId());
                return cards[2].isBetween(cards[0], cards[1]);
            case SAME_NOT_SAME:
                this.cardImages[3].setImageResource(cards[3].getImageId());
                return cards[3].getValue() == cards[2].getValue();
            case ACE_NO_ACE:
                this.cardImages[4].setImageResource(cards[4].getImageId());
                return cards[4].getValue() == CardValue.ACE;
        }
        return false;
    }

    public boolean checkRightButton() {
        if(this.gameState == BusfahrenState.HIGHER_LOWER){
            if(this.cards[1].getValueAsInt() == this.cards[0].getValueAsInt()){
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
