package de.tomade.saufomat2.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.constant.MiniGame;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by woors on 19.07.2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChooseMiniGameActivityTest {

    @Rule
    public ActivityTestRule<ChooseMiniGameActivity> activityRule = new ActivityTestRule<>(ChooseMiniGameActivity
            .class, true, false);
    private MiniGame currentGame;

    private void goToGame(MiniGame game, boolean left) {
        int fromLeft = 0;
        int fromRight = 0;
        switch (game) {
            case AUGENSAUFEN:
                fromLeft = 2;
                fromRight = 8 - fromLeft;
                if (fromRight < 0) {
                    fromRight *= -1;
                }
                if (left) {
                    for (int i = 0; i < fromLeft; i++) {
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                } else {
                    for (int i = 0; i < fromRight; i++) {
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case BIERGEBALLER:
                fromLeft = 1;
                fromRight = 8 - fromLeft;
                if (fromRight < 0) {
                    fromRight *= -1;
                }
                if (left) {
                    for (int i = 0; i < fromLeft; i++) {
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                } else {
                    for (int i = 0; i < fromRight; i++) {
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case BUSFAHREN:
                fromLeft = 0;
                fromRight = 8 - fromLeft;
                if (fromRight < 0) {
                    fromRight *= -1;
                }
                if (left) {
                    for (int i = 0; i < fromLeft; i++) {
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                } else {
                    for (int i = 0; i < fromRight; i++) {
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case BIERRUTSCHE:
                fromLeft = 7;
                fromRight = 8 - fromLeft;
                if (fromRight < 0) {
                    fromRight *= -1;
                }
                if (left) {
                    for (int i = 0; i < fromLeft; i++) {
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                } else {

                    for (int i = 0; i < fromRight; i++) {
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case ICH_HAB_NOCH_NIE:
                fromLeft = 6;
                fromRight = 8 - fromLeft;
                if (fromRight < 0) {
                    fromRight *= -1;
                }
                if (left) {
                    for (int i = 0; i < fromLeft; i++) {
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                } else {
                    for (int i = 0; i < fromRight; i++) {
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case KINGS:
                fromLeft = 5;
                fromRight = 8 - fromLeft;
                if (fromRight < 0) {
                    fromRight *= -1;
                }
                if (left) {
                    for (int i = 0; i < fromLeft; i++) {
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                } else {
                    for (int i = 0; i < fromRight; i++) {
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case KISTEN_STAPELN:
                fromLeft = 4;
                fromRight = 8 - fromLeft;
                if (fromRight < 0) {
                    fromRight *= -1;
                }
                if (left) {
                    for (int i = 0; i < fromLeft; i++) {
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                } else {
                    for (int i = 0; i < fromRight; i++) {
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case WERF_DICH_DICHT:
                fromLeft = 3;
                fromRight = 8 - fromLeft;
                if (fromRight < 0) {
                    fromRight *= -1;
                }
                if (left) {
                    for (int i = 0; i < fromLeft; i++) {
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                } else {
                    for (int i = 0; i < fromRight; i++) {
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
        }
    }

    @Test
    public void testBusfahrenLeftFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.BUSFAHREN, true);
        this.currentGame = MiniGame.BIERGEBALLER;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBusfahrenRightFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.BUSFAHREN, true);
        this.currentGame = MiniGame.BIERRUTSCHE;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBiergeballerLeftFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERGEBALLER, true);
        this.currentGame = MiniGame.AUGENSAUFEN;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBiergeballerRightFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERGEBALLER, true);
        this.currentGame = MiniGame.BUSFAHREN;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testAugensaufenLeftFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.AUGENSAUFEN, true);
        this.currentGame = MiniGame.WERF_DICH_DICHT;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testAugensaufenRightFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.AUGENSAUFEN, true);
        this.currentGame = MiniGame.BIERGEBALLER;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testWerfDichDichtLeftFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.WERF_DICH_DICHT, true);
        this.currentGame = MiniGame.KISTEN_STAPELN;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testWerfDichDichtRightFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.WERF_DICH_DICHT, true);
        this.currentGame = MiniGame.AUGENSAUFEN;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKistenStapelnLeftFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.KISTEN_STAPELN, true);
        this.currentGame = MiniGame.KINGS;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKistenStapelnRightFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.KISTEN_STAPELN, true);
        this.currentGame = MiniGame.WERF_DICH_DICHT;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKingsLeftFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.KINGS, true);
        this.currentGame = MiniGame.ICH_HAB_NOCH_NIE;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKingsRightFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.KINGS, true);
        this.currentGame = MiniGame.KISTEN_STAPELN;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());

    }

    @Test
    public void testIchHabNochNieLeftFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.ICH_HAB_NOCH_NIE, true);
        this.currentGame = MiniGame.BIERRUTSCHE;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testIchHabNochNieRightFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.ICH_HAB_NOCH_NIE, true);
        this.currentGame = MiniGame.KINGS;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBierrutscheLeftFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERRUTSCHE, true);
        this.currentGame = MiniGame.BUSFAHREN;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBierrutscheRightFromLeft() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERRUTSCHE, true);
        this.currentGame = MiniGame.ICH_HAB_NOCH_NIE;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }


    @Test
    public void testBusfahrenLeftFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.BUSFAHREN, false);
        this.currentGame = MiniGame.BIERGEBALLER;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBusfahrenRightFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.BUSFAHREN, false);
        this.currentGame = MiniGame.BIERRUTSCHE;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBiergeballerLeftFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERGEBALLER, false);
        this.currentGame = MiniGame.AUGENSAUFEN;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBiergeballerRightFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERGEBALLER, false);
        this.currentGame = MiniGame.BUSFAHREN;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testAugensaufenLeftFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.AUGENSAUFEN, false);
        this.currentGame = MiniGame.WERF_DICH_DICHT;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testAugensaufenRightFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.AUGENSAUFEN, false);
        this.currentGame = MiniGame.BIERGEBALLER;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testWerfDichDichtLeftFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.WERF_DICH_DICHT, false);
        this.currentGame = MiniGame.KISTEN_STAPELN;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testWerfDichDichtRightFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.WERF_DICH_DICHT, false);
        this.currentGame = MiniGame.AUGENSAUFEN;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKistenStapelnLeftFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.KISTEN_STAPELN, false);
        this.currentGame = MiniGame.KINGS;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKistenStapelnRightFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.KISTEN_STAPELN, false);
        this.currentGame = MiniGame.WERF_DICH_DICHT;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKingsLeftFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.KINGS, false);
        this.currentGame = MiniGame.ICH_HAB_NOCH_NIE;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKingsRightFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.KINGS, false);
        this.currentGame = MiniGame.KISTEN_STAPELN;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testIchHabNochNieLeftFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.ICH_HAB_NOCH_NIE, false);
        this.currentGame = MiniGame.BIERRUTSCHE;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testIchHabNochNieRightFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.ICH_HAB_NOCH_NIE, false);
        this.currentGame = MiniGame.KINGS;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBierrutscheLeftFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERRUTSCHE, false);
        this.currentGame = MiniGame.BUSFAHREN;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBierrutscheRightFromRight() {
        Intent intent = new Intent();
        this.activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERRUTSCHE, false);
        this.currentGame = MiniGame.ICH_HAB_NOCH_NIE;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(this.currentGame, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartAtAugensaufen() {
        Intent intent = new Intent();
        MiniGame game = MiniGame.AUGENSAUFEN;
        intent.putExtra("lastGame", game);
        this.activityRule.launchActivity(intent);

        Assert.assertEquals(game, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartAtBiergeballer() {
        Intent intent = new Intent();
        MiniGame game = MiniGame.BIERGEBALLER;
        intent.putExtra("lastGame", game);
        this.activityRule.launchActivity(intent);

        Assert.assertEquals(game, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartAtBusfahren() {
        Intent intent = new Intent();
        MiniGame game = MiniGame.BUSFAHREN;
        intent.putExtra("lastGame", game);
        this.activityRule.launchActivity(intent);

        Assert.assertEquals(game, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartAtBierrutsche() {
        Intent intent = new Intent();
        MiniGame game = MiniGame.BIERRUTSCHE;
        intent.putExtra("lastGame", game);
        this.activityRule.launchActivity(intent);

        Assert.assertEquals(game, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartAtIchHabNochNie() {
        Intent intent = new Intent();
        MiniGame game = MiniGame.ICH_HAB_NOCH_NIE;
        intent.putExtra("lastGame", game);
        this.activityRule.launchActivity(intent);

        Assert.assertEquals(game, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartAtKings() {
        Intent intent = new Intent();
        MiniGame game = MiniGame.KINGS;
        intent.putExtra("lastGame", game);
        this.activityRule.launchActivity(intent);

        Assert.assertEquals(game, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartAtKistenStapeln() {
        Intent intent = new Intent();
        MiniGame game = MiniGame.KISTEN_STAPELN;
        intent.putExtra("lastGame", game);
        this.activityRule.launchActivity(intent);

        Assert.assertEquals(game, this.activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartWerfDichDicht() {
        Intent intent = new Intent();
        MiniGame game = MiniGame.WERF_DICH_DICHT;
        intent.putExtra("lastGame", game);
        this.activityRule.launchActivity(intent);

        Assert.assertEquals(game, this.activityRule.getActivity().getCurrentGame());
    }
}