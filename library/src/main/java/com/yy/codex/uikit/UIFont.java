package com.yy.codex.uikit;

import android.graphics.Typeface;
import android.support.annotation.Nullable;

import static android.graphics.Typeface.DEFAULT;
import static android.graphics.Typeface.DEFAULT_BOLD;

/**
 * Created by cuiminghui on 2017/1/9.
 */

public class UIFont {

    @Nullable
    final public String fontFamily;
    final public float  fontSize;

    static public UIFont systemBold(float fontSize) {
        return new UIFont("SystemBold", fontSize);
    }

    public UIFont(float fontSize) {
        this.fontFamily = "System";
        this.fontSize = fontSize;
    }

    public UIFont(@Nullable String fontFamily, float fontSize) {
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
    }

    @Nullable
    public Typeface getTypeface() {
        if (fontFamily.equalsIgnoreCase("System")) {
            return DEFAULT;
        }
        else if (fontFamily.equalsIgnoreCase("SystemBold")) {
            return DEFAULT_BOLD;
        }
        return Typeface.create(fontFamily, Typeface.NORMAL);
    }

}
