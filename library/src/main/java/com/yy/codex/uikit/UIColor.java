package com.yy.codex.uikit;

import android.graphics.Color;

/**
 * Created by cuiminghui on 2017/1/16.
 */

public class UIColor {

    static UIColor blackColor = new UIColor(0, 0, 0, 1);
    static UIColor whiteColor = new UIColor(1, 1, 1, 1);
    static UIColor clearColor = new UIColor(0, 0, 0, 0);
    static UIColor redColor = new UIColor(1, 0, 0, 0);
    static UIColor greenColor = new UIColor(0, 1, 0, 0);
    static UIColor blueColor = new UIColor(0, 0, 1, 0);

    private double r = 0.0;
    private double g = 0.0;
    private double b = 0.0;
    private double a = 1.0;

    public UIColor(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public UIColor colorWithAlpha(double alpha) {
        return new UIColor(r, g, b, a * alpha);
    }

    public int toInt() {
        return Color.argb((int)(a * 255), (int)(r * 255), (int)(g * 255), (int)(b * 255));
    }

}
