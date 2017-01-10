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

    private Object target;
    private String selector;

    protected MotionEvent motionEvent;

    public UIGestureRecognizer(Object target, String selector) {
        this.target = target;
        this.selector = selector;
    }

    protected void invokeSelector() {
        try {
            Class clazz = target.getClass();

            Method method = null;
            int index = selector.indexOf(":");
            if (index != -1) {
                String subSelector = selector.substring(0, index);
                method = clazz.getDeclaredMethod(subSelector, UIGestureRecognizer.class);
                method.invoke(target, this);
            }
            else  {
                method = clazz.getDeclaredMethod(selector);
                method.invoke(target);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public CGPoint locationInView(UIView view) {
        if (motionEvent != null) {
            return new CGPoint(motionEvent.getX(), motionEvent.getY());
        }
        return new CGPoint(0, 0);
    }
}
