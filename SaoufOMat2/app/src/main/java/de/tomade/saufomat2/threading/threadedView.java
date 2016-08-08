package de.tomade.saufomat2.threading;

import android.graphics.Canvas;

/**
 * Created by woors on 30.05.2016.
 */
public interface ThreadedView {
    void update();

    void render(Canvas canvas);

    void setElapsedTime(long elapsedTime);

    void setAvgFps(String avgFps);
}
