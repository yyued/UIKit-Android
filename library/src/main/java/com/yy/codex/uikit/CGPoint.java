package com.yy.codex.uikit;

import android.support.annotation.NonNull;

/**
 * Created by cuiminghui on 2017/1/3.
 */

public final class CGPoint {

    private double mX = 0.0;
    private double mY = 0.0;

    public CGPoint(double x, double y) {
        this.mX = x;
        this.mY = y;
    }

    public double getX() {
        return this.mX;
    }

    public double getY() {
        return this.mY;
    }

    public boolean inRange(double xRange, double yRange, @NonNull CGPoint toPoint) {
        return Math.abs(toPoint.mX - this.mX) < xRange && Math.abs(toPoint.mY - this.mY) < yRange;
    }

    @NonNull
    @Override
    public String toString() {
        return "CGPoint{x=" + mX + ",y=" + mY + "}";
    }
}
