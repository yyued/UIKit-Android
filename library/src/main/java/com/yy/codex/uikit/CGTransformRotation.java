package com.yy.codex.uikit;

/**
 * Created by adi on 17/1/13.
 */

public class CGTransformRotation extends CGTransform {

    private double angle;

    public CGTransformRotation(double angle) {
        super(true);
        this.angle = angle;
    }
}
