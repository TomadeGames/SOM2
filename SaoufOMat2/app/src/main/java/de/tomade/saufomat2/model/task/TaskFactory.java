package de.tomade.saufomat2.model.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by woors on 10.03.2016.
 */
public class TaskFactory {
    List<Task> easyTasks;
    List<Task> mediumTasks;
    List<Task> hardTasks;
    List<Task> easyWinTasks;
    List<Task> mediumWinTasks;
    List<Task> hardWinTasks;
    List<Task> allTasks;

    public TaskFactory(){
        easyTasks = new ArrayList<>();
        mediumTasks = new ArrayList<>();
        hardTasks = new ArrayList<>();
        easyWinTasks = new ArrayList<>();
        mediumWinTasks = new ArrayList<>();
        hardWinTasks = new ArrayList<>();
        allTasks = new ArrayList<>();
        initList();
        refreshList(TaskDifficult.EASY);
        refreshList(TaskDifficult.MEDIUM);
        refreshList(TaskDifficult.HARD);
        refreshList(TaskDifficult.EASY_WIN);
        refreshList(TaskDifficult.MEDIUM_WIN);
        refreshList(TaskDifficult.HARD_WIN);
    }

    private void refreshList(TaskDifficult difficult){
        for(Task t: allTasks){
            switch (t.getDifficult()){
                case EASY:
                    if(difficult == TaskDifficult.EASY) {
                        easyTasks.add(t);
                    }
                    break;
                case MEDIUM:
                    if(difficult == TaskDifficult.MEDIUM) {
                        mediumTasks.add(t);
                    }
                    break;
                case HARD:
                    if(difficult == TaskDifficult.HARD) {
                        hardTasks.add(t);
                    }
                    break;
                case EASY_WIN:
                    if(difficult == TaskDifficult.EASY_WIN) {
                        easyWinTasks.add(t);
                    }
                    break;
                case MEDIUM_WIN:
                    if(difficult == TaskDifficult.MEDIUM_WIN) {
                        mediumWinTasks.add(t);
                    }
                    break;
                case HARD_WIN:
                    if(difficult == TaskDifficult.HARD_WIN) {
                        hardWinTasks.add(t);
                    }
                    break;
            }
        }
    }

    private void initList(){
        allTasks.add(new Task("Trinke nichts", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        allTasks.add(new Task("Trinke einen", TaskDifficult.EASY, 1, 0, TaskTarget.SELF));
        allTasks.add(new Task("Trinke zwei", TaskDifficult.MEDIUM, 2, 0, TaskTarget.SELF));
        allTasks.add(new Task("Trinke drei", TaskDifficult.HARD, 3, 0, TaskTarget.SELF));
        allTasks.add(new Task("Deine Nachbarn trinken einen", TaskDifficult.EASY, 1, 0, TaskTarget.NEIGHBOUR));
        allTasks.add(new Task("Deine Nachbarn trinken drei", TaskDifficult.EASY_WIN, 3, 0, TaskTarget.NEIGHBOUR));
        allTasks.add(new Task("Alle ausser dir trinken drei", TaskDifficult.MEDIUM_WIN, 3, 0 ,TaskTarget.ALL_BUT_SELF));
        allTasks.add(new Task("Suche drei Mitspieler aus, die einen Kurzen deiner Wahl trinken", TaskDifficult.HARD_WIN, 0, 0, TaskTarget.CHOOSE_THREE));
    }

    public Task getTask(TaskDifficult difficult){
        Random rnd = new Random();
        int index;
        switch (difficult){
            case EASY:
                index = rnd.nextInt(easyTasks.size());
                return easyTasks.get(index);
            case MEDIUM:
                index = rnd.nextInt(mediumTasks.size());
                return mediumTasks.get(index);
            case HARD:
                index = rnd.nextInt(hardTasks.size());
                return hardTasks.get(index);
            case EASY_WIN:
                index = rnd.nextInt(easyWinTasks.size());
                return easyWinTasks.get(index);
            case MEDIUM_WIN:
                index = rnd.nextInt(mediumWinTasks.size());
                return mediumWinTasks.get(index);
            case HARD_WIN:
                index = rnd.nextInt(hardWinTasks.size());
                return hardWinTasks.get(index);
        }
        return null;
    }

    private void removeTask(Task t){
        switch (t.getDifficult()){
            case EASY:
                easyTasks.remove(t);
                if(easyTasks.size() <= 1){
                    refreshList(TaskDifficult.EASY);
                }
                break;
            case MEDIUM:
                mediumTasks.remove(t);
                if(mediumTasks.size() <= 1){
                    refreshList(TaskDifficult.MEDIUM);
                }
                break;
            case HARD:
                hardTasks.remove(t);
                if(hardTasks.size() <= 1){
                    refreshList(TaskDifficult.HARD);
                }
                break;
        }
    }
}
