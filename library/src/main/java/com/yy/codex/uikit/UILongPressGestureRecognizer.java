package com.yy.codex.uikit;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by PonyCui_Home on 2017/1/11.
 */

public class UILongPressGestureRecognizer extends UIGestureRecognizer {

    private UITouch[] startTouches;
    private Timer startTimer;

    public double minimumPressDuration = 0.5;
    public double allowableMovement = 10.0;

    public UILongPressGestureRecognizer(@NonNull Object target, @NonNull String selector) {
        super(target, selector);
    }

    public UILongPressGestureRecognizer(@NonNull Runnable triggerBlock) {
        super(triggerBlock);
    }

    @Override
    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesBegan(touches, event);
        startTouches = touches;
        final UILongPressGestureRecognizer self = this;
        final Handler handler = new Handler();
        startTimer = new Timer();
        startTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mState != UIGestureRecognizerState.Failed) {
                            mState = UIGestureRecognizerState.Began;
//                            markOtherGestureRecognizersFailed(self);
                            sendActions();
                        }
                    }
                });
            }
        }, (long)(minimumPressDuration * 1000));
    }

    @Override
    public void touchesMoved(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesMoved(touches, event);
        if (mState == UIGestureRecognizerState.Possible && moveOutOfBounds(touches)) {
            mState = UIGestureRecognizerState.Failed;
            startTimer.cancel();
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
            startTimer.cancel();
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
        return accepted == 0;
    }

}
