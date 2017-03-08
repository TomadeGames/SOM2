package de.tomade.saufomat2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.tomade.saufomat2.activity.ChooseMiniGameActivityTest;
import de.tomade.saufomat2.activity.CreatePlayerActivityTest;

/**
 * Created by woors on 04.08.2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CreatePlayerActivityTest.class,
        ChooseMiniGameActivityTest.class
})
public class AllActivityTest {
}
