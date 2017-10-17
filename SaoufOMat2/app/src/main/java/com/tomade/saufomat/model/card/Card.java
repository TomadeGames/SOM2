package com.tomade.saufomat.model.card;

import com.tomade.saufomat.R;

import java.util.Random;

/**
 * Eine Spielkarte
 * Created by woors on 22.03.2016.
 */
public class Card {
    private static Random random = new Random();
    private CardValue value;
    private CardColor color;
    private int imageId;


    public Card(CardColor color, CardValue value) {
        this.color = color;
        this.value = value;
        this.initImage();
    }

    public Card(int color, int value) {
        this.setColor(color);
        this.setValue(value);
        this.initImage();
    }

    public Card(CardColor color, int value) {
        this.setColor(color);
        this.setValue(value);
        this.initImage();
    }

    public Card(int color, CardValue value) {
        this.setColor(color);
        this.setValue(value);
        this.initImage();
    }

    /**
     * Gibt eine zufällige Spielkarte zurück
     *
     * @return eine zufällige Karte
     */
    public static Card getRandomCard() {
        int color = random.nextInt(4);
        int value = random.nextInt(13);
        return new Card(color, value);
    }

    /**
     * Gibt eine Zufällge Spielkarte zwischen 7 und Ass zurück
     *
     * @return eine zufällige Karte zwischen 7 und Ass
     */
    public static Card getRandomCard7OrHigher() {
        int color = random.nextInt(4);
        int value = random.nextInt(8) + 5;
        return new Card(color, value);
    }

    /**
     * Gibt true zurück, wenn die Spielkarte rot ist
     *
     * @return ture, wenn die Karte rot ist. Sonst false
     */
    public boolean isRed() {
        return this.color == CardColor.DIAMOND
                || this.color == CardColor.HEART;
    }

    /**
     * Gibt true zurück, wenn der Kartenwert höher ist als der Kartenwert der übergebenen Karte.
     *
     * @param card die Karte mit der diese Verglichen wird
     * @return true, wenn der Kartenwert höher ist
     */
    public boolean isHigherAs(Card card) {
        return this.getValueAsInt() > card.getValueAsInt();
    }

