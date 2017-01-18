package com.yy.codex.foundation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yy.codex.uikit.UIView;

/**
 * Created by cuiminghui on 2017/1/12.
 */

public class NSLog {

    public static void log(@Nullable Object object) {
        System.out.println("NSLog: " + object.toString());
    }

    public static void warn(@Nullable Object object){
        Log.w("NSLog", object.toString());
    }

}
