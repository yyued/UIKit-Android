package com.yy.codex.uikit;

/**
 * Created by cuiminghui on 2017/1/16.
 */

public class UIEdgeInsets {

    static public UIEdgeInsets zero = new UIEdgeInsets(0, 0, 0, 0);

    final public double top;
    final public double left;
    final public double bottom;
    final public double right;

    public UIEdgeInsets(double top, double left, double bottom, double right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

}