    /**
     * Gibt true zurück, wenn der Kartenwert zwischen den Kartenwerten der beiden übergebenen Karten liegt.
     *
     * @param card1 die erste Karte
     * @param card2 die zweite Karte
     * @return true, wenn der Kartenwert zwischen den übergeben ist.
     */
    public boolean isBetween(Card card1, Card card2) {
        Card lowerCard;
        Card higherCard;
        if (card1.getValueAsInt() < card2.getValueAsInt()) {
            lowerCard = card1;
            higherCard = card2;
        } else {
            lowerCard = card2;
            higherCard = card1;
        }
        return lowerCard.getValueAsInt() < this.getValueAsInt()
                && this.getValueAsInt() < higherCard.getValueAsInt();
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == Card.class) {
            Card c = (Card) o;
            if (c.getValue() == this.getValue()
                    && c.getColor() == this.getColor()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gibt den Kartenwert der Karte zurück
     *
     * @return der Kartenwert der Karte
     */
    public CardValue getValue() {
        return this.value;
    }

    /**
     * Setzt den Kartenwert der Karte
     *
     * @param value der Kartenwert der Karte
     */
    public void setValue(CardValue value) {
        this.value = value;
    }

    /**
     * Setzt den Kartenwert der Karte
     *
     * @param value der Kartenwert der Karte als Zahl (0 = 2, 1 = 3, ...,  12 = Ass)
     * @throws IllegalArgumentException wird geworfen, wenn der übergebene Kartenwert nicht zwischen 0 und 12 lag.
     */
    public void setValue(int value) throws IllegalArgumentException {
        switch (value) {
            case 0:
                this.setValue(CardValue.TWO);
                break;
            case 1:
                this.setValue(CardValue.THREE);
                break;
            case 2:
                this.setValue(CardValue.FOUR);
                break;
            case 3:
                this.setValue(CardValue.FIFE);
                break;
            case 4:
                this.setValue(CardValue.SIX);
                break;
            case 5:
                this.setValue(CardValue.SEVEN);
                break;
            case 6:
                this.setValue(CardValue.EIGHT);
                break;
            case 7:
                this.setValue(CardValue.NINE);
                break;
            case 8:
                this.setValue(CardValue.TEN);
                break;
            case 9:
                this.setValue(CardValue.JACK);
                break;
            case 10:
                this.setValue(CardValue.QUEEN);
                break;
            case 11:
                this.setValue(CardValue.KING);
                break;
            case 12:
                this.setValue(CardValue.ACE);
                break;
            default:
                throw new IllegalArgumentException("Der Kartenwert muss zwischen 0 und 12 liegen");
        }
    }

    /**
     * Gibt den Kartenwert als Zahl zurück
     *
     * @return der Kartenwert als Zahl (0 = 2, 1 = 3, ..., 12 = Ass)
     */
    public int getValueAsInt() {
        switch (this.value) {
            case TWO:
                return 0;
            case THREE:
                return 1;
            case FOUR:
                return 2;
            case FIFE:
                return 3;
            case SIX:
                return 4;
            case SEVEN:
                return 5;
            case EIGHT:
                return 6;
            case NINE:
                return 7;
            case TEN:
                return 8;
            case JACK:
                return 9;
            case QUEEN:
                return 10;
            case KING:
                return 11;
            case ACE:
                return 12;
        }
        return -1;
    }

    /**
     * Gibt die Farbe der Karte zurück
     *
     * @return die Farbe der Karte
     */
    public CardColor getColor() {
        return this.color;
    }

    /**
     * Setzt die Farbe der Karte
     *
     * @param color die Farbe der Karte
     */
    public void setColor(CardColor color) {
        this.color = color;
    }

    /**
     * Setzt die Farbe der Karte
     *
     * @param color die Farbe der Karte als Zahl (0 = Karo, 1 = Herz, 2 = Kreuz, 3 = Pik)
     * @throws IllegalArgumentException wird geworfen, wenn die Farbe nicht zwichen 0 und 3 liegt
     */
    public void setColor(int color) throws IllegalArgumentException {
        switch (color) {
            case 0:
                this.setColor(CardColor.DIAMOND);
                break;
            case 1:
                this.setColor(CardColor.HEART);
                break;
            case 2:
                this.setColor(CardColor.CLUB);
                break;
            case 3:
                this.setColor(CardColor.SPADE);
                break;
            default:
                throw new IllegalArgumentException("Der Kartenfarbe muss zwischen 0 und 3 liegen");
        }

    }

    /**
     * Gibt die Id zum Bild der Karte zurück
     *
     * @return die Id zum Bild
     */
    public int getImageId() {
        return this.imageId;
    }

    private void initImage() {
        switch (this.getValue()) {
            case ACE:
                switch (this.getColor()) {
                    case DIAMOND:
                        this.imageId = R.drawable.karo_ass;
                        break;
                    case HEART:
                        this.imageId = R.drawable.herz_ass;
                        break;
                    case CLUB:
                        this.imageId = R.drawable.kreuz_ass;
                        break;
                    case SPADE:
                        this.imageId = R.drawable.pik_ass;
                        break;
                }
                break;
            case TWO:
                switch (this.getColor()) {
                    case DIAMOND:
                        this.imageId = R.drawable.karo2;
                        break;
                    case HEART:
                        this.imageId = R.drawable.herz2;
                        break;
                    case CLUB:
                        this.imageId = R.drawable.kreuz2;
                        break;
                    case SPADE:
                        this.imageId = R.drawable.pik2;
                        break;
                }
                break;
            case THREE:
                switch (this.getColor()) {
                    case DIAMOND:
                        this.imageId = R.drawable.karo3;
                        break;
                    case HEART:
                        this.imageId = R.drawable.herz3;
                        break;
                    case CLUB:
                        this.imageId = R.drawable.kreuz3;
                        break;
                    case SPADE:
                        this.imageId = R.drawable.pik3;
                        break;
                }
                break;
            case FOUR:
                switch (this.getColor()) {
                    case DIAMOND:
                        this.imageId = R.drawable.karo4;
                        break;
                    case HEART:
                        this.imageId = R.drawable.herz4;
                        break;
                    case CLUB:
                        this.imageId = R.drawable.kreuz4;
                        break;
                    case SPADE:
                        this.imageId = R.drawable.pik4;
                        break;
                }
                break;
            case FIFE:
                switch (this.getColor()) {
                    case DIAMOND:
                        this.imageId = R.drawable.karo5;
                        break;
                    case HEART:
                        this.imageId = R.drawable.herz5;
                        break;
                    case CLUB:
                        this.imageId = R.drawable.kreuz5;
                        break;
                    case SPADE:
                        this.imageId = R.drawable.pik5;
                        break;
                }
                break;
            case SIX:
                switch (this.getColor()) {
                    case DIAMOND:
                        this.imageId = R.drawable.karo6;
                        break;
                    case HEART:
                        this.imageId = R.drawable.herz6;
                        break;
                    case CLUB:
                        this.imageId = R.drawable.kreuz6;
                        break;
                    case SPADE:
                        this.imageId = R.drawable.pik6;
                        break;
                }
                break;
            case SEVEN:
                switch (this.getColor()) {
                    case DIAMOND:
                        this.imageId = R.drawable.karo7;
                        break;
                    case HEART:
                        this.imageId = R.drawable.herz7;
                        break;
                    case CLUB:
                        this.imageId = R.drawable.kreuz7;
                        break;
                    case SPADE:
                        this.imageId = R.drawable.pik7;
                        break;
                }
                break;
            case EIGHT:
                switch (this.getColor()) {
                    case DIAMOND:
                        this.imageId = R.drawable.karo8;
                        break;
                    case HEART:
                        this.imageId = R.drawable.herz8;
                        break;
                    case CLUB:
                        this.imageId = R.drawable.kreuz8;
                        break;
                    case SPADE:
                        this.imageId = R.drawable.pik8;
                        break;
                }
                break;
            case NINE:
                switch (this.getColor()) {
                    case DIAMOND:
                        this.imageId = R.drawable.karo9;
                        break;
                    case HEART:
                        this.imageId = R.drawable.herz9;
                        break;
                    case CLUB:
                        this.imageId = R.drawable.kreuz9;
                        break;
                    case SPADE:
                        this.imageId = R.drawable.pik9;
                        break;
                }
                break;
            case TEN:
                switch (this.getColor()) {
                    case DIAMOND:
                        this.imageId = R.drawable.karo10;
                        break;
                    case HEART:
                        this.imageId = R.drawable.herz10;
                        break;
                    case CLUB:
                        this.imageId = R.drawable.kreuz10;
                        break;
                    case SPADE:
                        this.imageId = R.drawable.pik10;
                        break;
                }
                break;
            case JACK:
                switch (this.getColor()) {
                    case DIAMOND:
                        this.imageId = R.drawable.karo_bube;
                        break;
                    case HEART:
                        this.imageId = R.drawable.herz_bube;
                        break;
                    case CLUB:
                        this.imageId = R.drawable.kreuz_bube;
                        break;
                    case SPADE:
                        this.imageId = R.drawable.pik_bube;
                        break;
                }
                break;
            case QUEEN:
                switch (this.getColor()) {
                    case DIAMOND:
                        this.imageId = R.drawable.karo_dame;
                        break;
                    case HEART:
                        this.imageId = R.drawable.herz_dame;
                        break;
                    case CLUB:
                        this.imageId = R.drawable.kreuz_dame;
                        break;
                    case SPADE:
                        this.imageId = R.drawable.pik_dame;
                        break;
                }
                break;
            case KING:
                switch (this.getColor()) {
                    case DIAMOND:
                        this.imageId = R.drawable.karo_koenig;
                        break;
                    case HEART:
                        this.imageId = R.drawable.herz_koenig;
                        break;
                    case CLUB:
                        this.imageId = R.drawable.kreuz_koenig;
                        break;
                    case SPADE:
                        this.imageId = R.drawable.pik_koenig;
                        break;
                }
                break;
        }
    }


    @Override
    public String toString() {
        return "Card: " + " " + this.getColor() + this.getValue();
    }
}
