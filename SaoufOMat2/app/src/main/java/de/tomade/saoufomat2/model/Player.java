package de.tomade.saoufomat2.model;

/**
 * Created by markk on 09.03.2016.
 */
public class Player {
    String name;
    int weight;
    String gender;
    int drinks = 0;

    public Player(){};

    public Player(String name, int weight, String gender, int drinks){
        this.name = name;
        this.weight = weight;
        this.gender = gender;
        this.drinks = drinks;
    }

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
}
