package com.yy.codex.uikit;

import android.graphics.Typeface;
import android.support.annotation.Nullable;

/**
 * Created by cuiminghui on 2017/1/9.
 */

public class UIFont {

    @Nullable
    final public String fontFamily;
    final public float  fontSize;

    public UIFont(float fontSize) {
        this.fontFamily = "";
        this.fontSize = fontSize;
    }

    public UIFont(@Nullable String fontFamily, float fontSize) {
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
    }

    @Nullable
    public Typeface getTypeface() {
        return Typeface.create(fontFamily, Typeface.NORMAL);
    }

}
