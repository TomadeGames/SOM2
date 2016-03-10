package de.tomade.saoufomat2.model.task;

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
    List<Task> allTasks;

    public TaskFactory(){
        easyTasks = new ArrayList<>();
        mediumTasks = new ArrayList<>();
        hardTasks = new ArrayList<>();
        allTasks = new ArrayList<>();
        initList();
        refreshList(TaskDifficult.EASY);
        refreshList(TaskDifficult.MEDIUM);
        refreshList(TaskDifficult.HARD);
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
            }
        }
    }

    private void initList(){
        allTasks.add(new Task("Trinke nichts", TaskDifficult.EASY, 0, 1, TaskTarget.SELF));
        allTasks.add(new Task("Trinke einen", TaskDifficult.EASY, 1, 0, TaskTarget.SELF));
        allTasks.add(new Task("Trinke zwei", TaskDifficult.MEDIUM, 2, 0, TaskTarget.SELF));
        allTasks.add(new Task("Trinke drei", TaskDifficult.HARD, 3, 0, TaskTarget.SELF));
        allTasks.add(new Task("Deine Nachbarn trinken einen", TaskDifficult.EASY, 1, 0, TaskTarget.NEIGHBOUR));
    }

    public Task getTask(TaskDifficult difficult){
        Random rnd = new Random();
        int index;
        switch (difficult){
            case EASY:
                index = rnd.nextInt(easyTasks.size()-1);
                return easyTasks.get(index);
            case MEDIUM:
                index = rnd.nextInt(mediumTasks.size()-1);
                return mediumTasks.get(index);
            case HARD:
                index = rnd.nextInt(hardTasks.size()-1);
                return hardTasks.get(index);
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
