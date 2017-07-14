package com.tomade.saufomat.activity.mainGame;

import com.tomade.saufomat.activity.mainGame.task.TaskDifficult;

import java.util.Random;

/**
 * Created by woors on 14.07.2017.
 */
class DifficultWithSaufOMeterEndFrame {
    private TaskDifficult difficult;
    private int saufOMeterEndFrame;

    public DifficultWithSaufOMeterEndFrame() {

    }

    public DifficultWithSaufOMeterEndFrame(TaskDifficult difficult, int saufOMeterEndFrame) {
        this.setDifficult(difficult);
        this.setSaufOMeterEndFrame(saufOMeterEndFrame);
    }

    public TaskDifficult getDifficult() {
        return this.difficult;
    }

    public void setDifficult(TaskDifficult difficult) {
        this.difficult = difficult;
    }

    public int getSaufOMeterEndFrame() {
        return this.saufOMeterEndFrame;
    }

    public void setSaufOMeterEndFrame(int saufOMeterEndFrame) {
        this.saufOMeterEndFrame = saufOMeterEndFrame;
    }
}

public class MainGameUtils {
    public static DifficultWithSaufOMeterEndFrame getCurrentDifficult(TaskDifficult leftDifficult, TaskDifficult
            middleDifficult, TaskDifficult rightDifficult) {

        float difficult = 0;
        int gameCount = 0;
        if (leftDifficult == middleDifficult && middleDifficult == rightDifficult) {
            switch (leftDifficult) {
                case EASY:
                    return new DifficultWithSaufOMeterEndFrame(TaskDifficult.EASY_WIN, 7);
                case MEDIUM:
                    return new DifficultWithSaufOMeterEndFrame(TaskDifficult.MEDIUM_WIN, 8);
                case HARD:
                    return new DifficultWithSaufOMeterEndFrame(TaskDifficult.HARD_WIN, 9);
                default:
                    break;
            }
        }
        TaskDifficult[] difficulties = {leftDifficult, middleDifficult, rightDifficult};
        for (int i = 0; i < 3; i++) {
            switch (difficulties[i]) {
                case EASY:
                    difficult += 0.2;
                    break;
                case MEDIUM:
                    difficult += 1.4;
                    break;
                case HARD:
                    difficult += 2.8;
                    break;
                case GAME:
                    gameCount++;
                    break;
            }
        }

        Random random = new Random(System.currentTimeMillis());
        difficult = ((difficult) / (3 - gameCount));
        int tmpDiff = (int) difficult;
        int saufOMeterEndFrame = 0;

        if (random.nextInt(3) < gameCount) {
            return new DifficultWithSaufOMeterEndFrame(TaskDifficult.GAME, 0);
        }
        if (difficult > 0.6) {
            saufOMeterEndFrame++;
        }
        if (difficult > 0.95) {
            saufOMeterEndFrame++;
        }
        if (difficult > 1.5) {
            saufOMeterEndFrame++;
        }
        if (difficult > 2) {
            saufOMeterEndFrame++;
        }
        if (difficult > 2.2) {
            saufOMeterEndFrame++;
        }
        switch (tmpDiff) {
            case 0:
                return new DifficultWithSaufOMeterEndFrame(TaskDifficult.EASY, saufOMeterEndFrame);
            case 1:
                return new DifficultWithSaufOMeterEndFrame(TaskDifficult.MEDIUM, saufOMeterEndFrame);
            case 2:
                return new DifficultWithSaufOMeterEndFrame(TaskDifficult.HARD, saufOMeterEndFrame);
        }
        return new DifficultWithSaufOMeterEndFrame(TaskDifficult.UNDEFINED, 0);
    }
}
