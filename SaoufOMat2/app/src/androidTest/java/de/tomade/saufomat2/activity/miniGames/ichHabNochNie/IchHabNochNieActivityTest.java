package de.tomade.saufomat2.activity.miniGames.ichHabNochNie;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.threading.ThreadedView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by woors on 04.08.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class IchHabNochNieActivityTest {
    @Rule
    public ActivityTestRule<IchHabNochNieActivity> activityTestRule = new ActivityTestRule<>(IchHabNochNieActivity.class);

    @Test
    public void testNextQuestion(){
        onView(withId(R.id.taskText)).perform(click());
        Assert.assertEquals(activityTestRule.getActivity().getAllQuestions().size() - 2 , activityTestRule.getActivity().getCurrentQuestions().size());
    }

    @Test
    public void testTutorialButton() throws InterruptedException {
        onView(withId(R.id.tutorialButton)).perform(click());
        Assert.assertTrue(activityTestRule.getActivity().isTutorialShown());
        onView(withId(R.id.taskText)).perform(click());
        Assert.assertTrue(!activityTestRule.getActivity().isTutorialShown());
    }

    @Test
    public void testQuestionReset(){
        int size = activityTestRule.getActivity().getAllQuestions().size();
        for (int i = 0; i < size - 2; i++){
            onView(withId(R.id.taskText)).perform(click());
            Assert.assertEquals("Fail at Index " + i + ": expected: " + (activityTestRule.getActivity().getAllQuestions().size() - i -2) + " but was: " + activityTestRule.getActivity().getCurrentQuestions().size()
                    ,activityTestRule.getActivity().getAllQuestions().size() - i -2 , activityTestRule.getActivity().getCurrentQuestions().size());
        }
        onView(withId(R.id.taskText)).perform(click());
        Assert.assertEquals(activityTestRule.getActivity().getAllQuestions().size(), activityTestRule.getActivity().getCurrentQuestions().size());
    }
}
