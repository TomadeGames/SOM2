package com.tomade.saufomat;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.SparseArray;

/**
 * Läd Bilddateien und Änliches und Cached diese
 * Created by woors on 12.07.2017.
 */

public class ContentLoader {
    private static final String TAG = ContentLoader.class.getSimpleName();
    /**
     * Liste für alle bereits geladenen Bilder
     */
    private static SparseArray<Bitmap> cachedImages = new SparseArray<>();

    /**
     * Gibt ein Bild aus den Resourcen skaliert zurück.
     *
     * @param resources die Resourcen, aus denen Geladen werden soll
     * @param id        die Id des Bildes
     * @param width     die Breite auf die das Bild skaliert werden soll
     * @param height    die Höhe auf die das Bild skaliert werden soll
     * @return das geladene Bild
     */
    public static Bitmap getImage(Resources resources, int id, int width, int height) {
        Bitmap image = cachedImages.get(id);
        if (image == null) {
            image = BitmapFactory.decodeResource(resources, id);
            image = Bitmap.createScaledBitmap(image, width, height, true);
            cachedImages.put(id, image);
            Log.i(TAG, "new Image [" + id + "] loaded. Total Images loaded: " + cachedImages.size());
        }
        return image;
    }

    /**
     * Gibt ein Bild aus den Resourcen unskaliert zurück.
     *
     * @param resources die Resourcen, aus denen Geladen werden soll
     * @param id        die Id des Bildes
     * @return das geladene Bild
     */
    public static Bitmap getImage(Resources resources, int id) {
        Bitmap image = cachedImages.get(id);
        if (image == null) {
            image = BitmapFactory.decodeResource(resources, id);
            cachedImages.put(id, image);
            Log.i(TAG, "new Image [" + id + "] loaded. Total Images loaded: " + cachedImages.size());
        }
        return image;
    }
}
