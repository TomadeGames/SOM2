package de.tomade.saufomat2.activity.miniGames.kings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.ChooseMiniGameActivity;
import de.tomade.saufomat2.activity.mainGame.MainGameActivity;
import de.tomade.saufomat2.model.MiniGame;
import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.model.card.Card;

public class KingsActivity extends Activity implements View.OnClickListener {
    private ImageView cardImage;
    private RelativeLayout bottomLayout;
    private TextView popupText;
    private TextView cardCounterText;

    private ArrayList<Player> playerList;
    private int currentPlayerId;
    private int maximumCards;
    private int cardCount = 0;

    private boolean fromMenue = false;
    private boolean tutorialShown = false;
    private KingsState gameState = KingsState.START;

    private Card card;
    private List<Card> lastCards = new ArrayList<>();

    private String lastText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kings);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.fromMenue = extras.getBoolean("fromMenue");
            if (!this.fromMenue) {
                this.playerList = extras.getParcelableArrayList("player");
                this.currentPlayerId = extras.getInt("currentPlayerId");
                if (3 * this.playerList.size() > 20) {
                    this.maximumCards = 2 * this.playerList.size();
                } else {
                    this.maximumCards = 3 * this.playerList.size();
                }
            }
        }
        this.cardImage = (ImageView) this.findViewById(R.id.cardImage);
        this.bottomLayout = (RelativeLayout) this.findViewById(R.id.popupPanel);
        this.popupText = (TextView) this.findViewById(R.id.popupText);
        this.cardCounterText = (TextView) this.findViewById(R.id.cardcounterText);
        this.cardCounterText.setText(cardCount + " / " + this.maximumCards);

        if (!this.fromMenue) {
            this.popupText.setText(Player.getPlayerById(this.playerList, this.currentPlayerId).getName() + "\nTippen um die Karte aufzudecken");
        }

        this.lastText = this.popupText.getText().toString();

        ImageButton tutorialButton = (ImageButton) this.findViewById(R.id.tutorialButton);
        tutorialButton.setOnClickListener(this);
        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);
        if (this.fromMenue) {
            backButton.setOnClickListener(this);
        } else {
            backButton.setVisibility(View.GONE);
            this.maximumCards = 32;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            if (tutorialShown) {
                hideTutorial();
            } else {
                switch (this.gameState) {
                    case START:
                        this.gameState = KingsState.ROUND_END;
                        getTask();
                        break;
                    case ROUND_END:
                        if (this.fromMenue) {
                            this.popupText.setText("Nächster Spieler");
                        } else {
                            this.currentPlayerId = Player.getPlayerById(this.playerList, this.currentPlayerId).getNextPlayerId();
                            if (cardCount < maximumCards) {
                                this.popupText.setText(Player.getPlayerById(this.playerList, this.currentPlayerId).getName() + "\nTippen um die Karte aufzudecken");
                            } else {
                                this.popupText.setText("Spiel vorbei");
                                this.gameState = KingsState.GAME_OVER;
                            }
                        }
                        this.gameState = KingsState.START;
                        break;
                    case GAME_OVER:
                        leaveGame();
                        break;
                }
            }
        }
        return true;
    }

    private void leaveGame() {
        Intent intent;
        if (this.fromMenue) {
            intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
            intent.putExtra("lastGame", MiniGame.KINGS);
        } else {
            intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
            intent.putParcelableArrayListExtra("player", this.playerList);
            intent.putExtra("currentPlayerId", this.currentPlayerId);
        }
        this.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                Intent intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
                intent.putExtra("lastGame", MiniGame.KINGS);
                this.startActivity(intent);
                break;
            case R.id.tutorialButton:
                if (this.tutorialShown) {
                    hideTutorial();
                } else {
                    showTutorial();
                }
                break;
        }
    }

    private void showTutorial() {
        if (!this.tutorialShown) {
            this.tutorialShown = true;
            this.lastText = this.popupText.getText().toString();
            this.popupText.setText("Es werden der Reihe nach Karten mit verschiedenen Aufgaben aufgedeckt. Das Spiel endet, wenn jeder Spieler drei Karten hatte.");
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
        if (lastCards.size() >= 32) {
            this.cardCount = 0;
            lastCards.clear();
        }
        while (!validCard) {
            validCard = true;
            card = Card.getRandomCard7OrHigher();
            for (Card c : this.lastCards) {
                if (card.equals(c)) {
                    validCard = false;
                }
            }
        }
        this.lastCards.add(card);
        this.cardImage.setImageResource(card.getImageId());
        switch (card.getValue()) {
            case SEVEN:
                this.popupText.setText("7\nDein linker Nachbar muss einen Trinken!");
                break;
            case EIGHT:
                this.popupText.setText("8\nDein rechter Nachbar muss einen Trinken!");
                break;
            case NINE:
                this.popupText.setText("9\nDu darfst dir jemanden aussuchen, der trinken muss!");
                break;
            case TEN:
                this.popupText.setText("10\nDiesem Spieler dürfen keine Fragen beantwortet werden!");

                break;
            case JACK:
                this.popupText.setText("Bube\nAlle Männer müssen trinken!");
                if (!this.fromMenue) {
                    for (Player p : playerList) {
                        if (p.getIsMan()) {
                            p.increaseDrinks(1);
                        }
                    }
                }
                break;
            case QUEEN:
                this.popupText.setText("Dame\nAlle Frauen müssen trinken!");
                if (!this.fromMenue) {
                    for (Player p : playerList) {
                        if (!p.getIsMan()) {
                            p.increaseDrinks(1);
                        }
                    }
                }
                break;
            case KING:
                this.popupText.setText("König\nDu darfst dir eine Regel ausdenken, die für dieses Spiel bestehen bleibt!");
                break;
            case ACE:
                this.popupText.setText("Ass\nAlle müssen trinken!");
                if (!this.fromMenue) {
                    for (Player p : playerList) {
                        p.increaseDrinks(1);
                    }
                }
                break;
        }
        this.cardCount++;
        this.cardCounterText.setText(this.cardCount + " / " + this.maximumCards);
    }
}
