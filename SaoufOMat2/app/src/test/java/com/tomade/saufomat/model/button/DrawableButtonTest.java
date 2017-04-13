package com.tomade.saufomat.model.button;

import android.graphics.Bitmap;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by woors on 24.05.2016.
 */
public class DrawableButtonTest {
    DrawableButton button;

    @Before
    public void setUp() {
        Bitmap bitmap = Mockito.mock(Bitmap.class);
        this.button = new DrawableButton(bitmap, 0, 0, 100, 100);
    }

    @Test
    public void testAddListener() throws Exception {
        final ButtonEvent event = new ButtonEvent(this);
        this.button.addListener(new ButtonListener() {
            @Override
            public void onInput(ButtonEvent bevent) {
                Assert.assertEquals(event, bevent);
            }
        });
        this.button.fireEvent(event);
    }

    @Test
    public void testRemoveListener() throws Exception {
        ButtonListener listener = new ButtonListener() {
            @Override
            public void onInput(ButtonEvent event) {

            }
        };
        this.button.addListener(listener);
        Assert.assertTrue(this.button.removeListener(listener));
    }

    @Test
    public void testRemoveListenerFail() throws Exception {
        ButtonListener listener = new ButtonListener() {
            @Override
            public void onInput(ButtonEvent event) {

            }
        };
        Assert.assertFalse(this.button.removeListener(listener));
    }

    @Test
    public void testCheckClickObenLinks() throws Exception {
        float x = -1 - this.button.getWith() / 2;
        float y = -1 - this.button.getHeight() / 2;
        Assert.assertFalse("erwartet: " + false + " erg: " + this.button.checkClick(x, y), this.button.checkClick(x,
                y));
    }

    @Test
    public void testCheckClickOben() throws Exception {
        float x = -1 - this.button.getWith() / 2;
        float y = 0 - this.button.getHeight() / 2;
        Assert.assertFalse("erwartet: " + false + " erg: " + this.button.checkClick(x, y), this.button.checkClick(x,
                y));
    }

    @Test
    public void testCheckClickOben2() throws Exception {
        float x = -1 - this.button.getWith() / 2;
        float y = this.button.getHeight() - this.button.getHeight() / 2;
        Assert.assertFalse(this.button.checkClick(x, y));
    }

    @Test
    public void testCheckClickObenRechts() throws Exception {
        float x = -1 - this.button.getWith() / 2;
        float y = this.button.getHeight() + 1 - this.button.getHeight() / 2;
        Assert.assertFalse(this.button.checkClick(x, y));
    }

    @Test
    public void testCheckClickRechts() throws Exception {
        float x = 0 - this.button.getWith() / 2;
        float y = this.button.getHeight() + 1 - this.button.getHeight() / 2;
        Assert.assertFalse(this.button.checkClick(x, y));
    }

    @Test
    public void testCheckClickRechts2() throws Exception {
        float x = this.button.getWith() - this.button.getWith() / 2;
        float y = this.button.getHeight() + 1 - this.button.getHeight() / 2;
        Assert.assertFalse(this.button.checkClick(x, y));
    }

    @Test
    public void testCheckClickRechtsUnten() throws Exception {
        float x = this.button.getWith() + 1 - this.button.getWith() / 2;
        float y = this.button.getHeight() + 1 - this.button.getHeight() / 2;
        Assert.assertFalse(this.button.checkClick(x, y));
    }

    @Test
    public void testCheckClickUnten() throws Exception {
        float x = 0 - this.button.getWith() / 2;
        float y = this.button.getHeight() + 1 - this.button.getHeight() / 2;
        Assert.assertFalse(this.button.checkClick(x, y));
    }

    @Test
    public void testCheckClickUnten2() throws Exception {
        float x = this.button.getWith() - this.button.getWith() / 2;
        float y = this.button.getHeight() + 1 - this.button.getHeight() / 2;
        Assert.assertFalse(this.button.checkClick(x, y));
    }

    @Test
    public void testCheckClickUntenLinks() throws Exception {
        float x = -1 - this.button.getWith() / 2;
        float y = this.button.getHeight() + 1 - this.button.getHeight() / 2;
        Assert.assertFalse(this.button.checkClick(x, y));
    }

    @Test
    public void testCheckClickLinks() throws Exception {
        float x = -1 - this.button.getWith() / 2;
        float y = 0 - this.button.getHeight() / 2;
        Assert.assertFalse("erwartet: " + false + " erg: " + this.button.checkClick(x, y), this.button.checkClick(x,
                y));
    }

    @Test
    public void testCheckClickLinks2() throws Exception {
        float x = -1 - this.button.getWith() / 2;
        float y = this.button.getHeight() - this.button.getHeight() / 2;
        Assert.assertFalse(this.button.checkClick(x, y));
    }

    @Test
    public void testCheckClickDrinLinksOben() throws Exception {
        float x = 0 - this.button.getWith() / 2;
        float y = 0 - this.button.getHeight() / 2;
        Assert.assertTrue(this.button.checkClick(x, y));
    }

    @Test
    public void testCheckClickDrinRechtsOben() throws Exception {
        float x = this.button.getWith() - this.button.getWith() / 2;
        float y = 0 - this.button.getHeight() / 2;
        Assert.assertTrue(this.button.checkClick(x, y));
        Assert.assertTrue("erwartet: " + true + " erg: " + this.button.checkClick(x, y), this.button.checkClick(x, y));
    }

    @Test
    public void testCheckClickDrinRechtsUnten() throws Exception {
        float x = this.button.getWith() - this.button.getWith() / 2;
        float y = this.button.getHeight() - this.button.getHeight() / 2;
        Assert.assertTrue(this.button.checkClick(x, y));
        Assert.assertTrue("erwartet: " + true + " erg: " + this.button.checkClick(x, y), this.button.checkClick(x, y));
    }

    @Test
    public void testCheckClickDrinLinksUnten() throws Exception {
        float x = 0 - this.button.getWith() / 2;
        float y = this.button.getHeight() - this.button.getHeight() / 2;
        Assert.assertTrue("erwartet: " + true + " erg: " + this.button.checkClick(x, y), this.button.checkClick(x, y));
    }
}