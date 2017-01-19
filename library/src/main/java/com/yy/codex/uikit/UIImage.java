package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.util.Base64;

/**
 * Created by cuiminghui on 2017/1/10.
 */

public class UIImage {

    public enum RenderingMode {
        Automatic,
        AlwaysOriginal,
        AlwaysTemplate
    }

    private final static LruCache<Number, Bitmap> sResCache = new LruCache<>(8 * 1024 * 1024);

    public UIImage() {
        mBitmap = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888);
    }

    public UIImage(@NonNull Context context, int resID) {
        if (sResCache.get(resID) instanceof Bitmap) {
            mBitmap = sResCache.get(resID);
        }
        else {
            mBitmap = BitmapFactory.decodeResource(context.getResources(), resID);
            sResCache.put(resID, mBitmap);
        }
    }

    public UIImage(@NonNull byte[] data) {
        mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public UIImage(@NonNull String base64String) {
        byte[] data = Base64.decode(base64String, Base64.DEFAULT);
        mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public UIImage(@NonNull Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    /* Scale */

    private double mScale = UIScreen.mainScreen.scale();

    public double getScale() {
        return mScale;
    }

    public void setScale(double scale) {
        this.mScale = scale;
    }

    /* RenderingMode */

    private RenderingMode mRenderingMode = RenderingMode.Automatic;

    public RenderingMode getRenderingMode() {
        return mRenderingMode;
    }

    public void setRenderingMode(RenderingMode renderingMode) {
        this.mRenderingMode = renderingMode;
    }

    /* Bitmap instance */

    @Nullable final protected Bitmap mBitmap;

    @Nullable
    public Bitmap getBitmap() {
        return mBitmap;
    }

    public CGSize getSize() {
        if (mBitmap == null) {
            return new CGSize(0, 0);
        }
        return new CGSize(Math.ceil(mBitmap.getWidth() / mScale), Math.ceil(mBitmap.getHeight() / mScale));
    }

}
