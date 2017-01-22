package com.yy.codex.uikit;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by PonyCui_Home on 2017/1/11.
 */

public class UILongPressGestureRecognizer extends UIGestureRecognizer {

    private UITouch[] mStartTouches;
    private Timer mStartTimer;

    public double mMinimumPressDuration = 0.5;
    public double mAllowableMovement = 10.0;
    public int mNumberOfTouchesRequired = 1;

    public UILongPressGestureRecognizer(@NonNull Object target, @NonNull String selector) {
        super(target, selector);
    }

    public UILongPressGestureRecognizer(@NonNull Runnable triggerBlock) {
        super(triggerBlock);
    }

    @Override
    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesBegan(touches, event);
        mStartTouches = touches;
        if (touches.length >= mNumberOfTouchesRequired) {
            setupTimer();
        }
    }

    private void setupTimer() {
        final UILongPressGestureRecognizer self = this;
        final Handler handler = new Handler();
        mStartTimer = new Timer();
        mStartTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mState != UIGestureRecognizerState.Failed) {
                            mState = UIGestureRecognizerState.Began;
                            sendActions();
                            UIGestureRecognizerLooper looper = mLooper;
                            if (looper != null) {
                                looper.checkState(self);
                                looper.markFailed();
                            }
                        }
                    }
                });
            }
        }, (long)(mMinimumPressDuration * 1000));
    }

    @Override
    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesMoved(touches, event);
        if (mState == UIGestureRecognizerState.Possible && touches.length > mStartTouches.length && touches.length >= mNumberOfTouchesRequired) {
            mStartTouches = touches;
            setupTimer();
        }
        else if (mState == UIGestureRecognizerState.Possible && mStartTouches.length >= mNumberOfTouchesRequired && moveOutOfBounds(touches)) {
            mState = UIGestureRecognizerState.Failed;
            if (mStartTimer != null) {
                mStartTimer.cancel();
            }
        }
        else if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            mState = UIGestureRecognizerState.Changed;
            sendActions();
        }
    }

    @Override
    public void touchesEnded(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesEnded(touches, event);
        if (mStartTimer != null) {
            mStartTimer.cancel();
        }
        if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            mState = UIGestureRecognizerState.Ended;
            sendActions();
        }
        else {
            mState = UIGestureRecognizerState.Failed;
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
        for (int i = 0; i < touches.length; i++) {
            CGPoint p0 = touches[i].locationInView(view);
            for (int j = 0; j < mStartTouches.length; j++) {
                CGPoint p1 = mStartTouches[j].locationInView(view);
                if (p0.inRange(mAllowableMovement, mAllowableMovement, p1)) {
                    accepted++;
                    break;
                }
            }
        }
        return accepted < mNumberOfTouchesRequired;
    }

}
