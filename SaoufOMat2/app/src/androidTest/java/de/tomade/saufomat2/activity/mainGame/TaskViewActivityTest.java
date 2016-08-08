package de.tomade.saufomat2.activity.mainGame;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.ChooseMiniGameActivity;
import de.tomade.saufomat2.activity.mainGame.task.Task;
import de.tomade.saufomat2.activity.mainGame.task.TaskDifficult;
import de.tomade.saufomat2.activity.mainGame.task.TaskTarget;
import de.tomade.saufomat2.model.Player;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by woors on 28.07.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TaskViewActivityTest {
    @Rule
    public ActivityTestRule<TaskViewActivity> mActivityRule =
            new ActivityTestRule<>(TaskViewActivity.class, true, false);


    private void startWithValidParams(ArrayList<Player> player, int yesCount, int noCount, TaskTarget target) {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent();
        Task currentTask = new Task("testTask,1,2,ALL", TaskDifficult.EASY, yesCount, noCount, target);

        int currentPlayerId = 0;

        intent.putExtra("task", currentTask);
        intent.putParcelableArrayListExtra("player", player);
        intent.putExtra("currentPlayer", currentPlayerId);

        mActivityRule.launchActivity(intent);
    }

    @Test
    public void testClickOkButton(){
        ArrayList<Player> player = new ArrayList<>();
        player.add(new Player("testPlayer0", 70, true, 0, 1, 1));
        player.add(new Player("testPlayer1", 80, false, 0, 0, 0));
        startWithValidParams(player, 1,2, TaskTarget.ALL);
        onView(withId(R.id.yesButton)).perform(click());
        for(int i = 0; i < player.size(); i++) {
            Assert.assertEquals(mActivityRule.getActivity().getPlayers().get(i).getDrinks(), 1);
        }
        Assert.assertEquals(mActivityRule.getActivity().getCurrentPlayer().getId(), 1);
    }

    @Test
    public void testClickNoButton(){
        ArrayList<Player> player = new ArrayList<>();
        player.add(new Player("testPlayer0", 70, true, 0, 1, 1));
        player.add(new Player("testPlayer1", 80, false, 0, 0, 0));
        startWithValidParams(player, 1,2, TaskTarget.ALL);
        onView(withId(R.id.yesButton)).perform(click());

        Assert.assertEquals(mActivityRule.getActivity().getPlayers().get(0).getDrinks(), 2);
        Assert.assertEquals(mActivityRule.getActivity().getPlayers().get(1).getDrinks(), 0);

        Assert.assertEquals(mActivityRule.getActivity().getCurrentPlayer(), 1);
    }

}