package com.yy.codex.uikit;

/**
 * Created by adi on 17/1/17.
 */

public class CGShadow {

    public final double radius;
    public final double offsetX;
    public final double offsetY;
    public final UIColor color;

    public CGShadow(double radius, double offsetX, double offsetY, UIColor color) {
        this.radius = radius;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.color = color;
    }

    public CGShadow setRadius(double radius) {
        return new CGShadow(radius, this.offsetX, this.offsetY, this.color);
    }

    public CGShadow setOffsetX(double offsetX){
        return new CGShadow(this.radius, offsetX, this.offsetY, this.color);
    }

    public CGShadow setOffsetY(double offsetY){
        return new CGShadow(this.radius, this.offsetX, offsetY, this.color);
    }

    public CGShadow setOffsetX(UIColor color){
        return new CGShadow(this.radius, offsetX, this.offsetY, color);
    }

}
