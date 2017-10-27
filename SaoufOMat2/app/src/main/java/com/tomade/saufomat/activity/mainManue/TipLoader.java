package com.tomade.saufomat.activity.mainManue;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Läd Tipps aus der Datei in denen sie definiert sind
 * Created by woors on 27.10.2017.
 */

public class TipLoader {
    private static final String FILENAME = "raw/tips.txt";
    private static final String TAG = TipLoader.class.getSimpleName();

    private static List<String> loadTips(Context context) {
        BufferedReader reader = null;
        List<String> tips = new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(FILENAME)));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    tips.add(line);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "File " + FILENAME + " not found", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Cannot close File " + FILENAME, e);
                }
            }
        }
        if (tips.isEmpty()) {
            tips.add("404!");
        }
        return tips;
    }

    public static String getRandomTip(Context context) {
        List<String> tips = loadTips(context);
        int index = new Random(System.currentTimeMillis()).nextInt(tips.size());
        return tips.get(index);
    }
}
