package com.yy.codex.uikit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by cuiminghui on 2017/1/10.
 */

public class UIImage {

    public UIImage() {
        bitmap = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888);
    }

    public UIImage(Context context, int resID) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), resID);
    }

    public UIImage(byte[] data) {
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public UIImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /* Bitmap instance */

    private Bitmap bitmap = null;

    public Bitmap getBitmap() {
        return bitmap;
    }

}
