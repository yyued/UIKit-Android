package com.yy.codex.uikit;

import android.graphics.Color;
import android.support.annotation.NonNull;

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
    public static final UIColor grayColor = new UIColor(.3, .3, .3, 1);
    public static final UIColor orangeColor = new UIColor(1, .38, 0, 1);
    public static final UIColor yellowColor = new UIColor(1, 1, 0, 1);


    final public double r;
    final public double g;
    final public double b;
    final public double a;

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

    @NonNull
    @Override
    public String toString() {
        return "UIColor, r = " + r + ", g = " + g + ", b = " + b + ", a = " + a + "";
    }

}
