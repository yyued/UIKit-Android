package com.yy.codex.uikit;

/**
 * Created by cuiminghui on 2017/1/3.
 */

public final class CGRect {

    public CGPoint origin = new CGPoint(0, 0);
    public CGSize size = new CGSize(0, 0);

    public CGRect(double x, double y, double width, double height) {
        this.origin = new CGPoint(x, y);
        this.size = new CGSize(width, height);
    }

}
