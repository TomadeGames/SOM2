package de.tomade.saufomat2.busfahren;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.model.card.Card;
import de.tomade.saufomat2.model.card.CardValue;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by woors on 07.06.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BusfahrenActivityTest {
    public static final String TAG = BusfahrenActivityTest.class.getSimpleName();
    @Rule
    public ActivityTestRule<BusfahrenActivity> activityRule = new ActivityTestRule<>(BusfahrenActivity.class);
    private Card[] cards;
    private int drinkCount;
    private final long WAIT_TIME = 500;
    private final long REPEATS = 1;
    private BusfahrenActivity activity;

    public BusfahrenActivityTest(){
    }

    private void clickLeftButton(){ //Rot, Höher, Dazwischen, Gleich, Ass
        Log.d(TAG, "links");
        onView(withId(R.id.leftButton)).perform(click());
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void clickRightButton(){ //Schwarz, Tiefer, Nicht Dazwischen, Nicht Gleich, Kein Ass
        Log.d(TAG, "rechts");
        onView(withId(R.id.rightButton)).perform(click());
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void answerQuestion(int question){
        switch (question) {
            case 0:
                if (cards[0].isRed()) {
                    clickLeftButton();
                } else {
                    clickRightButton();
                }
                break;
            case 1:
                if (cards[1].isHigherAs(cards[0])) {
                    Log.d(TAG, "higher");
                    clickLeftButton();
                } else {
                    Log.d(TAG, "lower");
                    clickRightButton();
                }
                break;
            case 2:
                if (cards[2].isBetween(cards[0], cards[1])) {
                    clickLeftButton();
                } else {
                    clickRightButton();
                }
                break;
            case 3:
                if (cards[3].equals(cards[2])) {
                    clickLeftButton();
                } else {
                    clickRightButton();
                }
                break;
            case 4:
                if (cards[4].getValue() == CardValue.ACE) {
                    clickLeftButton();
                } else {
                    clickRightButton();
                }
                break;
        }
    }

    @Before
    public void setUp() throws InterruptedException {
        activity = activityRule.getActivity();
        cards = activity.getCards();
        drinkCount = 0;
    }

    @After
    public void tearDown(){
        //activity.restartActivity();
    }

    @Test
    public void testTutorialButton(){
        onView(withId(R.id.tutorialButton)).perform(click());
        if(activity.getTutorial().getVisibility() != View.VISIBLE){
            Assert.fail();
        }
        onView(withId(R.id.tutorialPanel)).perform(click());
        if(activity.getTutorial().getVisibility() == View.VISIBLE){
            Assert.fail();
        }
    }

    @Test
    public void testRedButton() throws InterruptedException {
        if(!cards[0].isRed()){
            drinkCount = 1;
        }
        Thread.sleep(WAIT_TIME);
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(getErrorMessage(), drinkCount, activity.getDrinkCount());
    }

    @Test
    public void testBlackButton() throws InterruptedException {
        if(cards[0].isRed()){
            drinkCount = 1;
        }
        Thread.sleep(WAIT_TIME);
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(getErrorMessage(), drinkCount, activity.getDrinkCount());
    }

    @Test
    public void testLowerButton() throws Exception{  //rechts
        answerQuestion(0);

        if(cards[1].isHigherAs(cards[0])){
            drinkCount = 2;
        }
        Thread.sleep(WAIT_TIME);
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(getErrorMessage(), drinkCount, activity.getDrinkCount());

    }

    @Test
    public void testHigherButton() throws InterruptedException { //links
        answerQuestion(0);

        if(!cards[1].isHigherAs(cards[0])){
            drinkCount = 2;
        }
        Thread.sleep(WAIT_TIME);

        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(getErrorMessage(), drinkCount, activity.getDrinkCount());

    }

    private String getErrorMessage(){
        String erg = "expected: " + drinkCount + " erg: " +activity.getDrinkCount() + " cards: ";
        for(int i = 0; i < 5; i++){
            erg += cards[i].getColor() + "-" + cards[i].getValue() + ", ";
        }
        return erg;
    }

    @Test
    public void testBetweenButton() throws InterruptedException { //links
        answerQuestion(0);
        answerQuestion(1);

        if(!cards[2].isBetween(cards[0], cards[1])){
            drinkCount = 3;
        }
        Thread.sleep(WAIT_TIME);

        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(getErrorMessage(), drinkCount, activity.getDrinkCount());
    }

    @Test
    public void testNotBetweenButton() throws InterruptedException { //rechts
        answerQuestion(0);
        answerQuestion(1);

        if(cards[2].isBetween(cards[0], cards[1])){
            drinkCount = 3;
        }
        Thread.sleep(WAIT_TIME);

        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(getErrorMessage(), drinkCount, activity.getDrinkCount());
    }

    @Test
    public void testAgainButton() throws InterruptedException { //links
        answerQuestion(0);
        answerQuestion(1);
        answerQuestion(2);

        if(!cards[4].equals(cards[3])){
            drinkCount = 4;
        }
        Thread.sleep(WAIT_TIME);

        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(getErrorMessage(), drinkCount, activity.getDrinkCount());
    }

    @Test
    public void testNotAgainButton() throws InterruptedException { //rechts
        answerQuestion(0);
        answerQuestion(1);
        answerQuestion(2);

        if(cards[4].equals(cards[3])){
            drinkCount = 4;
        }
        Thread.sleep(WAIT_TIME);

        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(getErrorMessage(), drinkCount, activity.getDrinkCount());
    }

    /* Entfernt, da neu laden der Activity nicht funktioniert
    @Test
    public void testAceButton() throws InterruptedException { //links
        answerQuestion(0, true);
        answerQuestion(1, true);
        answerQuestion(2, true);
        answerQuestion(3, true);

        if(cards[4].getValue() != CardValue.ACE){
            drinkCount = 5;
        }
        Thread.sleep(WAIT_TIME);

        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(getErrorMessage(), drinkCount, activity.getDrinkCount());
    }

    @Test
    public void testNoAceButton() throws InterruptedException { //rechts
        answerQuestion(0, true);
        answerQuestion(1, true);
        answerQuestion(2, true);
        answerQuestion(3, true);

        if(cards[4].getValue() == CardValue.ACE){
            drinkCount = 5;
        }
        Thread.sleep(WAIT_TIME);

        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(getErrorMessage(), drinkCount, activity.getDrinkCount());
    }
    */

    @Test
    public void testLastButton() throws InterruptedException {
        answerQuestion(0);
        answerQuestion(1);
        answerQuestion(2);
        answerQuestion(3);

        drinkCount = 5;

        if(cards[4].getValue() == CardValue.ACE){
            clickRightButton();
        }
        else{
            clickLeftButton();
        }

        Thread.sleep(WAIT_TIME);
        Assert.assertEquals(getErrorMessage(), drinkCount, activity.getDrinkCount());
    }
}