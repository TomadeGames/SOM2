package de.tomade.saufomat2.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.tomade.saufomat2.activity.mainGame.task.Task;
import de.tomade.saufomat2.activity.mainGame.task.TaskDefinitions;
import de.tomade.saufomat2.activity.mainGame.task.TaskDifficult;

/**
 * Erstellt Aufagben
 * Created by woors on 10.03.2016.
 */
public class TaskFactory {
    private List<Task> easyTasks;
    private List<Task> mediumTasks;
    private List<Task> hardTasks;
    private List<Task> easyWinTasks;
    private List<Task> mediumWinTasks;
    private List<Task> hardWinTasks;
    private List<Task> allTasks;

    public TaskFactory() {
        this.easyTasks = new ArrayList<>();
        this.mediumTasks = new ArrayList<>();
        this.hardTasks = new ArrayList<>();
        this.easyWinTasks = new ArrayList<>();
        this.mediumWinTasks = new ArrayList<>();
        this.hardWinTasks = new ArrayList<>();
        this.allTasks = TaskDefinitions.getTasks();

        this.refreshList(TaskDifficult.EASY);
        this.refreshList(TaskDifficult.MEDIUM);
        this.refreshList(TaskDifficult.HARD);
        this.refreshList(TaskDifficult.EASY_WIN);
        this.refreshList(TaskDifficult.MEDIUM_WIN);
        this.refreshList(TaskDifficult.HARD_WIN);
    }

    public TaskFactory(List<Task> loadedTasks) {
        this.allTasks = TaskDefinitions.getTasks();
        this.easyTasks = new ArrayList<>();
        this.mediumTasks = new ArrayList<>();
        this.hardTasks = new ArrayList<>();
        this.easyWinTasks = new ArrayList<>();
        this.mediumWinTasks = new ArrayList<>();
        this.hardWinTasks = new ArrayList<>();

        this.readSavedList(loadedTasks);
    }

    private void readSavedList(List<Task> loadedTasks) {
        for (Task task : loadedTasks) {
            if (!task.isAlreadyUsed()) {
                switch (task.getDifficult()) {
                    case EASY:
                        this.easyTasks.add(task);
                        break;
                    case MEDIUM:
                        this.mediumTasks.add(task);
                        break;
                    case HARD:
                        this.hardTasks.add(task);
                        break;
                    case EASY_WIN:
                        this.easyWinTasks.add(task);
                        break;
                    case MEDIUM_WIN:
                        this.mediumWinTasks.add(task);
                        break;
                    case HARD_WIN:
                        this.hardWinTasks.add(task);
                        break;
                }
            }
        }
    }

    private void refreshList(TaskDifficult difficult) {
        for (Task t : this.allTasks) {
            t.setAlreadyUsed(false);
            switch (t.getDifficult()) {
                case EASY:
                    if (difficult == TaskDifficult.EASY) {
                        this.easyTasks.add(t);
                    }
                    break;
                case MEDIUM:
                    if (difficult == TaskDifficult.MEDIUM) {
                        this.mediumTasks.add(t);
                    }
                    break;
                case HARD:
                    if (difficult == TaskDifficult.HARD) {
                        this.hardTasks.add(t);
                    }
                    break;
                case EASY_WIN:
                    if (difficult == TaskDifficult.EASY_WIN) {
                        this.easyWinTasks.add(t);
                    }
                    break;
                case MEDIUM_WIN:
                    if (difficult == TaskDifficult.MEDIUM_WIN) {
                        this.mediumWinTasks.add(t);
                    }
                    break;
                case HARD_WIN:
                    if (difficult == TaskDifficult.HARD_WIN) {
                        this.hardWinTasks.add(t);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public Task getTask(TaskDifficult difficult) {
        Random rnd = new Random();
        int index;
        Task task;
        switch (difficult) {
            case EASY:
                index = rnd.nextInt(this.easyTasks.size());
                task = this.easyTasks.get(index);
                break;
            case MEDIUM:
                index = rnd.nextInt(this.mediumTasks.size());
                task = this.mediumTasks.get(index);
                break;
            case HARD:
                index = rnd.nextInt(this.hardTasks.size());
                task = this.hardTasks.get(index);
                break;
            case EASY_WIN:
                index = rnd.nextInt(this.easyWinTasks.size());
                task = this.easyWinTasks.get(index);
                break;
            case MEDIUM_WIN:
                index = rnd.nextInt(this.mediumWinTasks.size());
                task = this.mediumWinTasks.get(index);
                break;
            case HARD_WIN:
                index = rnd.nextInt(this.hardWinTasks.size());
                task = this.hardWinTasks.get(index);
                break;
            default:
                throw new IllegalStateException("Difficult [" + difficult + "] does not exist");
        }
        task.setAlreadyUsed(true);
        return task;
    }

    public List<Task> getAllTasks() {
        return this.allTasks;
    }
}
