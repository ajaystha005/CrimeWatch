package com.mvp.crimewatch.helper;

import android.graphics.PointF;

/**
 * Created by ajayshrestha on 3/19/17.
 */


/**
 * This is helper class to clone the functionality of Java AWT Point2D
 */
public class Point2D extends PointF {

    public double x;
    public double y;

    public Point2D() {

    }

    public Point2D(double x, double y) {
        this.set((float) x, (float) y);
        this.x = x;
        this.y = y;
    }

    public final void set(double x, double y) {
        this.set((float) x, (float) y);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
