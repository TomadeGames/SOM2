package de.tomade.saufomat2.activity.mainGame;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by woors on 04.08.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainGameActivityTest {
    @Rule
    public ActivityTestRule<MainGameActivity> activityTestRule = new ActivityTestRule<>(MainGameActivity.class, true, false);
}