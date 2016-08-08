package de.tomade.saufomat2.activity.mainGame.task;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by woors on 24.05.2016.
 */
public class TaskFactoryTest {
    TaskFactory factory;

    @Before
    public void setUp(){
        factory = new TaskFactory();
    }

    @Test
    public void getEasyTask(){
        TaskDifficult difficult = TaskDifficult.EASY;
        int count = factory.getEasyTasks().size();
        Task t = factory.getTask(difficult);
        Assert.assertEquals(difficult, t.getDifficult());
        Assert.assertEquals(count - 1, factory.getEasyTasks());
    }

    @Test
    public void getMediumTask(){
        TaskDifficult difficult = TaskDifficult.MEDIUM;
        int count = factory.getMediumTasks().size();
        Task t = factory.getTask(difficult);
        Assert.assertEquals(difficult, t.getDifficult());
        Assert.assertEquals(count - 1, factory.getMediumTasks());
    }

    @Test
    public void getHardTask(){
        TaskDifficult difficult = TaskDifficult.HARD;
        int count = factory.getHardTasks().size();
        Task t = factory.getTask(difficult);
        Assert.assertEquals(difficult, t.getDifficult());
        Assert.assertEquals(count - 1, factory.getHardTasks());
    }

    @Test
    public void getEasyWinTask(){
        TaskDifficult difficult = TaskDifficult.EASY_WIN;
        int count = factory.getEasyWinTasks().size();
        Task t = factory.getTask(difficult);
        Assert.assertEquals(difficult, t.getDifficult());
        Assert.assertEquals(count - 1, factory.getEasyWinTasks());
    }
    @Test
    public void getMediumWinTask(){
        TaskDifficult difficult = TaskDifficult.MEDIUM_WIN;
        int count = factory.getMediumWinTasks().size();
        Task t = factory.getTask(difficult);
        Assert.assertEquals(difficult, t.getDifficult());
        Assert.assertEquals(count - 1, factory.getMediumWinTasks());
    }

    @Test
    public void getHardWinTask(){
        TaskDifficult difficult = TaskDifficult.HARD_WIN;
        int count = factory.getHardWinTasks().size();
        Task t = factory.getTask(difficult);
        Assert.assertEquals(difficult, t.getDifficult());
        Assert.assertEquals(count - 1, factory.getHardWinTasks());
    }

    @Test
    public void getGameTask(){
        TaskDifficult difficult = TaskDifficult.GAME;
        Task t = factory.getTask(difficult);
        Assert.assertEquals(null, t);
    }

    @Test
    public void testRefreshEasy(){
        TaskDifficult difficult = TaskDifficult.EASY;
        int count = factory.getEasyTasks().size();
        for(int i = 0; i < count - 1; i++) {
            Task t = factory.getTask(difficult);
            Assert.assertEquals(difficult, t.getDifficult());
            Assert.assertEquals(count - i, factory.getEasyTasks());
        }
        Task t = factory.getTask(difficult);
        Assert.assertEquals(difficult, t.getDifficult());
        Assert.assertEquals(count, factory.getEasyTasks());
    }

    @Test
    public void testRefreshMedium(){
        TaskDifficult difficult = TaskDifficult.MEDIUM;
        int count = factory.getMediumTasks().size();
        for(int i = 0; i < count - 1; i++) {
            Task t = factory.getTask(difficult);
            Assert.assertEquals(difficult, t.getDifficult());
            Assert.assertEquals(count - i, factory.getMediumTasks());
        }
        Task t = factory.getTask(difficult);
        Assert.assertEquals(difficult, t.getDifficult());
        Assert.assertEquals(count, factory.getMediumTasks());
    }

    @Test
    public void testRefreshHard(){
        TaskDifficult difficult = TaskDifficult.HARD;
        int count = factory.getHardTasks().size();
        for(int i = 0; i < count - 1; i++) {
            Task t = factory.getTask(difficult);
            Assert.assertEquals(difficult, t.getDifficult());
            Assert.assertEquals(count - i, factory.getHardTasks());
        }
        Task t = factory.getTask(difficult);
        Assert.assertEquals(difficult, t.getDifficult());
        Assert.assertEquals(count, factory.getHardTasks());
    }

    @Test
    public void testRefreshEasyWin(){
        TaskDifficult difficult = TaskDifficult.EASY_WIN;
        int count = factory.getEasyWinTasks().size();
        for(int i = 0; i < count - 1; i++) {
            Task t = factory.getTask(difficult);
            Assert.assertEquals(difficult, t.getDifficult());
            Assert.assertEquals(count - i, factory.getEasyWinTasks());
        }
        Task t = factory.getTask(difficult);
        Assert.assertEquals(difficult, t.getDifficult());
        Assert.assertEquals(count, factory.getEasyWinTasks());
    }

    @Test
    public void testRefreshMediumWin(){
        TaskDifficult difficult = TaskDifficult.MEDIUM_WIN;
        int count = factory.getMediumWinTasks().size();
        for(int i = 0; i < count - 1; i++) {
            Task t = factory.getTask(difficult);
            Assert.assertEquals(difficult, t.getDifficult());
            Assert.assertEquals(count - i, factory.getMediumWinTasks());
        }
        Task t = factory.getTask(difficult);
        Assert.assertEquals(difficult, t.getDifficult());
        Assert.assertEquals(count, factory.getMediumWinTasks());
    }

    @Test
    public void testRefreshHardWin(){
        TaskDifficult difficult = TaskDifficult.HARD_WIN;
        int count = factory.getHardWinTasks().size();
        for(int i = 0; i < count - 1; i++) {
            Task t = factory.getTask(difficult);
            Assert.assertEquals(difficult, t.getDifficult());
            Assert.assertEquals(count - i, factory.getHardWinTasks());
        }
        Task t = factory.getTask(difficult);
        Assert.assertEquals(difficult, t.getDifficult());
        Assert.assertEquals(count, factory.getHardWinTasks());
    }
}
