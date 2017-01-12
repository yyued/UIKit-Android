package com.yy.codex.uikit;

import android.support.annotation.Nullable;

/**
 * Created by cuiminghui on 2017/1/12.
 */

public class NSLog {

    public static void log(@Nullable Object object) {
        System.out.println("NSLog: " + object.toString());
    }

    public static void log(@Nullable UIView view) {
        System.out.print("NSLog: " + view.toString());
        System.out.print(", Frame: ("+(int)view.getFrame().origin.getX() + ", " + (int)view.getFrame().origin.getY()+", "+(int)view.getFrame().size.getWidth()+", "+(int)view.getFrame().size.getHeight()+")");
        System.out.print("\n");
    }

}
