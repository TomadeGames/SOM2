package de.tomade.saoufomat2.model.task;

/**
 * Created by woors on 10.03.2016.
 */
public class Task {
    private String text;
    private TaskDifficult difficult;
    private int drinkCount;     //Getränkeanzahl bei Ja
    private int cost;           //Getränkeanzahl bei Nein
    private TaskTarget target;

    public Task(String text, TaskDifficult difficult, int drinkCount, int cost, TaskTarget target){
        this.text = text;
        this.difficult = difficult;
        this.drinkCount = drinkCount;
        this.cost = cost;
        this.target = target;
    }

    public String getText() {
        return text;
    }

    public TaskDifficult getDifficult() {
        return difficult;
    }

    public int getDrinkCount() {
        return drinkCount;
    }

    public int getCost() {
        return cost;
    }

    public TaskTarget getTarget() {
        return target;
    }
}
