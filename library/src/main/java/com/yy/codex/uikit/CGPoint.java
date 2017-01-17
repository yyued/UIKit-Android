package com.yy.codex.uikit;

import android.support.annotation.NonNull;

/**
 * Created by cuiminghui on 2017/1/3.
 */

public final class CGPoint {

    final public double x;
    final public double y;

    public CGPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @NonNull
    public CGPoint setX(double x) {
        return new CGPoint(x, y);
    }

    @NonNull
    public CGPoint setY(double y) {
        return new CGPoint(x, y);
    }

    public boolean inRange(double xRange, double yRange, @NonNull CGPoint toPoint) {
        return Math.abs(toPoint.x - this.x) < xRange && Math.abs(toPoint.y - this.y) < yRange;
    }

    @NonNull @Override
    public String toString() {
        return "CGPoint{x=" + x + ",y=" + y + "}";
    }

}
