package com.yy.codex.uikit;

/**
 * Created by adi on 17/1/13.
 */

public class CGTransformTranslation extends CGTransform {
    protected double tx;
    protected double ty;

    public CGTransformTranslation(double tx, double ty) {
        super(true);
        this.tx = tx;
        this.ty = ty;
    }
}
