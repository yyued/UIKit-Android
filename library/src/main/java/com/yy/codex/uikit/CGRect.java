package com.yy.codex.uikit;

import android.graphics.Rect;
import android.graphics.RectF;

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

    public RectF toRectF() {
        return new RectF((float) origin.getX(), (float) origin.getY(), (float) (origin.getX() + size.getWidth()), (float) (origin.getY() + size.getHeight()));
    }
    public RectF toRectF(CGPoint point) {
        float top = (float) point.getY();
        float left = (float) point.getX();
        float right = (float) size.getWidth() + left;
        float bottom = (float) size.getHeight() + top;
        return new RectF(left, top, right, bottom);
    }
    public Rect toRect() {
        return new Rect((int) origin.getX(), (int) origin.getY(), (int) (origin.getX() + size.getWidth()), (int) (origin.getY() + size.getHeight()));
    }

    public RectF shrinkToRectF(float offset){
        float top = (float) origin.getY() + offset;
        float left = (float) origin.getX() + offset;
        float right = (float) size.getWidth() + left - 2 * offset;
        float bottom = (float) size.getHeight() + top - 2 * offset;
        return new RectF(left, top, right, bottom);
    }

    public RectF shrinkToRectF(float offset, CGPoint point){
        float top = (float) point.getY() + offset;
        float left = (float) point.getX() + offset;
        float right = (float) size.getWidth() + left - 2 * offset;
        float bottom = (float) size.getHeight() + top - 2 * offset;
        return new RectF(left, top, right, bottom);
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

    public CGSize getSize(){
        return size;
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
