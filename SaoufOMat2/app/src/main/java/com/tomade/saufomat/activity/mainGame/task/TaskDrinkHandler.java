package com.tomade.saufomat.activity.mainGame.task;

import android.util.Log;

import com.tomade.saufomat.DrinkHelper;
import com.tomade.saufomat.activity.ActivityWithPlayer;

/**
 * Hilfsklasse zur verteilung von Getränken bei bestimmten Aufgabenzielen
 * Created by woors on 11.10.2017.
 */

public class TaskDrinkHandler {
    private static final String TAG = TaskDrinkHandler.class.getSimpleName();

    /**
     * Erhöht den Getränkezähler für einen Task
     *
     * @param currentTask der aktuelle Task
     * @param source      die Acitivity, die diese Funktion aufruft
     */
    public static void increaseDrinks(SimpleTask currentTask, ActivityWithPlayer source) {
        switch (currentTask.getTaskTarget()) {
            case SELF:
                DrinkHelper.increaseCurrentPlayer(currentTask.getDrinkCount(), source);
                break;
            case NEIGHBOUR:
                DrinkHelper.increaseNeighbours(currentTask.getDrinkCount(), source.getCurrentPlayer(), source);
                break;
            case MEN:
                DrinkHelper.increaseMen(currentTask.getDrinkCount(), source);
                break;
            case WOMEN:
                DrinkHelper.increaseWomen(currentTask.getDrinkCount(), source);
                break;
            case NEIGHBOUR_LEFT:
                DrinkHelper.increaseLeft(currentTask.getDrinkCount(), source);
                break;
            case NEIGHBOUR_RIGHT:
                DrinkHelper.increaseRight(currentTask.getDrinkCount(), source);
                break;
            case ALL:
                DrinkHelper.increaseAll(currentTask.getDrinkCount(), source);
                break;
            case ALL_BUT_SELF:
                DrinkHelper.increaseAllButOnePlayer(currentTask.getDrinkCount(), source.getCurrentPlayer(), source);
                break;
            case SELF_AND_NEIGHBOURS:
                DrinkHelper.increasePlayerWithNeighbours(currentTask.getDrinkCount(), source.getCurrentPlayer(),
                        source);
                break;
            case SELF_AND_CHOOSE_ONE:
                DrinkHelper.increaseCurrentPlayer(currentTask.getDrinkCount(), source);
                break;
            default:
                Log.i(TAG, "TaskTarget " + currentTask.getTaskTarget() + " does not increase drinks");
                break;
        }
    }
}
