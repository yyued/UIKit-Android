package com.yy.codex.uikit;

import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by cuiminghui on 2017/1/3.
 */

public final class CGRect {

    @NonNull final public CGPoint origin;
    @NonNull final public CGSize size;

    public CGRect(double x, double y, double width, double height) {
        this.origin = new CGPoint(x, y);
        this.size = new CGSize(width, height);
    }

    @NonNull
    public RectF toRectF() {
        return new RectF((float) origin.x, (float) origin.y, (float) (origin.x + size.width), (float) (origin.y + size.height));
    }

    @NonNull
    public RectF toRectF(@NonNull CGPoint point) {
        float top = (float) point.y;
        float left = (float) point.x;
        float right = (float) size.width + left;
        float bottom = (float) size.height + top;
        return new RectF(left, top, right, bottom);
    }

    @NonNull
    public Rect toRect() {
        return new Rect((int) origin.x, (int) origin.y, (int) (origin.x + size.width), (int) (origin.y + size.height));
    }

    @NonNull
    public RectF shrinkToRectF(float offset){
        float top = (float) origin.y + offset;
        float left = (float) origin.x + offset;
        float right = (float) size.width + left - 2 * offset;
        float bottom = (float) size.height + top - 2 * offset;
        return new RectF(left, top, right, bottom);
    }

    @NonNull
    public RectF shrinkToRectF(float offset, @NonNull CGPoint point){
        float top = (float) point.y + offset;
        float left = (float) point.x + offset;
        float right = (float) size.width + left - 2 * offset;
        float bottom = (float) size.height + top - 2 * offset;
        return new RectF(left, top, right, bottom);
    }

    @NonNull
    public CGRect setX(double x) {
        return new CGRect(x, this.origin.y, this.size.width, this.size.height);
    }

    public double getX() {
        return origin.x;
    }

    @NonNull
    public CGRect setY(double y) {
        return new CGRect(this.origin.x, y, this.size.width, this.size.height);
    }

    public double getY() {
        return origin.y;
    }

    @NonNull
    public CGRect setWidth(double width) {
        return new CGRect(this.origin.x, this.origin.y, width, this.size.height);
    }

    public double getWidth() {
        return size.width;
    }

    @NonNull
    public CGRect setHeight(double height) {
        return new CGRect(this.origin.x, this.origin.y, this.size.width, height);
    }

    public double getHeight() {
        return size.height;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj != null && CGRect.class.isAssignableFrom(obj.getClass())) {
            CGRect anObj = (CGRect) obj;
            boolean equal = Math.abs(origin.x - anObj.origin.x) < 0.01 &&
                            Math.abs(origin.y - anObj.origin.y) < 0.01 &&
                            Math.abs(size.width - anObj.size.width) < 0.01 &&
                            Math.abs(size.height - anObj.size.height) < 0.01;
            return equal;
        }
        return false;
    }

    @NonNull @Override
    public String toString() {
        return "CGRect > origin = " + origin.toString() + ", size = " + size.toString();
    }

}
