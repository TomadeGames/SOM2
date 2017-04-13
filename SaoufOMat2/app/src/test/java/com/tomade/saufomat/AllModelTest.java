package com.tomade.saufomat;

import com.tomade.saufomat.activity.mainGame.task.TaskTest;
import com.tomade.saufomat.model.PlayerTest;
import com.tomade.saufomat.model.button.DrawableButtonTest;
import com.tomade.saufomat.model.card.CardTest;
import com.tomade.saufomat.model.drawable.DrawableImageTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

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
