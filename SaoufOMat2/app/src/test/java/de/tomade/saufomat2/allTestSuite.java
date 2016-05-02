package de.tomade.saufomat2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.tomade.saufomat2.model.PlayerTest;
import de.tomade.saufomat2.model.card.CardTest;

/**
 * Created by woors on 07.04.2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({PlayerTest.class, CardTest.class})
public class allTestSuite {
}
