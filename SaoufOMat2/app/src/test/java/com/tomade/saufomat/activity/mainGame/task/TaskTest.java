package com.tomade.saufomat.activity.mainGame.task;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by woors on 24.05.2016.
 */
public class TaskTest {
    @Test
    public void constructorTest() {
        Task t = new Task("test", TaskDifficult.EASY, 0, 0, TaskTarget.ALL);
        Assert.assertEquals("test", t.getText());
        Assert.assertEquals(TaskDifficult.EASY, t.getDifficult());
        Assert.assertEquals(0, t.getCost());
        Assert.assertEquals(0, t.getDrinkCount());
        Assert.assertEquals(TaskTarget.ALL, t.getTarget());
    }

    @Test
    public void constructorTest2() {
        Task t = new Task("test", TaskDifficult.EASY, -1, -1, TaskTarget.ALL);
        Assert.assertEquals("test", t.getText());
        Assert.assertEquals(TaskDifficult.EASY, t.getDifficult());
        Assert.assertEquals(0, t.getCost());
        Assert.assertEquals(0, t.getDrinkCount());
        Assert.assertEquals(TaskTarget.ALL, t.getTarget());
    }
}
