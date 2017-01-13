package com.yy.codex.uikit;

/**
 * Created by adi on 17/1/13.
 */

public class CGTransformScale extends CGTransform {
    protected double sx;
    protected double sy;

    public CGTransformScale(double sx, double sy) {
        super(true);
        this.sx = sx;
        this.sy = sy;
    }
}
