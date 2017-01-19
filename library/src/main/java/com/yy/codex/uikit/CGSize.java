package com.yy.codex.uikit;

import android.support.annotation.NonNull;

/**
 * Created by cuiminghui on 2017/1/3.
 */

public final class CGSize {

    final public double width;
    final public double height;

    public CGSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @NonNull
    public CGSize setWidth(double width) {
        return new CGSize(width, height);
    }

    @NonNull
    public CGSize setHeight(double height) {
        return new CGSize(width, height);
    }

    @NonNull @Override
    public String toString() {
        return "CGSize{mWidth=" + width + ",height=" + height + "}";
    }

}
