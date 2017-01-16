package com.yy.codex.uikit;

import android.graphics.Color;

/**
 * Created by cuiminghui on 2017/1/16.
 */

public class UIColor {

    public static final UIColor blackColor = new UIColor(0, 0, 0, 1);
    public static final UIColor whiteColor = new UIColor(1, 1, 1, 1);
    public static final UIColor clearColor = new UIColor(0, 0, 0, 0);
    public static final UIColor redColor = new UIColor(1, 0, 0, 1);
    public static final UIColor greenColor = new UIColor(0, 1, 0, 1);
    public static final UIColor blueColor = new UIColor(0, 0, 1, 1);

    private double mR = 0.0;
    private double mG = 0.0;
    private double mB = 0.0;
    private double mA = 1.0;

    public UIColor(double r, double g, double b, double a) {
        this.mR = r;
        this.mG = g;
        this.mB = b;
        this.mA = a;
    }

    public double getR() {
        return mR;
    }

    public double getG() {
        return mG;
    }

    public double getB() {
        return mB;
    }

    public double getA() {
        return mA;
    }

    public UIColor colorWithAlpha(double alpha) {
        return new UIColor(mR, mG, mB, mA * alpha);
    }

    public int toInt() {
        return Color.argb((int)(mA * 255), (int)(mR * 255), (int)(mG * 255), (int)(mB * 255));
    }

}
