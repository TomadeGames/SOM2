package de.tomade.saufomat2.activity.miniGames.kings;

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
import de.tomade.saufomat2.model.card.Card;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by woors on 04.08.2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class KingsActivityTest {
    @Rule
    public ActivityTestRule<KingsActivity> activityTestRule = new ActivityTestRule<>(KingsActivity.class, true, false);

    @Before
    public void setUp(){
        Intent intent = new Intent();
        intent.putExtra("fromMenue", true);
        activityTestRule.launchActivity(intent);
    }

    private void checkCard(Card c){
        switch (c.getValue()){
            case SEVEN:
                Assert.assertTrue(activityTestRule.getActivity().getPopupText().getText().toString().contains("7"));
                break;
            case EIGHT:
                Assert.assertTrue(activityTestRule.getActivity().getPopupText().getText().toString().contains("8"));
                break;
            case NINE:
                Assert.assertTrue(activityTestRule.getActivity().getPopupText().getText().toString().contains("9"));
                break;
            case TEN:
                Assert.assertTrue(activityTestRule.getActivity().getPopupText().getText().toString().contains("10"));
                break;
            case JACK:
                Assert.assertTrue(activityTestRule.getActivity().getPopupText().getText().toString().contains("Bube"));
                break;
            case QUEEN:
                Assert.assertTrue(activityTestRule.getActivity().getPopupText().getText().toString().contains("Dame"));
                break;
            case KING:
                Assert.assertTrue(activityTestRule.getActivity().getPopupText().getText().toString().contains("König"));
                break;
            case ACE:
                Assert.assertTrue(activityTestRule.getActivity().getPopupText().getText().toString().contains("Ass"));
                break;
            default:
                Assert.fail();
                break;
        }
    }

    @Test
    public void testNextCard(){
        Assert.assertEquals(0, activityTestRule.getActivity().getCardCount());
        onView(withId(R.id.popupText)).perform(click());
        Assert.assertEquals(1, activityTestRule.getActivity().getCardCount());
        checkCard(activityTestRule.getActivity().getCard());
    }

    @Test
    public void testTutorialButton(){
        Assert.assertFalse(activityTestRule.getActivity().isTutorialShown());
        onView(withId(R.id.tutorialButton)).perform(click());
        Assert.assertTrue(activityTestRule.getActivity().isTutorialShown());
        onView(withId(R.id.cardImage)).perform(click());
        Assert.assertFalse(activityTestRule.getActivity().isTutorialShown());

    }

    @Test
    public void testCardReset() throws InterruptedException {
        Assert.assertEquals(0, activityTestRule.getActivity().getCardCount());
        for(int i = 0; i < 32; i++){
            onView(withId(R.id.popupText)).perform(click());
            Thread.sleep(100);
            Assert.assertEquals("Fail at: " + i + " expected: " + (i+1) + " but was: " + activityTestRule.getActivity().getCardCount(), i + 1, activityTestRule.getActivity().getCardCount());
            checkCard(activityTestRule.getActivity().getCard());
            onView(withId(R.id.popupText)).perform(click());
        }

        onView(withId(R.id.popupText)).perform(click());
        Assert.assertEquals(1, activityTestRule.getActivity().getCardCount());
        checkCard(activityTestRule.getActivity().getCard());
    }
}