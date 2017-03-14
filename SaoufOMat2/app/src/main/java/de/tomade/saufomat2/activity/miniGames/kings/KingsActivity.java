package de.tomade.saufomat2.activity.miniGames.kings;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.miniGames.BaseMiniGame;
import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.model.card.Card;

public class KingsActivity extends BaseMiniGame implements View.OnClickListener {
    private ImageView cardImage;
    private TextView popupText;
    private TextView cardCounterText;

    private int maximumCards;
    private int cardCount = 0;

    private boolean tutorialShown = false;
    private KingsState gameState = KingsState.START;

    private Card card;
    private List<Card> lastCards = new ArrayList<>();

    private String lastText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_kings);


        if (this.fromMainGame) {
            if (3 * this.playerList.size() > 20) {
                this.maximumCards = 2 * this.playerList.size();
            } else {
                this.maximumCards = 3 * this.playerList.size();
            }
            if (this.maximumCards > 32) {
                this.maximumCards = 32;
            }
        } else {
            this.maximumCards = 32;
        }

        this.cardImage = (ImageView) this.findViewById(R.id.cardImage);
        this.popupText = (TextView) this.findViewById(R.id.popupText);
        this.cardCounterText = (TextView) this.findViewById(R.id.cardcounterText);
        this.cardCounterText.setText(this.cardCount + " / " + this.maximumCards);

        if (this.fromMainGame) {
            this.popupText.setText(this.getString(R.string.minigame_kings_tap_to_start, this.currentPlayer.getName()));
        }

        this.lastText = this.popupText.getText().toString();

        ImageButton tutorialButton = (ImageButton) this.findViewById(R.id.tutorialButton);
        tutorialButton.setOnClickListener(this);
        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);
        View backLabel = this.findViewById(R.id.backText);
        if (!this.fromMainGame) {
            backButton.setOnClickListener(this);
        } else {
            backButton.setVisibility(View.GONE);
            backLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            if (this.tutorialShown) {
                this.hideTutorial();
            } else {
                switch (this.gameState) {
                    case START:
                        this.gameState = KingsState.ROUND_END;
                        this.getTask();
                        break;
                    case ROUND_END:
                        if (!this.fromMainGame) {
                            this.popupText.setText(R.string.minigame_kings_next_player);
                        } else {
                            this.nextTurn();
                            if (this.cardCount < this.maximumCards) {
                                this.popupText.setText(this.getString(R.string.minigame_kings_tap_to_start, this
                                        .currentPlayer.getName()));
                                this.gameState = KingsState.START;
                            } else {
                                this.popupText.setText(R.string.minigame_kings_game_over);
                                this.gameState = KingsState.GAME_OVER;
                            }
                        }
                        break;
                    case GAME_OVER:
                        this.leaveGame();
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                this.leaveGame();
                break;
            case R.id.tutorialButton:
                if (this.tutorialShown) {
                    this.hideTutorial();
                } else {
                    this.showTutorial();
                }
                break;
        }
    }

    private void showTutorial() {
        if (!this.tutorialShown) {
            this.tutorialShown = true;
            this.lastText = this.popupText.getText().toString();
            this.popupText.setText(R.string.minigame_kings_tutorial);
        }
    }

    private void hideTutorial() {
        if (this.tutorialShown) {
            this.tutorialShown = false;
            this.popupText.setText(this.lastText);
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
        this.cardImage.setImageResource(this.card.getImageId());
        switch (this.card.getValue()) {
            case SEVEN:
                this.popupText.setText(R.string.minigame_kings_card_value_seven);
                break;
            case EIGHT:
                this.popupText.setText(R.string.minigame_kings_card_value_eight);
                break;
            case NINE:
                this.popupText.setText(R.string.minigame_kings_card_value_nine);
                break;
            case TEN:
                this.popupText.setText(R.string.minigame_kings_card_value_ten);

                break;
            case JACK:
                this.popupText.setText(R.string.minigame_kings_card_value_jack);
                if (this.fromMainGame) {
                    for (Player player : this.playerList) {
                        if (player.getIsMan()) {
                            player.increaseDrinks(1);
                        }
                    }
                }
                break;
            case QUEEN:
                this.popupText.setText(R.string.minigame_kings_card_value_queen);
                if (this.fromMainGame) {
                    for (Player player : this.playerList) {
                        if (!player.getIsMan()) {
                            player.increaseDrinks(1);
                        }
                    }
                }
                break;
            case KING:
                this.popupText.setText(R.string.minigame_kings_card_value_king);
                break;
            case ACE:
                this.popupText.setText(R.string.minigame_kings_card_value_ace);
                if (this.fromMainGame) {
                    for (Player player : this.playerList) {
                        player.increaseDrinks(1);
                    }
                }
                break;
            default:
                break;
        }
        this.cardCount = this.cardCount + 1;
        this.cardCounterText.setText(this.cardCount + " / " + this.maximumCards);
    }
}
