package de.tomade.saufomat2.model.button;

import android.graphics.Bitmap;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Created by woors on 24.05.2016.
 */
public class DrawableButtonTest {
    DrawableButton button;

    @Before
    public void setUp(){
        Bitmap bitmap = Mockito.mock(Bitmap.class);
        button = new DrawableButton(bitmap, 0, 0, 100, 100);
    }

    @Test
    public void testAddListener() throws Exception {
        final ButtonEvent event = new ButtonEvent(this);
        button.addListener(new ButtonListener() {
            @Override
            public void onInput(ButtonEvent bevent) {
                Assert.assertEquals(event, bevent);
            }
        });
        button.fireEvent(event);
    }

    @Test
    public void testRemoveListener() throws Exception {
        ButtonListener listener = new ButtonListener() {
            @Override
            public void onInput(ButtonEvent event) {

            }
        };
        button.addListener(listener);
        Assert.assertTrue(button.removeListener(listener));
    }

    @Test
    public void testRemoveListenerFail() throws Exception{
        ButtonListener listener = new ButtonListener() {
            @Override
            public void onInput(ButtonEvent event) {

            }
        };
        Assert.assertFalse(button.removeListener(listener));
    }

    @Test
    public void testCheckClickObenLinks() throws Exception {
        float x = -1- button.getWith() / 2;
        float y = -1 - button.getHeight() / 2;
        Assert.assertFalse("erwartet: " + false + " erg: "  + button.checkClick(x,y), button.checkClick(x,y));
    }

    @Test
    public void testCheckClickOben() throws Exception {
        float x = -1 - button.getWith() / 2;
        float y = 0 - button.getHeight() / 2;
        Assert.assertFalse("erwartet: " + false + " erg: "  + button.checkClick(x,y), button.checkClick(x,y));
    }

    @Test
    public void testCheckClickOben2() throws Exception {
        float x = -1- button.getWith() / 2;
        float y = button.getHeight() - button.getHeight() / 2;
        Assert.assertFalse(button.checkClick(x,y));
    }

    @Test
    public void testCheckClickObenRechts() throws Exception {
        float x = -1- button.getWith() / 2;
        float y = button.getHeight()+1 - button.getHeight() / 2;
        Assert.assertFalse(button.checkClick(x,y));
    }

    @Test
    public void testCheckClickRechts() throws Exception {
        float x = 0- button.getWith() / 2;
        float y = button.getHeight()+1 - button.getHeight() / 2;
        Assert.assertFalse(button.checkClick(x,y));
    }

    @Test
    public void testCheckClickRechts2() throws Exception {
        float x = button.getWith()- button.getWith() / 2;
        float y = button.getHeight()+1 - button.getHeight() / 2;
        Assert.assertFalse(button.checkClick(x,y));
    }

    @Test
    public void testCheckClickRechtsUnten() throws Exception {
        float x = button.getWith() + 1- button.getWith() / 2;
        float y = button.getHeight()+1 - button.getHeight() / 2;
        Assert.assertFalse(button.checkClick(x,y));
    }

    @Test
    public void testCheckClickUnten() throws Exception {
        float x = 0- button.getWith() / 2;
        float y = button.getHeight()+1 - button.getHeight() / 2;
        Assert.assertFalse(button.checkClick(x,y));
    }

    @Test
    public void testCheckClickUnten2() throws Exception {
        float x = button.getWith()- button.getWith() / 2;
        float y = button.getHeight()+1 - button.getHeight() / 2;
        Assert.assertFalse(button.checkClick(x,y));
    }

    @Test
    public void testCheckClickUntenLinks() throws Exception {
        float x = -1- button.getWith() / 2;
        float y = button.getHeight()+1 - button.getHeight() / 2;
        Assert.assertFalse(button.checkClick(x,y));
    }

    @Test
    public void testCheckClickLinks() throws Exception {
        float x = -1- button.getWith() / 2;
        float y = 0 - button.getHeight() / 2;
        Assert.assertFalse("erwartet: " + false + " erg: "  + button.checkClick(x,y), button.checkClick(x,y));
    }

    @Test
    public void testCheckClickLinks2() throws Exception {
        float x = -1- button.getWith() / 2;
        float y = button.getHeight() - button.getHeight() / 2;
        Assert.assertFalse(button.checkClick(x,y));
    }

    @Test
    public void testCheckClickDrinLinksOben() throws Exception {
        float x = 0- button.getWith() / 2;
        float y = 0 - button.getHeight() / 2;
        Assert.assertTrue(button.checkClick(x,y));
    }

    @Test
    public void testCheckClickDrinRechtsOben() throws Exception {
        float x = button.getWith()- button.getWith() / 2;
        float y = 0 - button.getHeight() / 2;
        Assert.assertTrue(button.checkClick(x,y));
        Assert.assertTrue("erwartet: " + true + " erg: " + button.checkClick(x,y), button.checkClick(x,y));
    }

    @Test
    public void testCheckClickDrinRechtsUnten() throws Exception {
        float x = button.getWith()- button.getWith() / 2;
        float y = button.getHeight() - button.getHeight() / 2;
        Assert.assertTrue(button.checkClick(x,y));
        Assert.assertTrue("erwartet: " + true + " erg: " + button.checkClick(x,y), button.checkClick(x,y));
    }

    @Test
    public void testCheckClickDrinLinksUnten() throws Exception {
        float x = 0- button.getWith() / 2;
        float y = button.getHeight() - button.getHeight() / 2;
        Assert.assertTrue("erwartet: " + true + " erg: " + button.checkClick(x,y), button.checkClick(x,y));
    }
}