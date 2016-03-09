package de.tomade.saoufomat2;

/**
 * Created by markk on 09.03.2016.
 */
public class Player {
    String name;
    int weight;
    String gender;

    public Player(){};

    public Player(String name, int weight, String gender){
        this.name = name;
        this.weight = weight;
        this.gender = gender;
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
}
