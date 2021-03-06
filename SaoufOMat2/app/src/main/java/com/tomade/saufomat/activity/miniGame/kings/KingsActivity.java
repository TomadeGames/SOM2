package com.tomade.saufomat.activity.miniGame.kings;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tomade.saufomat.DrinkHelper;
import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.miniGame.BaseMiniGameActivity;
import com.tomade.saufomat.activity.miniGame.BaseMiniGamePresenter;
import com.tomade.saufomat.model.card.Card;
import com.tomade.saufomat.view.CardImageView;

import java.util.ArrayList;
import java.util.List;

public class KingsActivity extends BaseMiniGameActivity<BaseMiniGamePresenter> {
    private CardImageView cardImage;
    private TextView popupText;
    private TextView cardCounterText;

    private int maximumCards = 32;
    private int cardCount = 0;

    private KingsState gameState = KingsState.START;

    private Card card;
    private List<Card> lastCards = new ArrayList<>();

    @Override
    protected void initPresenter() {
        this.presenter = new BaseMiniGamePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_kings);
        super.onCreate(savedInstanceState);


        if (this.presenter.isFromMainGame()) {
            this.maximumCards = this.presenter.getPlayerCount() * 3;
            if (this.maximumCards > TARGET_TURN_COUNT) {
                this.maximumCards = this.presenter.getPlayerCount() * 2;
            }
            if (this.maximumCards > TARGET_TURN_COUNT) {
                this.maximumCards = this.presenter.getPlayerCount();
            }
        }

        this.cardImage = this.findViewById(R.id.cardImage);
        this.popupText = this.findViewById(R.id.popupText);
        this.cardCounterText = this.findViewById(R.id.cardcounterText);
        this.cardCounterText.setText(this.cardCount + " / " + this.maximumCards);

        if (this.presenter.isFromMainGame()) {
            this.popupText.setText(this.getString(R.string.minigame_kings_tap_to_start, this.presenter
                    .getCurrentPlayer().getName()));
        }

        ImageButton tutorialButton = this.findViewById(R.id.tutorialButton);
        tutorialButton.setOnClickListener(this);
        ImageButton backButton = this.findViewById(R.id.backButton);
        View backLabel = this.findViewById(R.id.backText);
        if (!this.presenter.isFromMainGame()) {
            backButton.setOnClickListener(this);
        } else {
            backButton.setVisibility(View.GONE);
            backLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            switch (this.gameState) {
                case START:
                    this.getTask();
                    break;
                case ROUND_END:
                    if (!this.presenter.isFromMainGame()) {
                        this.popupText.setText(R.string.minigame_kings_next_player);
                        this.flipCardBack();
                    } else {
                        this.presenter.nextPlayer();
                        if (this.cardCount < this.maximumCards) {
                            this.popupText.setText(this.getString(R.string.minigame_kings_tap_to_start, this
                                    .presenter.getCurrentPlayer().getName()));
                            this.flipCardBack();
                        } else {
                            this.popupText.setText(R.string.minigame_kings_game_over);
                            this.gameState = KingsState.GAME_OVER;
                        }
                    }
                    break;
                case GAME_OVER:
                    this.presenter.leaveGame();
                    break;
            }
        }
        return true;
    }

    private void flipCardBack() {
        this.gameState = KingsState.ANIMATION;
        this.cardImage.flipCardBack(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                KingsActivity.this.gameState = KingsState.START;
            }
        });
    }

    private void flipCard() {
        this.gameState = KingsState.ANIMATION;
        this.cardImage.flipCard(this.card.getImageId(), new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setTaskText();
                KingsActivity.this.gameState = KingsState.ROUND_END;
            }
        });
    }

    private void setTaskText() {
        if (this.card.isRed()) {
            switch (this.card.getValue()) {
                case SEVEN:
                    this.popupText.setText(R.string.minigame_kings_card_red_seven);
                    break;
                case EIGHT:
                    this.popupText.setText(R.string.minigame_kings_card_red_eight);
                    break;
                case NINE:
                    this.popupText.setText(R.string.minigame_kings_card_red_nine);
                    break;
                case TEN:
                    this.popupText.setText(R.string.minigame_kings_card_red_ten);
                    break;
                case JACK:
                    this.popupText.setText(R.string.minigame_kings_card_red_jack);
                    break;
                case QUEEN:
                    this.popupText.setText(R.string.minigame_kings_card_red_queen);
                    break;
                case KING:
                    this.popupText.setText(R.string.minigame_kings_card_red_king);
                    break;
                case ACE:
                    this.popupText.setText(R.string.minigame_kings_card_red_ace);
                    DrinkHelper.increaseAll(3, this);
                    break;
                default:
                    break;
            }
        } else {
            switch (this.card.getValue()) {
                case SEVEN:
                    this.popupText.setText(R.string.minigame_kings_card_black_seven);
                    DrinkHelper.increaseCurrentPlayer(1, this);
                    break;
                case EIGHT:
                    this.popupText.setText(R.string.minigame_kings_card_black_eight);
                    DrinkHelper.increaseCurrentPlayer(2, this);
                    break;
                case NINE:
                    this.popupText.setText(R.string.minigame_kings_card_black_nine);
                    DrinkHelper.increaseCurrentPlayer(3, this);
                    break;
                case TEN:
                    this.popupText.setText(R.string.minigame_kings_card_black_ten);
                    DrinkHelper.increaseCurrentPlayer(4, this);
                    break;
                case JACK:
                    this.popupText.setText(R.string.minigame_kings_card_black_jack);
                    DrinkHelper.increaseMen(1, this);
                    break;
                case QUEEN:
                    this.popupText.setText(R.string.minigame_kings_card_black_queen);
                    DrinkHelper.increaseWomen(1, this);
                    break;
                case KING:
                    this.popupText.setText(R.string.minigame_kings_card_black_king);
                    break;
                case ACE:
                    this.popupText.setText(R.string.minigame_kings_card_black_ace);
                    DrinkHelper.increaseAll(1, this);
                    break;
                default:
                    break;
            }
        }
    }

    private void getTask() {
        boolean validCard = false;
        if (this.lastCards.size() >= 32) {
            this.cardCount = 0;
            this.lastCards.clear();
        }
        while (!validCard) {
            validCard = true;
            this.card = Card.getRandomCard7OrHigher();
            for (Card c : this.lastCards) {
                if (this.card.equals(c)) {
                    validCard = false;
                }
            }
        }
        this.lastCards.add(this.card);
        this.flipCard();

        this.cardCount = this.cardCount + 1;
        this.cardCounterText.setText(this.cardCount + " / " + this.maximumCards);
    }
}
