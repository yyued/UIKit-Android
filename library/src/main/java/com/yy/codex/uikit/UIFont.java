package com.yy.codex.uikit;

import android.graphics.Typeface;
import android.support.annotation.Nullable;

/**
 * Created by cuiminghui on 2017/1/9.
 */

public class UIFont {

    @Nullable
    public String fontFamily = "";
    public float  fontSize = 17;

    public UIFont(float fontSize) {
        this.fontSize = fontSize;
    }

    public UIFont(@Nullable String fontFamily, float fontSize) {
        this.fontFamily = fontFamily;
    }

    @Nullable
    public Typeface getTypeface() {
        return Typeface.create(fontFamily, Typeface.NORMAL);
    }

}
