package com.yy.codex.uikit;

import android.graphics.Typeface;

/**
 * Created by cuiminghui on 2017/1/9.
 */

public class UIFont {

    public String fontFamily = "";
    public float  fontSize = 17;

    public UIFont(float fontSize) {
        this.fontSize = fontSize;
    }

    public UIFont(String fontFamily, float fontSize) {
        this.fontFamily = fontFamily;
    }

    public Typeface getTypeface() {
        return Typeface.create(fontFamily, Typeface.NORMAL);
    }

}
