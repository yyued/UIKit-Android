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
        return new RectF((float) origin.x, (float) origin.y, (float) (origin.x + size.getWidth()), (float) (origin.y + size.getHeight()));
    }

    @NonNull
    public RectF toRectF(@NonNull CGPoint point) {
        float top = (float) point.y;
        float left = (float) point.x;
        float right = (float) size.getWidth() + left;
        float bottom = (float) size.getHeight() + top;
        return new RectF(left, top, right, bottom);
    }

    @NonNull
    public Rect toRect() {
        return new Rect((int) origin.x, (int) origin.y, (int) (origin.x + size.getWidth()), (int) (origin.y + size.getHeight()));
    }

    @NonNull
    public RectF shrinkToRectF(float offset){
        float top = (float) origin.y + offset;
        float left = (float) origin.x + offset;
        float right = (float) size.getWidth() + left - 2 * offset;
        float bottom = (float) size.getHeight() + top - 2 * offset;
        return new RectF(left, top, right, bottom);
    }

    @NonNull
    public RectF shrinkToRectF(float offset, @NonNull CGPoint point){
        float top = (float) point.y + offset;
        float left = (float) point.x + offset;
        float right = (float) size.getWidth() + left - 2 * offset;
        float bottom = (float) size.getHeight() + top - 2 * offset;
        return new RectF(left, top, right, bottom);
    }

    @NonNull
    public CGRect setX(double x) {
        return new CGRect(x, this.origin.y, this.size.getWidth(), this.size.getHeight());
    }

    public double getX() {
        return origin.x;
    }

    @NonNull
    public CGRect setY(double y) {
        return new CGRect(this.origin.x, y, this.size.getWidth(), this.size.getHeight());
    }

    public double getY() {
        return origin.y;
    }

    @NonNull
    public CGRect setWidth(double width) {
        return new CGRect(this.origin.x, this.origin.y, width, this.size.getHeight());
    }

    public double getWidth() {
        return size.getWidth();
    }

    @NonNull
    public CGRect setHeight(double height) {
        return new CGRect(this.origin.x, this.origin.y, this.size.getWidth(), height);
    }

    public double getHeight() {
        return size.getHeight();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj != null && CGRect.class.isAssignableFrom(obj.getClass())) {
            CGRect anObj = (CGRect) obj;
            boolean equal = Math.abs(origin.x - anObj.origin.x) < 0.01 &&
                            Math.abs(origin.y - anObj.origin.y) < 0.01 &&
                            Math.abs(size.getWidth() - anObj.size.getWidth()) < 0.01 &&
                            Math.abs(size.getHeight() - anObj.size.getHeight()) < 0.01;
            return equal;
        }
        return false;
    }

}
