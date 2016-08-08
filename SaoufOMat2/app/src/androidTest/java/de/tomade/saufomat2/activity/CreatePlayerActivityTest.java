package de.tomade.saufomat2.activity;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.threading.ThreadedView;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.doubleClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsAnything.anything;
import static org.junit.Assert.*;

/**
 * Created by woors on 01.08.2016.
 * Die Tastatur muss bei einem Emulator auf Software-Tastatur eingestellt werden
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreatePlayerActivityTest {
    @Rule
    public ActivityTestRule<CreatePlayerActivity> mActivityRule = new ActivityTestRule<>(CreatePlayerActivity.class);

    private void checkPlayer(int index, String playerName, int weight, int nextPlayerIndex, int lastPlayerIndex, boolean isMann){
        Player pthis = mActivityRule.getActivity().getPlayers().get(index);
        Player p0 = mActivityRule.getActivity().getPlayers().get(lastPlayerIndex);
        Player p1 = mActivityRule.getActivity().getPlayers().get(nextPlayerIndex);

        Assert.assertEquals(pthis.getName(), playerName);
        Assert.assertEquals("Spieler: " + pthis.getId() + " " + pthis.getName() + " nextPlayerId: " + pthis.getNextPlayerId() + " erwartet: "
                + p1.getId() + " " + p1.getName(), pthis.getNextPlayerId(), p1.getId());
        Assert.assertEquals("Spieler: " + pthis.getName() + " lastPlayerId: " + pthis.getLastPlayerId() + " erwartet: "
                + p0.getId() + " " + p0.getName(), pthis.getLastPlayerId(), p0.getId());
        Assert.assertEquals(pthis.getWeight(), weight);
        Assert.assertEquals(pthis.getDrinks(), 0);
        Assert.assertEquals(pthis.getIsMan(), isMann);
    }

    private void addPlayer(String name, int weight, boolean isMan){
        onView(withId(R.id.btnNewPlayer)).perform(click());
        onView(withId(R.id.etxtName)).perform(typeText(name)).check(matches(withText(name)));
        chooseGender(isMan);
        onView(withId(R.id.etxtWeight)).perform(replaceText("" + weight)).check(matches(withText("" + weight)));
        onView(withId(16908313)).perform(click());
    }

    private void checkSinglePlayer(int index, String playerName, int weight, boolean isMann){
        Player pthis = mActivityRule.getActivity().getPlayers().get(index);

        Assert.assertEquals(pthis.getName(), playerName);
        Assert.assertEquals(pthis.getWeight(), weight);
        Assert.assertEquals(pthis.getDrinks(), 0);
        Assert.assertEquals(pthis.getIsMan(), isMann);
    }

    private void chooseGender(boolean isMan){
        String gender;
        if(isMan){
            gender = "Mann";
        }
        else{
            gender = "Frau";
        }
        onView(withId(R.id.spGender)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(gender))).perform(click());
        onView(withId(R.id.spGender)).check(matches(withSpinnerText(containsString(gender))));
    }

    @Test
    public void testAddPlayer() throws InterruptedException {
        String player0Name = "testPlayer0";
        int weight0 = 1;
        boolean isMan0 = false;

        addPlayer(player0Name, weight0, isMan0);
        checkSinglePlayer(0, player0Name, weight0, isMan0);
    }

    @Test
    public void testAdd2DiffrentPlayer(){
        String player0Name = "testPlayer0";
        int weight0 = 72;
        boolean isMan0 = false;

        addPlayer(player0Name, weight0, isMan0);

        String player1Name = "testPlayer1";
        int weight1 = 71;
        boolean isMan1 = true;

        addPlayer(player1Name, weight1, isMan1);

        Assert.assertEquals(mActivityRule.getActivity().getPlayers().size(), 2);

        checkSinglePlayer(0, player0Name, weight0, isMan0);
        checkSinglePlayer(1, player1Name, weight1, isMan1 );
    }

    @Test
    public void testAdd2SamePlayer(){
        String player0Name = "testPlayer0";
        int weight0 = 72;
        boolean isMan0 = false;

        addPlayer(player0Name, weight0, isMan0);

        String player1Name = player0Name;
        int weight1 = 71;
        boolean isMan1 = true;

        addPlayer(player1Name, weight1, isMan1);

        Assert.assertEquals(mActivityRule.getActivity().getPlayers().size(), 1);

        checkSinglePlayer(0, player0Name, weight0, isMan0);
    }

    @Test
    public void testAddPlayerWithToLowWeight(){
        String player0Name = "testPlayer0";
        int weight0 = 0;
        boolean isMan0 = false;

        addPlayer(player0Name, weight0, isMan0);

        Assert.assertEquals(mActivityRule.getActivity().getPlayers().size(), 0);
    }

    @Test
    public void testAddPlayerDefaultValues(){
        onView(withId(R.id.btnNewPlayer)).perform(click());
        String player0Name = "testPlayer0";
        onView(withId(R.id.etxtName)).perform(typeText(player0Name)).check(matches(withText(player0Name)));
        onView(withId(16908313)).perform(click());

        Assert.assertEquals(mActivityRule.getActivity().getPlayers().size(), 1);
        checkSinglePlayer(0, player0Name, 70, true);
    }

    @Test
    public void testAddPlayerNoInput(){
        onView(withId(R.id.btnNewPlayer)).perform(click());
        onView(withId(16908313)).perform(click());

        Assert.assertEquals(mActivityRule.getActivity().getPlayers().size(), 0);
    }

    @Test
    public void testDeletePlayer(){
        addPlayer("testPlayer0", 1, true);
        onView(withId(R.id.ibDelete)).perform(click());
        Assert.assertEquals(mActivityRule.getActivity().getPlayers().size(), 0);
    }

    @Test
    public void testDeleteSecondPlayer(){
        String p0Name = "testPlayer0";
        String p1Name = "testPlayer1";

        addPlayer(p0Name, 1, true);
        addPlayer(p1Name, 1, true);

        onView(allOf(withId(R.id.ibDelete), hasSibling(withText(p1Name)))).perform(click());

        Assert.assertEquals(mActivityRule.getActivity().getPlayers().size(), 1);
        Assert.assertEquals(mActivityRule.getActivity().getPlayers().get(0).getName(),p0Name);
    }

    @Test
    public void testEditPlayer(){
        String p0Name = "testPlayer0";
        String p1Name = "testPlayer1";
        int weight0 = 1;
        int weight1 = 2;
        boolean isMan0 = true;
        boolean isMan1 = false;

        addPlayer(p0Name, weight0, isMan0);

        onView(allOf(withId(R.id.ibEdit), hasSibling(withText(p0Name)))).perform(click());
        onView(withId(R.id.etxtName)).perform(replaceText(p1Name)).check(matches(withText(p1Name)));
        chooseGender(isMan1);
        onView(withId(R.id.etxtWeight)).perform(replaceText("" + weight1)).check(matches(withText("" + weight1)));
        onView(withId(16908313)).perform(click());

        checkSinglePlayer(0, p1Name, weight1, isMan1);
    }

    @Test
    public void testEditSecondPlayer(){
        String p0Name = "testPlayer0";
        String p1Name = "testPlayer1";
        int weight0 = 1;
        int weight1 = 2;
        boolean isMan0 = true;
        boolean isMan1 = false;

        addPlayer("testPlayer2", weight0, isMan0);
        addPlayer(p0Name, weight0, isMan0);

        onView(allOf(withId(R.id.ibEdit), hasSibling(withText(p0Name)))).perform(click());
        onView(withId(R.id.etxtName)).perform(replaceText(p1Name)).check(matches(withText(p1Name)));
        chooseGender(isMan1);
        onView(withId(R.id.etxtWeight)).perform(replaceText("" + weight1)).check(matches(withText("" + weight1)));
        onView(withId(16908313)).perform(click());

        checkSinglePlayer(1, p1Name, weight1, isMan1);
    }

    //Ist fehlgeschlagen (Fehler liegt im Code)
    @Test
    public void testEditWrongName(){
        String p0Name = "testPlayer0";
        String p1Name = "testPlayer2";
        int weight0 = 1;
        int weight1 = 2;
        boolean isMan0 = true;
        boolean isMan1 = false;

        addPlayer("testPlayer2", weight0, isMan0);
        addPlayer(p0Name, weight0, isMan0);

        onView(allOf(withId(R.id.ibEdit), hasSibling(withText(p0Name)))).perform(click());
        onView(withId(R.id.etxtName)).perform(replaceText(p1Name)).check(matches(withText(p1Name)));
        chooseGender(isMan1);
        onView(withId(R.id.etxtWeight)).perform(replaceText("" + weight1)).check(matches(withText("" + weight1)));
        onView(withId(16908313)).perform(click());

        checkSinglePlayer(1, p0Name, weight0, isMan0);
    }

    @Test
    public void testEditToLowWeight(){
        String p0Name = "testPlayer0";
        String p1Name = "testPlayer1";
        int weight0 = 1;
        int weight1 = 0;
        boolean isMan0 = true;
        boolean isMan1 = false;

        addPlayer(p0Name, weight0, isMan0);

        onView(allOf(withId(R.id.ibEdit), hasSibling(withText(p0Name)))).perform(click());
        onView(withId(R.id.etxtName)).perform(replaceText(p1Name)).check(matches(withText(p1Name)));
        chooseGender(isMan1);
        onView(withId(R.id.etxtWeight)).perform(replaceText("" + weight1)).check(matches(withText("" + weight1)));
        onView(withId(16908313)).perform(click());

        checkSinglePlayer(0, p0Name, weight0, isMan0);
    }
}