package com.yy.codex.uikit;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by it on 17/1/4.
 */

public class UIGestureRecognizer extends GestureDetector.SimpleOnGestureListener {

    UIView view;
    private NSInvocation invocation;

    protected MotionEvent motionEvent;

    public UIGestureRecognizer(Object target, String selector) {
        invocation = new NSInvocation(target, selector);
    }

    protected void invokeSelector() {
        if (invocation != null) {
            try {
                invocation.invoke(new Object[]{this});
            } catch (Exception e) {}
        }
    }

    public UIView getView() {
        return view;
    }

    public CGPoint locationInView(UIView view) {
        if (motionEvent != null) {
            return new CGPoint(motionEvent.getX(), motionEvent.getY());
        }
        return new CGPoint(0, 0);
    }

}
