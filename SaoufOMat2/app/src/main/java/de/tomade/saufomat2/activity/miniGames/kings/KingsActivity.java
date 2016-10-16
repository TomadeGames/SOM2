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
import de.tomade.saufomat2.activity.miniGames.MiniGame;
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
                    this.setMaximumCards(2 * this.playerList.size());
                } else {
                    this.setMaximumCards(3 * this.playerList.size());
                }
            }
        }
        this.cardImage = (ImageView) this.findViewById(R.id.cardImage);
        this.bottomLayout = (RelativeLayout) this.findViewById(R.id.popupPanel);
        this.setPopupText((TextView) this.findViewById(R.id.popupText));
        this.cardCounterText = (TextView) this.findViewById(R.id.cardcounterText);
        this.cardCounterText.setText(getCardCount() + " / " + this.getMaximumCards());

        if (!this.fromMenue) {
            this.getPopupText().setText(Player.getPlayerById(this.playerList, this.currentPlayerId).getName() + "\nTippen um die Karte aufzudecken");
        }

        this.lastText = this.getPopupText().getText().toString();

        ImageButton tutorialButton = (ImageButton) this.findViewById(R.id.tutorialButton);
        tutorialButton.setOnClickListener(this);
        ImageButton backButton = (ImageButton) this.findViewById(R.id.backButton);
        View backLabel = this.findViewById(R.id.backText);
        if (this.fromMenue) {
            backButton.setOnClickListener(this);
        } else {
            backButton.setVisibility(View.GONE);
            backLabel.setVisibility(View.GONE);
            this.setMaximumCards(32);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            if (isTutorialShown()) {
                hideTutorial();
            } else {
                switch (this.getGameState()) {
                    case START:
                        this.setGameState(KingsState.ROUND_END);
                        getTask();
                        break;
                    case ROUND_END:
                        if (this.fromMenue) {
                            this.getPopupText().setText("Nächster Spieler");
                        } else {
                            this.currentPlayerId = Player.getPlayerById(this.playerList, this.currentPlayerId).getNextPlayerId();
                            if (getCardCount() < getMaximumCards()) {
                                this.getPopupText().setText(Player.getPlayerById(this.playerList, this.currentPlayerId).getName() + "\nTippen um die Karte aufzudecken");
                            } else {
                                this.getPopupText().setText("Spiel vorbei");
                                this.setGameState(KingsState.GAME_OVER);
                            }
                        }
                        this.setGameState(KingsState.START);
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
                if (this.isTutorialShown()) {
                    hideTutorial();
                } else {
                    showTutorial();
                }
                break;
        }
    }

    private void showTutorial() {
        if (!this.isTutorialShown()) {
            this.setTutorialShown(true);
            this.lastText = this.getPopupText().getText().toString();
            this.getPopupText().setText(R.string.kings_tutorial);
        }
    }

    private void hideTutorial() {
        if (this.isTutorialShown()) {
            this.setTutorialShown(false);
            this.getPopupText().setText(this.lastText);
        }
    }

    private void getTask() {
        boolean validCard = false;
        if (getLastCards().size() >= 32) {
            this.setCardCount(0);
            getLastCards().clear();
        }
        while (!validCard) {
            validCard = true;
            setCard(Card.getRandomCard7OrHigher());
            for (Card c : this.getLastCards()) {
                if (getCard().equals(c)) {
                    validCard = false;
                }
            }
        }
        this.getLastCards().add(getCard());
        this.cardImage.setImageResource(getCard().getImageId());
        switch (getCard().getValue()) {
            case SEVEN:
                this.getPopupText().setText("7\nDein linker Nachbar muss einen Trinken!");
                break;
            case EIGHT:
                this.getPopupText().setText("8\nDein rechter Nachbar muss einen Trinken!");
                break;
            case NINE:
                this.getPopupText().setText("9\nDu darfst dir jemanden aussuchen, der trinken muss!");
                break;
            case TEN:
                this.getPopupText().setText("10\nDiesem Spieler dürfen keine Fragen beantwortet werden!");

                break;
            case JACK:
                this.getPopupText().setText("Bube\nAlle Männer müssen trinken!");
                if (!this.fromMenue) {
                    for (Player p : playerList) {
                        if (p.getIsMan()) {
                            p.increaseDrinks(1);
                        }
                    }
                }
                break;
            case QUEEN:
                this.getPopupText().setText("Dame\nAlle Frauen müssen trinken!");
                if (!this.fromMenue) {
                    for (Player p : playerList) {
                        if (!p.getIsMan()) {
                            p.increaseDrinks(1);
                        }
                    }
                }
                break;
            case KING:
                this.getPopupText().setText("König\nDu darfst dir eine Regel ausdenken, die für dieses Spiel bestehen bleibt!");
                break;
            case ACE:
                this.getPopupText().setText("Ass\nAlle müssen trinken!");
                if (!this.fromMenue) {
                    for (Player p : playerList) {
                        p.increaseDrinks(1);
                    }
                }
                break;
        }
        this.setCardCount(this.getCardCount() + 1);
        this.cardCounterText.setText(this.getCardCount() + " / " + this.getMaximumCards());
    }

    public TextView getPopupText() {
        return popupText;
    }

    public void setPopupText(TextView popupText) {
        this.popupText = popupText;
    }

    public int getMaximumCards() {
        return maximumCards;
    }

    public void setMaximumCards(int maximumCards) {
        this.maximumCards = maximumCards;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public boolean isTutorialShown() {
        return tutorialShown;
    }

    public void setTutorialShown(boolean tutorialShown) {
        this.tutorialShown = tutorialShown;
    }

    public KingsState getGameState() {
        return gameState;
    }

    public void setGameState(KingsState gameState) {
        this.gameState = gameState;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public List<Card> getLastCards() {
        return lastCards;
    }

    public void setLastCards(List<Card> lastCards) {
        this.lastCards = lastCards;
    }
}
