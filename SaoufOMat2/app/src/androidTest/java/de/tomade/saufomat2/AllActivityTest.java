package de.tomade.saufomat2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.tomade.saufomat2.activity.ChooseMiniGameActivityTest;
import de.tomade.saufomat2.activity.CreatePlayerActivityTest;
import de.tomade.saufomat2.activity.miniGames.augensaufen.AugensaufenActivityTest;
import de.tomade.saufomat2.activity.miniGames.ichHabNochNie.IchHabNochNieActivityTest;
import de.tomade.saufomat2.activity.miniGames.kings.KingsActivityTest;
import de.tomade.saufomat2.activity.miniGames.werfDichDicht.WerfDichDichtActivityTest;
import de.tomade.saufomat2.busfahren.BusfahrenActivityTest;

/**
 * Created by woors on 04.08.2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        BusfahrenActivityTest.class,
        CreatePlayerActivityTest.class,
        ChooseMiniGameActivityTest.class,
        WerfDichDichtActivityTest.class,
        KingsActivityTest.class,
        IchHabNochNieActivityTest.class,
        AugensaufenActivityTest.class
})
public class AllActivityTest {
}
