package com.yy.codex.uikit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by cuiminghui on 2017/1/13.
 */

public class UIPinchGestureRecognizer extends UIGestureRecognizer {

    @Nullable private UITouch[] mStartTouches;
    private double mScaleCurrent = 1.0;
    @NonNull private CGPoint[] mScaleCurrentPoints = new CGPoint[2];
    private double mScaleInitial = 1.0;
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
        mStartTouches = touches;
    }

    @Override
    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesMoved(touches, event);
        if (mState == UIGestureRecognizerState.Possible) {
            if (touches.length < 2) {
                return;
            }
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
        mScaleCurrent = scale;
        mScaleInitial = scale;
        resetScaleInitialPoints();
    }

    public double getScale() {
        return mScaleCurrent;
    }

    private void resetScale() {
        if (mLastPoints.length >= 2) {
            mScaleCurrentPoints[0] = mLastPoints[0].getAbsolutePoint();
            mScaleCurrentPoints[1] = mLastPoints[1].getAbsolutePoint();
        }
        double initialLength = Math.sqrt(Math.pow(mScaleInitialPoints[0].x - mScaleInitialPoints[1].x, 2) + Math.pow(mScaleInitialPoints[0].y - mScaleInitialPoints[1].y, 2));
        double currentLength = Math.sqrt(Math.pow(mScaleCurrentPoints[0].x - mScaleCurrentPoints[1].x, 2) + Math.pow(mScaleCurrentPoints[0].y - mScaleCurrentPoints[1].y, 2));
        double screenLength =  Math.sqrt(Math.pow(UIScreen.mainScreen.bounds().size.width, 2) + Math.pow(UIScreen.mainScreen.bounds().size.height, 2));
        mScaleCurrent = mScaleInitial + (currentLength - initialLength) / screenLength;
    }

    private void resetScaleInitialPoints() {
        if (mLastPoints.length >= 2) {
            mScaleInitialPoints[0] = mLastPoints[0].getAbsolutePoint();
            mScaleInitialPoints[1] = mLastPoints[1].getAbsolutePoint();
        }
    }

}
