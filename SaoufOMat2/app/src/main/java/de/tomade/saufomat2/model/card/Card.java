package de.tomade.saufomat2.model.card;

import java.util.Random;

import de.tomade.saufomat2.R;

/**
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
        initImage();
    }

    public Card(int color, int value) {
        this.setColor(color);
        this.setValue(value);
        initImage();
    }

    public Card(CardColor color, int value) {
        this.setColor(color);
        this.setValue(value);
        initImage();
    }

    public Card(int color, CardValue value) {
        this.setColor(color);
        this.setValue(value);
        initImage();
    }

    public static Card getRandomCard() {
        int color = random.nextInt(4);
        int value = random.nextInt(13);
        Card card = new Card(color, value);
        return card;
    }

    public boolean isRed() {
        return this.color == CardColor.DIAMOND
                || this.color == CardColor.HEART;
    }

    public boolean isHigherAs(Card c) {
        if (this.getValue() == c.getValue()) {
            return false;
        }
        return this.getValueAsInt() > c.getValueAsInt();
    }

    public boolean isBetween(Card c0, Card c1) {
        Card lowerCard;
        Card higherCard;
        if (c0.getValueAsInt() < c1.getValueAsInt()) {
            lowerCard = c0;
            higherCard = c1;
        } else {
            lowerCard = c1;
            higherCard = c0;
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

    public CardValue getValue() {
        return value;
    }

    public void setValue(int value) {
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
        }
    }

    public void setValue(CardValue value) {
        this.value = value;
    }

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

    public CardColor getColor() {
        return color;
    }

    public void setColor(int color) {
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
        }
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
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
}
