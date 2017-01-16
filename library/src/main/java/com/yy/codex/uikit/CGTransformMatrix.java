package com.yy.codex.uikit;

import android.graphics.Matrix;

/**
 * Created by adi on 17/1/13.
 */

public class CGTransformMatrix extends CGTransform {
    protected double a;
    protected double b;
    protected double c;
    protected double d;
    protected double tx;
    protected double ty;

    private Matrix matrix;

    public CGTransformMatrix(double a, double b, double c, double d, double tx, double ty) {
        super(true);
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.tx = tx;
        this.ty = ty;
    }
}
