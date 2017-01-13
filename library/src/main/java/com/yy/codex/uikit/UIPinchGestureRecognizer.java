package com.yy.codex.uikit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by cuiminghui on 2017/1/13.
 */

public class UIPinchGestureRecognizer extends UIGestureRecognizer {

    @Nullable private UITouch[] mStartTouches;
    private double mScale = 1.0;
    @NonNull private CGPoint[] mScaleInitialPoints = new CGPoint[2];

    public UIPinchGestureRecognizer(@NonNull Object target, @NonNull String selector) {
        super(target, selector);
    }

    public UIPinchGestureRecognizer(@NonNull Runnable triggerBlock) {
        super(triggerBlock);
    }

    @Override
    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesBegan(touches, event);
        if (touches.length != 2) {
            mState = UIGestureRecognizerState.Failed;
            return;
        }
        mStartTouches = touches;
    }

    @Override
    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesMoved(touches, event);
        if (mState == UIGestureRecognizerState.Possible) {
            if (touches.length != 2) {
                mState = UIGestureRecognizerState.Failed;
                return;
            }
            resetScaleInitialPoints();
            resetScale();
            mState = UIGestureRecognizerState.Began;
            sendActions();
        }
        else if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            mState = UIGestureRecognizerState.Changed;
            resetScale();
            sendActions();
        }
    }

    @Override
    public void touchesEnded(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesEnded(touches, event);
        if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            mState = UIGestureRecognizerState.Ended;
            sendActions();
        }
    }

    @Override
    int gesturePriority() {
        return super.gesturePriority() + 99;
    }

    public void setScale(double scale) {
        mScale = scale;
        resetScaleInitialPoints();
    }

    public double getScale() {
        return mScale;
    }

    private void resetScale() {
        double initialLegth = Math.sqrt(Math.pow(mScaleInitialPoints[0].getX() - mScaleInitialPoints[1].getX(), 2) + Math.pow(mScaleInitialPoints[0].getY() - mScaleInitialPoints[1].getY(), 2));
        NSLog.log(initialLegth);
    }

    private void resetScaleInitialPoints() {
        if (lastPoints.length >= 2) {
            mScaleInitialPoints[0] = lastPoints[0].getAbsolutePoint();
            mScaleInitialPoints[1] = lastPoints[1].getAbsolutePoint();
        }
    }

}
