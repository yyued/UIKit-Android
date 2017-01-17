package com.yy.codex.uikit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yy.codex.foundation.NSLog;

/**
 * Created by PonyCui_Home on 2017/1/11.
 */

public class UIPanGestureRecognizer extends UIGestureRecognizer {

    @Nullable UITouch[] mMaxTouches;
    @NonNull CGPoint[] mTranslatePoint = new CGPoint[0];
    @NonNull CGPoint mVelocityPoint = new CGPoint(0, 0);

    public UIPanGestureRecognizer(@NonNull Object target, @NonNull String selector) {
        super(target, selector);
    }

    public UIPanGestureRecognizer(@NonNull Runnable triggerBlock) {
        super(triggerBlock);
    }

    @Override
    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesBegan(touches, event);
        if (touches.length > 1) {
            mState = UIGestureRecognizerState.Failed;
            return;
        }
        mMaxTouches = touches;
        setTranslation(new CGPoint(0, 0));
    }

    @Override
    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            resetVelocity(touches);
        }
        super.touchesMoved(touches, event);
        if (mMaxTouches == null) {
            mState = UIGestureRecognizerState.Failed;
            return;
        }
        if (mState == UIGestureRecognizerState.Possible && moveOutOfBounds(touches)) {
            setTranslation(new CGPoint(-translation().x, -translation().y));
            mState = UIGestureRecognizerState.Began;
            sendActions();
        }
        else if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            if (touches.length > mMaxTouches.length) {
                mMaxTouches = touches;
                bonusTranslation();
            }
            else if (touches.length < mMaxTouches.length) {
                mState = UIGestureRecognizerState.Ended;
                sendActions();
                return;
            }
            mState = UIGestureRecognizerState.Changed;
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
        else if (mState == UIGestureRecognizerState.Ended) {
            // Because as least one finger touch up, turns Ended during Moved.
        }
        else {
            mState = UIGestureRecognizerState.Failed;
        }
    }

    @NonNull
    public CGPoint translation() {
        if (mLastPoints.length > 0 && mTranslatePoint != null && mLastPoints.length == mTranslatePoint.length) {
            double sumX = 0.0;
            double sumY = 0.0;
            for (int i = 0; i < mLastPoints.length; i++) {
                sumX += mLastPoints[i].getAbsolutePoint().x - mTranslatePoint[i].x;
                sumY += mLastPoints[i].getAbsolutePoint().y - mTranslatePoint[i].y;
            }
            return new CGPoint(sumX, sumY);
        }
        return new CGPoint(0, 0);
    }

    public void setTranslation(@NonNull CGPoint point) {
        if (mLastPoints.length > 0) {
            mTranslatePoint = new CGPoint[mLastPoints.length];
            for (int i = 0; i < mLastPoints.length; i++) {
                mTranslatePoint[i] = new CGPoint(
                    mLastPoints[i].getAbsolutePoint().x + point.x,
                    mLastPoints[i].getAbsolutePoint().y + point.y
                );
            }
        }
    }

    public void bonusTranslation() {
        if (mTranslatePoint.length < mLastPoints.length) {
            CGPoint[] translatePoint = new CGPoint[mLastPoints.length];
            for (int i = 0; i < mLastPoints.length; i++) {
                if (i < mTranslatePoint.length) {
                    translatePoint[i] = mTranslatePoint[i];
                }
                else {
                    translatePoint[i] = mLastPoints[i].getAbsolutePoint();
                }
            }
            mTranslatePoint = translatePoint;
        }
    }

    @NonNull
    public CGPoint velocity() {
        return mVelocityPoint;
    }

    private void resetVelocity(@NonNull UITouch[] nextTouches) {
        if (mLastPoints.length > 0 && nextTouches.length > 0) {
            double ts = ((double)(nextTouches[0].getTimestamp() - mLastPoints[0].getTimestamp()) / 1000.0);
            if (ts == 0.0) { }
            else {
                double vx = (nextTouches[0].getAbsolutePoint().x - mLastPoints[0].getAbsolutePoint().x) / ts;
                double vy = (nextTouches[0].getAbsolutePoint().y - mLastPoints[0].getAbsolutePoint().y) / ts;
                mVelocityPoint = new CGPoint(vx, vy);
            }
        }
    }

    private boolean moveOutOfBounds(@NonNull UITouch[] touches) {
        if (mMaxTouches == null) {
            return true;
        }
        UIView view = getView();
        if (view == null) {
            return true;
        }
        int accepted = 0;
        double allowableMovement = 8.0;
        for (int i = 0; i < touches.length; i++) {
            CGPoint p0 = touches[i].locationInView(view);
            for (int j = 0; j < mMaxTouches.length; j++) {
                CGPoint p1 = mMaxTouches[j].locationInView(view);
                if (!p0.inRange(allowableMovement, allowableMovement, p1)) {
                    accepted++;
                    break;
                }
            }
        }
        return accepted > 0;
    }

}
