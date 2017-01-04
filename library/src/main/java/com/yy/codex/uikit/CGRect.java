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

    public CGRect setX(double x) {
        return new CGRect(x, this.origin.getY(), this.size.getWidth(), this.size.getHeight());
    }

    public CGRect setY(double y) {
        return new CGRect(this.origin.getX(), y, this.size.getWidth(), this.size.getHeight());
    }

    public CGRect setWidth(double width) {
        return new CGRect(this.origin.getX(), this.origin.getY(), width, this.size.getHeight());
    }

    public CGRect setHeight(double height) {
        return new CGRect(this.origin.getX(), this.origin.getY(), this.size.getWidth(), height);
    }

    @Override
    public boolean equals(Object obj) {
        if (CGRect.class.isAssignableFrom(obj.getClass())) {
            CGRect anObj = (CGRect) obj;
            boolean equal = Math.abs(origin.getX() - anObj.origin.getX()) < 0.01 &&
                            Math.abs(origin.getY() - anObj.origin.getY()) < 0.01 &&
                            Math.abs(size.getWidth() - anObj.size.getWidth()) < 0.01 &&
                            Math.abs(size.getHeight() - anObj.size.getHeight()) < 0.01;
            return equal;
        }
        return false;
    }
}
