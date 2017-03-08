package de.tomade.saufomat2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.tomade.saufomat2.activity.mainGame.task.TaskTest;
import de.tomade.saufomat2.model.PlayerTest;
import de.tomade.saufomat2.model.button.DrawableButtonTest;
import de.tomade.saufomat2.model.card.CardTest;
import de.tomade.saufomat2.model.drawable.DrawableImageTest;

/**
 * Created by woors on 04.08.2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        DrawableButtonTest.class,
        CardTest.class,
        DrawableImageTest.class,
        PlayerTest.class,
        TaskTest.class,
})
public class AllModelTest {
}
