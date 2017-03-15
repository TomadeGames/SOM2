package de.tomade.saufomat2.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by markk on 09.03.2016.
 */
public class Player implements Parcelable {
    public static final Parcelable.Creator<Player> CREATOR =
            new Parcelable.Creator<Player>() {

                @Override
                public Player createFromParcel(Parcel source) {
                    return new Player(source);
                }

                @Override
                public Player[] newArray(int size) {
                    return new Player[size];
                }

            };
    private static int nextId = 0;
    private int id;
    private String name;
    private int weight;
    private boolean isMan;
    private int drinks = 0;
    private Player nextPlayer;
    private Player lastPlayer;
    private boolean hasNextPlayer = false;
    private boolean hasLastPlayer = false;

    public Player() {
        this.id = nextId;
        nextId++;
    }

    public Player(String name, int weight, boolean isMan, int drinks, Player nextPlayer, Player lastPlayer) {
        this();
        this.name = name;
        this.weight = weight;
        this.isMan = isMan;
        this.drinks = drinks;
        this.nextPlayer = nextPlayer;
        this.lastPlayer = lastPlayer;
    }

    private Player(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.weight = in.readInt();
        this.isMan = in.readByte() != 0;
        this.drinks = in.readInt();
        this.nextPlayer = (Player) in.readSerializable();
        this.lastPlayer = (Player) in.readSerializable();
        if (this.nextPlayer == null) {
            this.nextPlayer = this;
        }
        if (this.lastPlayer == null) {
            this.lastPlayer = this;
        }
    }

    @Nullable
    public static Player getPlayerById(List<Player> playerList, int id) {
        for (Player p : playerList) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean getIsMan() {
        return this.isMan;
    }

    public void setIsMan(boolean isMan) {
        this.isMan = isMan;
    }

    public int getDrinks() {
        return this.drinks;
    }

    public void setDrinks(int drinks) {
        this.drinks = drinks;
    }

    public void increaseDrinks(int increase) {
        this.drinks += increase;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.weight);
        dest.writeByte((byte) (this.isMan ? 1 : 0));
        dest.writeInt(this.drinks);
        if (this.nextPlayer != this) {
            dest.writeParcelable(this.nextPlayer, 0);
        } else {
            dest.writeParcelable(null, 0);
        }
        if (this.lastPlayer != this) {
            dest.writeParcelable(this.lastPlayer, 0);
        } else {
            dest.writeParcelable(null, 0);
        }
    }

    public Player getNextPlayer() {
        return this.nextPlayer;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.hasNextPlayer = true;
        this.nextPlayer = nextPlayer;
    }

    public Player getLastPlayer() {
        return this.lastPlayer;
    }

    public void setLastPlayer(Player lastPlayer) {
        this.hasLastPlayer = true;
        this.lastPlayer = lastPlayer;
    }

    public boolean getHasNextPlayer() {
        return this.hasNextPlayer;
    }

    public void setHasNextPlayer(boolean hasNextPlayer) {
        this.hasNextPlayer = hasNextPlayer;
    }

    public boolean getHastLastPlayer() {
        return this.hasLastPlayer;
    }

    public void setHasLastPlayer(boolean hasLastPlayer) {
        this.hasLastPlayer = hasLastPlayer;
    }

    public String toString() {
        return "Name: " + this.getName() + " Gewicht: " + this.getWeight() + " Mann?: " +
                this.getIsMan() + " ID: " + this.getId() + " LastPlayer: " + this.lastPlayer +
                " NextPlayer: " + this.nextPlayer + "\n" + " HasLastPlayerBoolean: " + this
                .getHastLastPlayer() +
                " HasNextPlayerBoolean: " + this.getHasNextPlayer();
    }
}
