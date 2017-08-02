package com.tomade.saufomat.activity;

import android.util.Log;
import android.view.MotionEvent;

import com.tomade.saufomat.constant.Direction;

/**
 * Hilfsklasse für Swipe-Eingaben
 * Created by woors on 02.08.2017.
 */

public class SwipeController {
    private static final String TAG = SwipeController.class.getSimpleName();
    private float startX;
    private float startY;
    private Direction directionX;
    private Direction directionY;
    private float distanceX;
    private float distanceY;
    private float distance;
    private long duration;

    public SwipeController() {
        this.resetSwipe();
    }

    private void resetSwipe() {
        this.distanceX = -1;
        this.distanceY = -1;
        this.distance = -1;
        this.duration = -1;
    }

    public void startSwipe(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            this.startX = motionEvent.getX();
            this.startY = motionEvent.getY();
            Log.d(TAG, "Swipe started at (" + this.startX + ", " + this.startY + ")");
            this.resetSwipe();
        } else {
            Log.e(TAG, "Swipe started in wrong MotionEvent Action: " + motionEvent.getAction() + " should be " +
                    "ACTION_DOWN " +

                    "(" + MotionEvent.ACTION_DOWN + ")");
        }
    }

    /**
     * Beendet den Swipe
     *
     * @param motionEvent
     */
    public void endSwipe(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            float endX = motionEvent.getX();
            float endY = motionEvent.getY();
            this.distanceX = this.amount(this.startX - endX);
            this.distanceY = this.amount(this.startY - endY);
            this.distance = (float) Math.sqrt(((this.distanceX * this.distanceX) + (this.distanceY + this.distanceY)));
            this.duration = motionEvent.getEventTime() - motionEvent.getDownTime();
            if (this.startX < endX) {
                this.directionX = Direction.RIGHT;
            } else {
                this.directionX = Direction.LEFT;
            }
            if (this.startY > endY) {
                this.directionY = Direction.UP;
            } else {
                this.directionY = Direction.DOWN;
            }
            Log.d(TAG, "Swipe ended: distanceX(" + this.distanceX + ") distanceY(" + this.distanceY + ") distance(" +
                    this.distance + ") duration(" + this.duration + ") directionX(" + this.directionX + ") directionY" +
                    "(" + this.directionY + ")");
        } else {
            Log.e(TAG, "Swipe end in wrong MotionEvent Action: " + motionEvent.getAction() + " should be " +
                    "ACTION_UP (" + MotionEvent.ACTION_UP + ")");
        }
    }

    private float amount(float distance) {
        if (distance < 0) {
            return -distance;
        }
        return distance;
    }

    /**
     * Gibt die Swipedistanz in X-Richtung zurück oder -1, wenn kein Swipe statt fand
     *
     * @return Swipedistanz in X-Richtung oder -1
     */
    public float getDistanceX() {
        return this.distanceX;
    }

    /**
     * Gibt die Swipedistanz in Y-Richtung zurück oder -1, wenn kein Swipe statt fand
     *
     * @return Swipedistanz in Y-Richtung oder -1
     */
    public float getDistanceY() {
        return this.distanceY;
    }

    /**
     * Gibt die Swipedistanz zurück oder -1, wenn kein Swipe statt fand
     *
     * @return Swipedistanz oder -1
     */
    public float getDistance() {
        return this.distance;
    }

    /**
     * Gibt die Swipezeit zurück oder -1, wenn kein Swipe statt fand
     *
     * @return Swipezeit oder -1
     */
    public long getDuration() {
        return this.duration;
    }

    /**
     * Verarbeitet Swipe-Eingaben
     *
     * @param motionEvent ein MotionEvent
     * @return true, wenn Swipe abgeschlossen wurde
     */
    public boolean handleSwipe(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            this.startSwipe(motionEvent);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            this.endSwipe(motionEvent);
            return true;
        }
        return false;
    }

    public Direction getDirectionY() {
        return this.directionY;
    }

    public Direction getDirectionX() {
        return this.directionX;
    }
}