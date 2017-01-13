package com.yy.codex.uikit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cuiminghui on 2017/1/11.
 */

public class UITapGestureRecognizer extends UIGestureRecognizer {

    private UITouch[] startTouches;

    public int numberOfTapsRequired = 1;
    public int numberOfTouchesRequired = 1;

    public UITapGestureRecognizer(@NonNull Object target, @NonNull String selector) {
        super(target, selector);
    }

    public UITapGestureRecognizer(@NonNull Runnable triggerBlock) {
        super(triggerBlock);
    }

    @Override
    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesBegan(touches, event);
        startTouches = touches;
    }

    @Override
    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesMoved(touches, event);
        if (moveOutOfBounds(touches)) {
            mState = UIGestureRecognizerState.Failed;
        }
    }

    @Nullable private Timer mWaitsTimer;

    @Override
    public void touchesEnded(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesEnded(touches, event);
        if (mState == UIGestureRecognizerState.Failed) {
            return;
        }
        if (numberOfTapsRequired > 1) {
            if (mWaitsTimer != null) {
                mWaitsTimer.cancel();
                mWaitsTimer = null;
            }
            int acceptedTouches = 0;
            for (int i = 0; i < touches.length; i++) {
                if (touches[i].getTapCount() == numberOfTapsRequired) {
                    acceptedTouches++;
                }
            }
            if (acceptedTouches < numberOfTouchesRequired) {
                mWaitsTimer = new Timer();
                mWaitsTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (mState != UIGestureRecognizerState.Ended) {
                            mState = UIGestureRecognizerState.Failed;
                        }
                    }
                }, 300);
                return;
            }
        }
        if (!moveOutOfBounds(touches)) {
            final UITapGestureRecognizer self = this;
            waitOtherGesture(new Runnable() {
                @Override
                public void run() {
                    mState = UIGestureRecognizerState.Ended;
                    markOtherGestureRecognizersFailed(self);
                    sendActions();
                }
            });
        }
    }

    private boolean moveOutOfBounds(@NonNull UITouch[] touches) {
        if (startTouches == null) {
            return true;
        }
        UIView view = getView();
        if (view == null) {
            return true;
        }
        double allowableMovement = 22.0;
        int accepted = 0;
        for (int i = 0; i < touches.length; i++) {
            CGPoint p0 = touches[i].locationInView(view);
            for (int j = 0; j < startTouches.length; j++) {
                CGPoint p1 = startTouches[j].locationInView(view);
                if (p0.inRange(allowableMovement, allowableMovement, p1)) {
                    accepted++;
                    break;
                }
            }
        }
        return accepted < startTouches.length;
    }

}
