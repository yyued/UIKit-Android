package com.yy.codex.foundation;

import android.support.annotation.Nullable;

import com.yy.codex.uikit.UIView;

/**
 * Created by cuiminghui on 2017/1/12.
 */

public class NSLog {

    public static void log(@Nullable Object object) {
        System.out.println("NSLog: " + object.toString());
    }

}
