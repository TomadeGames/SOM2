package de.tomade.saufomat2.activity.miniGames.augensaufen;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.CreatePlayerActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by woors on 03.08.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AugensaufenActivityTest {
    @Rule
    public ActivityTestRule<AugensaufenActivity> mActivityRule = new ActivityTestRule<>(AugensaufenActivity.class, true, false);

    @Before
    public void setUp(){
        Intent intent = new Intent();
        intent.putExtra("fromMenue", true);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void testDice(){
        Assert.assertEquals(AugensaufenState.START, mActivityRule.getActivity().getGameState());
        onView(withId(R.id.diceImage)).perform(click());
        Assert.assertEquals(AugensaufenState.ROLLING, mActivityRule.getActivity().getGameState());
        onView(withId(R.id.diceImage)).perform(click());
        Assert.assertEquals(AugensaufenState.RESULT, mActivityRule.getActivity().getGameState());
        Assert.assertTrue(mActivityRule.getActivity().getCurrentDiceIndex() <= 5 && mActivityRule.getActivity().getCurrentDiceIndex() >=0);
    }
}