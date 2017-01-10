package com.yy.codex.uikit;


import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

/**
 * Created by it on 17/1/5.
 */

public class UIPanGestureRecognizer extends UIGestureRecognizer {

    protected VelocityTracker velocityTracker;//生命变量

    public UIPanGestureRecognizer(Object target, String selector) {
        super(target, selector);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        this.motionEvent = e2;
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(e2);
        velocityTracker.computeCurrentVelocity(1, (float)0.01);

        super.invokeSelector();
        return false;
    }

    public CGPoint velocityInView(UIView view) {
        return new CGPoint(velocityTracker.getXVelocity(), velocityTracker.getYVelocity());
    }

}
