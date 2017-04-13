package com.tomade.saufomat.model.drawable;

import android.graphics.Bitmap;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Random;

/**
 * Created by woors on 24.05.2016.
 */
public class DrawableImageTest {

    @Test
    public void testConstructor() {
        Bitmap bitmap = Mockito.mock(Bitmap.class);
        Random rnd = new Random(System.currentTimeMillis());
        int x = rnd.nextInt();
        int y = rnd.nextInt();
        int width = rnd.nextInt();
        int height = rnd.nextInt();
        DrawableImage img = new DrawableImage(bitmap, x, y, width, height);
        Assert.assertEquals(x, img.getX());
        Assert.assertEquals(y, img.getY());
        Assert.assertEquals(width, img.getWith());
        Assert.assertEquals(height, img.getHeight());
    }
}
