package com.yy.codex.uikit;

import android.support.annotation.NonNull;

/**
 * Created by cuiminghui on 2017/1/13.
 */

public class UISwipeGestureRecognizer extends UIGestureRecognizer {

    public enum Direction {
        Right,
        Left,
        Up,
        Down,
    }

    public Direction direction = Direction.Right;

    @NonNull private CGPoint mOriginalPoint = new CGPoint(0, 0);
    @NonNull private CGPoint mVelocityPoint = new CGPoint(0, 0);

    public UISwipeGestureRecognizer(@NonNull Object target, @NonNull String selector) {
        super(target, selector);
    }

    public UISwipeGestureRecognizer(@NonNull Runnable triggerBlock) {
        super(triggerBlock);
    }

    @Override
    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesBegan(touches, event);
        if (touches.length > 0) {
            mOriginalPoint = touches[0].getAbsolutePoint();
        }
        else {
            mState = UIGestureRecognizerState.Failed;
        }
    }

    @Override
    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        if (touches.length <= 0) {
            mState = UIGestureRecognizerState.Failed;
            return;
        }
        if (mState == UIGestureRecognizerState.Possible) {
            resetVelocity(touches);
        }
        super.touchesMoved(touches, event);
        if (direction == Direction.Right) {
            double distance = touches[0].getAbsolutePoint().x - mOriginalPoint.x;
            if (distance < -22.0) {
                mState = UIGestureRecognizerState.Failed;
            }
            else {
                if (distance > 100.0 && mVelocityPoint.x > 1000.0) {
                    mState = UIGestureRecognizerState.Ended;
                    sendActions();
                }
                else if (mVelocityPoint.x > 500.0) {
                    mState = UIGestureRecognizerState.Ended;
                    sendActions();
                }
            }
        }
        else if (direction == Direction.Left) {
            double distance = touches[0].getAbsolutePoint().x - mOriginalPoint.x;
            if (distance > 22.0) {
                mState = UIGestureRecognizerState.Failed;
            }
            else {
                if (distance < -100.0 && mVelocityPoint.x < -1000.0) {
                    mState = UIGestureRecognizerState.Ended;
                    sendActions();
                }
                else if (mVelocityPoint.x < -500.0) {
                    mState = UIGestureRecognizerState.Ended;
                    sendActions();
                }
            }
        }
        else if (direction == Direction.Down) {
            double distance = touches[0].getAbsolutePoint().y - mOriginalPoint.y;
            if (distance < -22.0) {
                mState = UIGestureRecognizerState.Failed;
            }
            else {
                if (distance > 100.0 && mVelocityPoint.y > 1000.0) {
                    mState = UIGestureRecognizerState.Ended;
                    sendActions();
                }
                else if (mVelocityPoint.y > 500.0) {
                    mState = UIGestureRecognizerState.Ended;
                    sendActions();
                }
            }
        }
        else if (direction == Direction.Up) {
            double distance = touches[0].getAbsolutePoint().y - mOriginalPoint.y;
            if (distance > 22.0) {
                mState = UIGestureRecognizerState.Failed;
            }
            else {
                if (distance < -100.0 && mVelocityPoint.y < -1000.0) {
                    mState = UIGestureRecognizerState.Ended;
                    sendActions();
                }
                else if (mVelocityPoint.y < -500.0) {
                    mState = UIGestureRecognizerState.Ended;
                    sendActions();
                }
            }
        }
    }

    @Override
    public void touchesEnded(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesEnded(touches, event);
        if (mState != UIGestureRecognizerState.Ended) {
            mState = UIGestureRecognizerState.Failed;
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

}
