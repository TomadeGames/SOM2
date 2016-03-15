package de.tomade.saufomat2.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by markk on 09.03.2016.
 */
public class Player implements Parcelable {
    private static int nextId = 0;
    private int id;
    private String name;
    private int weight;
    private String gender;
    private int drinks = 0;
    private int nextPlayerId;
    private int lastPlayerId;

    public Player(){
        this.id = this.nextId;
        this.nextId++;
    };

    public Player(String name, int weight, String gender, int drinks, int nextPlayerId, int lastPlayerId){
        this();
        this.name = name;
        this.weight = weight;
        this.gender = gender;
        this.drinks = drinks;
        this.nextPlayerId = nextPlayerId;
        this.lastPlayerId = lastPlayerId;
    }

    private Player(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.weight = in.readInt();
        this.gender = in.readString();
        this.drinks = in.readInt();
    }

    public int getId(){return this.id; }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getWeight(){
        return this.weight;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public String getGender(){
        return this.gender;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public int getDrinks(){
        return this.drinks;
    }

    public void setDrinks(int drinks){
        this.drinks = drinks;
    }

    public void increaseDrinks(int increase){this.drinks += increase; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.weight);
        dest.writeString(this.gender);
        dest.writeInt(this.drinks);
    }
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

    public int getNextPlayerId() {
        return nextPlayerId;
    }

    public void setNextPlayerId(int nextPlayerId) {
        this.nextPlayerId = nextPlayerId;
    }

    public int getLastPlayerId() {
        return lastPlayerId;
    }

    public void setLastPlayerId(int lastPlayerId) {
        this.lastPlayerId = lastPlayerId;
    }
}
