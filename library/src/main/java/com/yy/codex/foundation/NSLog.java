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

    public static void log(@Nullable UIView view) {
        System.out.print("NSLog: " + view.toString());
        System.out.print(", Frame: ("+(int)view.getFrame().origin.x + ", " + (int)view.getFrame().origin.y+", "+(int)view.getFrame().size.getWidth()+", "+(int)view.getFrame().size.getHeight()+")");
        System.out.print("\n");
    }

}
