package com.yy.codex.uikit;

/**
 * Created by cuiminghui on 2017/1/16.
 */

public class UIEdgeInsets {

    double mTop;
    double mLeft;
    double mBottom;
    double mRight;

    public UIEdgeInsets(double top, double left, double bottom, double right) {
        this.mTop = top;
        this.mLeft = left;
        this.mBottom = bottom;
        this.mRight = right;
    }

    public double getTop() {
        return mTop;
    }

    public double getLeft() {
        return mLeft;
    }

    public double getBottom() {
        return mBottom;
    }

    public double getRight() {
        return mRight;
    }

}
