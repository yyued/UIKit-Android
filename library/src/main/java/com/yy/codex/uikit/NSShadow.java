package com.yy.codex.uikit;

import android.graphics.Color;

/**
 * Created by cuiminghui on 2017/1/9.
 */

public class NSShadow {

    public CGSize shadowOffset = new CGSize(1, 1);
    public double shadowBlurRadius = 1;
    public int    shadowColor = Color.BLACK;

    public NSShadow() {}

    public NSShadow(CGSize shadowOffset, double shadowBlurRadius, int shadowColor) {
        this.shadowOffset = shadowOffset;
        this.shadowBlurRadius = shadowBlurRadius;
        this.shadowColor = shadowColor;
    }

}
