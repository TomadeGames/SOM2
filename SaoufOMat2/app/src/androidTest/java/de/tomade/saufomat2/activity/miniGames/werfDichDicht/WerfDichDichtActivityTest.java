package de.tomade.saufomat2.activity.miniGames.werfDichDicht;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.intent.IntentCallback;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Time;

import de.tomade.saufomat2.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by woors on 04.08.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class WerfDichDichtActivityTest {

    @Rule
    public ActivityTestRule<WerfDichDichtActivity> activityTestRule = new ActivityTestRule<>(WerfDichDichtActivity.class, true, false);

    @Before
    public void setUp(){
        Intent intent = new Intent();
        intent.putExtra("fromMenue", true);
        activityTestRule.launchActivity(intent);
    }

    @Test
    public void testTutorialButton(){
        Assert.assertTrue(activityTestRule.getActivity().getTutorial().getVisibility() != View.VISIBLE);
        onView(withId(R.id.tutorialButton)).perform(click());
        Assert.assertTrue(activityTestRule.getActivity().getTutorial().getVisibility() == View.VISIBLE);
        onView(withId(R.id.tutorialPanel)).perform(click());
        Assert.assertTrue(activityTestRule.getActivity().getTutorial().getVisibility() != View.VISIBLE);
    }

    @Test
    public void testFillGlass() throws InterruptedException {
        Assert.assertEquals(WerfDichDichtState.START, activityTestRule.getActivity().getGameState());
        onView(withId(R.id.dicePanel)).perform(click());
        Thread.sleep(50);
        Assert.assertEquals(WerfDichDichtState.ROLLING, activityTestRule.getActivity().getGameState());
        onView(withId(R.id.dicePanel)).perform(click());
        Assert.assertEquals(WerfDichDichtState.GLASS_ANIMATION, activityTestRule.getActivity().getGameState());
        while (activityTestRule.getActivity().getPopupText().getVisibility() != View.VISIBLE){

        }
        Thread.sleep(500);
        Assert.assertEquals(WerfDichDichtState.START, activityTestRule.getActivity().getGameState());

        boolean[] expected = {false, false, false, false, false, false};

        expected[activityTestRule.getActivity().getCurrentDiceIndex()] = !expected[activityTestRule.getActivity().getCurrentDiceIndex()];

        for(int i = 0; i < 6; i++) {
            Assert.assertEquals(expected[i], activityTestRule.getActivity().getIsFull()[i]);
        }
    }

    @Test
    public void testEmptyGlass() throws InterruptedException {
        boolean[] expected = {false, false, false, false, false, false};
        for (int j = 0; j < 7; j++) {
            Assert.assertEquals(WerfDichDichtState.START, activityTestRule.getActivity().getGameState());
            onView(withId(R.id.dicePanel)).perform(click());
            Thread.sleep(50);
            Assert.assertEquals(WerfDichDichtState.ROLLING, activityTestRule.getActivity().getGameState());
            onView(withId(R.id.dicePanel)).perform(click());
            Assert.assertEquals(WerfDichDichtState.GLASS_ANIMATION, activityTestRule.getActivity().getGameState());
            while (activityTestRule.getActivity().getPopupText().getVisibility() != View.VISIBLE) {

            }
            Thread.sleep(500);
            Assert.assertEquals(WerfDichDichtState.START, activityTestRule.getActivity().getGameState());


            expected[activityTestRule.getActivity().getCurrentDiceIndex()] = !expected[activityTestRule.getActivity().getCurrentDiceIndex()];

            for (int i = 0; i < 6; i++) {
                Assert.assertEquals(expected[i], activityTestRule.getActivity().getIsFull()[i]);
            }
        }
    }
}