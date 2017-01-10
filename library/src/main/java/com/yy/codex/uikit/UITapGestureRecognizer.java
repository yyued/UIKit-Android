package com.yy.codex.uikit;


import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

/**
 * Created by it on 17/1/4.
 */

public class UITapGestureRecognizer extends UIGestureRecognizer {

    public UITapGestureRecognizer(Object target, String selector) {
        super(target, selector);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        this.motionEvent = e;

        int numberOfTap = MotionEventCompat.getActionIndex(e);

        super.invokeSelector();
        return true;
    }
}
