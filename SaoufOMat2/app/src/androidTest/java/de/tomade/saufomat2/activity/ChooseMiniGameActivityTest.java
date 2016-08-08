package de.tomade.saufomat2.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.busfahren.BusfahrenActivity;
import de.tomade.saufomat2.model.MiniGame;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by woors on 19.07.2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChooseMiniGameActivityTest {

    /*
    AUGENSAUFEN
    BIERGEBALLER
    BUSFAHREN
    BIERRUTSCHE
    ICH_HAB_NOCH_NIE
    KINGS
    KISTEN_STAPELN
    WERF_DICH_DICHT
    */

    @Rule
    public ActivityTestRule<ChooseMiniGameActivity> activityRule = new ActivityTestRule<>(ChooseMiniGameActivity.class, true, false);
    private MiniGame currentGame;

    private void goToGame(MiniGame game, boolean left){
        int fromLeft = 0;
        int fromRight = 0;
        switch (game){
            case AUGENSAUFEN:
                fromLeft = 2;
                fromRight = 8 - fromLeft;
                if(fromRight < 0){
                    fromRight *= -1;
                }
                if(left){
                    for(int i = 0; i < fromLeft; i++){
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                }
                else{
                    for(int i = 0; i < fromRight; i++){
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case BIERGEBALLER:
                fromLeft = 1;
                fromRight = 8 - fromLeft;
                if(fromRight < 0){
                    fromRight *= -1;
                }
                if(left){
                    for(int i = 0; i < fromLeft; i++){
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                }
                else{
                    for(int i = 0; i < fromRight; i++){
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case BUSFAHREN:
                fromLeft = 0;
                fromRight = 8 - fromLeft;
                if(fromRight < 0){
                    fromRight *= -1;
                }
                if(left){
                    for(int i = 0; i < fromLeft; i++){
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                }
                else{
                    for(int i = 0; i < fromRight; i++){
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case BIERRUTSCHE:
                fromLeft = 7;
                fromRight = 8 - fromLeft;
                if(fromRight < 0){
                    fromRight *= -1;
                }
                if(left){
                    for(int i = 0; i < fromLeft; i++){
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                }
                else{

                    for(int i = 0; i < fromRight; i++){
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case ICH_HAB_NOCH_NIE:
                fromLeft = 6;
                fromRight = 8 - fromLeft;
                if(fromRight < 0){
                    fromRight *= -1;
                }
                if(left){
                    for(int i = 0; i < fromLeft; i++){
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                }
                else{
                    for(int i = 0; i < fromRight; i++){
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case KINGS:
                fromLeft = 5;
                fromRight = 8 - fromLeft;
                if(fromRight < 0){
                    fromRight *= -1;
                }
                if(left){
                    for(int i = 0; i < fromLeft; i++){
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                }
                else{
                    for(int i = 0; i < fromRight; i++){
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case KISTEN_STAPELN:
                fromLeft = 4;
                fromRight = 8 - fromLeft;
                if(fromRight < 0){
                    fromRight *= -1;
                }
                if(left){
                    for(int i = 0; i < fromLeft; i++){
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                }
                else{
                    for(int i = 0; i < fromRight; i++){
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
            case WERF_DICH_DICHT:
                fromLeft = 3;
                fromRight = 8 - fromLeft;
                if(fromRight < 0){
                    fromRight *= -1;
                }
                if(left){
                    for(int i = 0; i < fromLeft; i++){
                        onView(withId(R.id.leftButton)).perform(click());
                    }
                }
                else{
                    for(int i = 0; i < fromRight; i++){
                        onView(withId(R.id.rightButton)).perform(click());
                    }
                }
                break;
        }
    }

    @Test
    public void testBusfahrenLeftFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.BUSFAHREN, true);
        currentGame = MiniGame.BIERGEBALLER;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBusfahrenRightFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.BUSFAHREN, true);
        currentGame = MiniGame.BIERRUTSCHE;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBiergeballerLeftFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERGEBALLER, true);
        currentGame = MiniGame.AUGENSAUFEN;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBiergeballerRightFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERGEBALLER, true);
        currentGame = MiniGame.BUSFAHREN;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testAugensaufenLeftFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.AUGENSAUFEN, true);
        currentGame = MiniGame.WERF_DICH_DICHT;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testAugensaufenRightFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.AUGENSAUFEN, true);
        currentGame = MiniGame.BIERGEBALLER;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testWerfDichDichtLeftFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.WERF_DICH_DICHT, true);
        currentGame = MiniGame.KISTEN_STAPELN;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testWerfDichDichtRightFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.WERF_DICH_DICHT, true);
        currentGame = MiniGame.AUGENSAUFEN;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKistenStapelnLeftFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.KISTEN_STAPELN, true);
        currentGame = MiniGame.KINGS;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKistenStapelnRightFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.KISTEN_STAPELN, true);
        currentGame = MiniGame.WERF_DICH_DICHT;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKingsLeftFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.KINGS, true);
        currentGame = MiniGame.ICH_HAB_NOCH_NIE;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKingsRightFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.KINGS, true);
        currentGame = MiniGame.KISTEN_STAPELN;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());

    }

    @Test
    public void testIchHabNochNieLeftFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.ICH_HAB_NOCH_NIE, true);
        currentGame = MiniGame.BIERRUTSCHE;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testIchHabNochNieRightFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.ICH_HAB_NOCH_NIE, true);
        currentGame = MiniGame.KINGS;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBierrutscheLeftFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERRUTSCHE, true);
        currentGame = MiniGame.BUSFAHREN;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBierrutscheRightFromLeft(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERRUTSCHE, true);
        currentGame = MiniGame.ICH_HAB_NOCH_NIE;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }


    @Test
    public void testBusfahrenLeftFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.BUSFAHREN, false);
        currentGame = MiniGame.BIERGEBALLER;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBusfahrenRightFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.BUSFAHREN, false);
        currentGame = MiniGame.BIERRUTSCHE;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBiergeballerLeftFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERGEBALLER, false);
        currentGame = MiniGame.AUGENSAUFEN;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBiergeballerRightFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERGEBALLER, false);
        currentGame = MiniGame.BUSFAHREN;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame, activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testAugensaufenLeftFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.AUGENSAUFEN, false);
        currentGame = MiniGame.WERF_DICH_DICHT;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testAugensaufenRightFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.AUGENSAUFEN, false);
        currentGame = MiniGame.BIERGEBALLER;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testWerfDichDichtLeftFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.WERF_DICH_DICHT, false);
        currentGame = MiniGame.KISTEN_STAPELN;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testWerfDichDichtRightFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.WERF_DICH_DICHT, false);
        currentGame = MiniGame.AUGENSAUFEN;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKistenStapelnLeftFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.KISTEN_STAPELN, false);
        currentGame = MiniGame.KINGS;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKistenStapelnRightFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.KISTEN_STAPELN, false);
        currentGame = MiniGame.WERF_DICH_DICHT;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKingsLeftFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.KINGS, false);
        currentGame = MiniGame.ICH_HAB_NOCH_NIE;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testKingsRightFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.KINGS, false);
        currentGame = MiniGame.KISTEN_STAPELN;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testIchHabNochNieLeftFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.ICH_HAB_NOCH_NIE, false);
        currentGame = MiniGame.BIERRUTSCHE;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testIchHabNochNieRightFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.ICH_HAB_NOCH_NIE, false);
        currentGame = MiniGame.KINGS;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBierrutscheLeftFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERRUTSCHE, false);
        currentGame = MiniGame.BUSFAHREN;
        onView(withId(R.id.leftButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testBierrutscheRightFromRight(){
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        goToGame(MiniGame.BIERRUTSCHE, false);
        currentGame = MiniGame.ICH_HAB_NOCH_NIE;
        onView(withId(R.id.rightButton)).perform(click());
        Assert.assertEquals(currentGame,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartAtAugensaufen(){
        Intent intent = new Intent();
        MiniGame game = MiniGame.AUGENSAUFEN;
        intent.putExtra("lastGame", game);
        activityRule.launchActivity(intent);

        Assert.assertEquals(game,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartAtBiergeballer(){
        Intent intent = new Intent();
        MiniGame game = MiniGame.BIERGEBALLER;
        intent.putExtra("lastGame", game);
        activityRule.launchActivity(intent);

        Assert.assertEquals(game,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartAtBusfahren(){
        Intent intent = new Intent();
        MiniGame game = MiniGame.BUSFAHREN;
        intent.putExtra("lastGame", game);
        activityRule.launchActivity(intent);

        Assert.assertEquals(game,  activityRule.getActivity().getCurrentGame());
    }
    @Test
    public void testStartAtBierrutsche(){
        Intent intent = new Intent();
        MiniGame game = MiniGame.BIERRUTSCHE;
        intent.putExtra("lastGame", game);
        activityRule.launchActivity(intent);

        Assert.assertEquals(game,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartAtIchHabNochNie(){
        Intent intent = new Intent();
        MiniGame game = MiniGame.ICH_HAB_NOCH_NIE;
        intent.putExtra("lastGame", game);
        activityRule.launchActivity(intent);

        Assert.assertEquals(game,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartAtKings(){
        Intent intent = new Intent();
        MiniGame game = MiniGame.KINGS;
        intent.putExtra("lastGame", game);
        activityRule.launchActivity(intent);

        Assert.assertEquals(game,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartAtKistenStapeln(){
        Intent intent = new Intent();
        MiniGame game = MiniGame.KISTEN_STAPELN;
        intent.putExtra("lastGame", game);
        activityRule.launchActivity(intent);

        Assert.assertEquals(game,  activityRule.getActivity().getCurrentGame());
    }

    @Test
    public void testStartWerfDichDicht(){
        Intent intent = new Intent();
        MiniGame game = MiniGame.WERF_DICH_DICHT;
        intent.putExtra("lastGame", game);
        activityRule.launchActivity(intent);

        Assert.assertEquals(game,  activityRule.getActivity().getCurrentGame());
    }
}