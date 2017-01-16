package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by cuiminghui on 2017/1/10.
 */

public class UIImage {

    public UIImage() {
        bitmap = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888);
    }

    public UIImage(@NonNull Context context, int resID) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), resID);
    }

    public UIImage(@NonNull byte[] data) {
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public UIImage(@NonNull Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /* Scale */

    private double scale = UIScreen.mainScreen.scale();

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    /* Bitmap instance */

    @Nullable private Bitmap bitmap = null;

    @Nullable
    public Bitmap getBitmap() {
        return bitmap;
    }

    public CGSize getSize() {
        if (bitmap == null) {
            return new CGSize(0, 0);
        }
        return new CGSize(bitmap.getWidth() / scale, bitmap.getHeight() / scale);
    }

}
