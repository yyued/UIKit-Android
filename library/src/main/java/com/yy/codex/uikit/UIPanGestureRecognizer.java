package com.yy.codex.uikit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by PonyCui_Home on 2017/1/11.
 */

public class UIPanGestureRecognizer extends UIGestureRecognizer {

    @Nullable UITouch[] mStartTouches;
    @NonNull CGPoint mTranslatePoint = new CGPoint(0, 0);
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
        mStartTouches = touches;
        setTranslation(new CGPoint(0, 0));
    }

    @Override
    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            resetVelocity(touches);
        }
        super.touchesMoved(touches, event);
        if (mStartTouches == null) {
            mState = UIGestureRecognizerState.Failed;
            return;
        }
        if (mState == UIGestureRecognizerState.Possible && moveOutOfBounds(touches)) {
            setTranslation(new CGPoint(-translation().getX(), -translation().getY()));
            mState = UIGestureRecognizerState.Began;
            sendActions();
        }
        else if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
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
        else {
            mState = UIGestureRecognizerState.Failed;
        }
    }

    @NonNull
    public CGPoint translation() {
        if (mLastPoints.length > 0 && mTranslatePoint != null) {
            return new CGPoint(
                    mLastPoints[0].getAbsolutePoint().getX() - mTranslatePoint.getX(),
                    mLastPoints[0].getAbsolutePoint().getY() - mTranslatePoint.getY()
            );
        }
        return new CGPoint(0, 0);
    }

    public void setTranslation(@NonNull CGPoint point) {
        if (mLastPoints.length > 0) {
            mTranslatePoint = new CGPoint(
                    mLastPoints[0].getAbsolutePoint().getX() + point.getX(),
                    mLastPoints[0].getAbsolutePoint().getY() + point.getY()
            );
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
                double vx = (nextTouches[0].getAbsolutePoint().getX() - mLastPoints[0].getAbsolutePoint().getX()) / ts;
                double vy = (nextTouches[0].getAbsolutePoint().getY() - mLastPoints[0].getAbsolutePoint().getY()) / ts;
                mVelocityPoint = new CGPoint(vx, vy);
            }
        }
    }

    private boolean moveOutOfBounds(@NonNull UITouch[] touches) {
        if (mStartTouches == null) {
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
            for (int j = 0; j < mStartTouches.length; j++) {
                CGPoint p1 = mStartTouches[j].locationInView(view);
                if (!p0.inRange(allowableMovement, allowableMovement, p1)) {
                    accepted++;
                    break;
                }
            }
        }
        return accepted > 0;
    }

}
