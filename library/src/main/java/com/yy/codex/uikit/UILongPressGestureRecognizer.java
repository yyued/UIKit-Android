package com.yy.codex.uikit;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by PonyCui_Home on 2017/1/11.
 */

public class UILongPressGestureRecognizer extends UIGestureRecognizer {

    private UITouch[] startTouches;

    public double minimumPressDuration = 0.5;
    public double allowableMovement = 10.0;

    public UILongPressGestureRecognizer(Object target, String selector) {
        super(target, selector);
    }

    @Override
    public void touchesBegan(UITouch[] touches, UIEvent event) {
        super.touchesBegan(touches, event);
        startTouches = touches;
        final UILongPressGestureRecognizer self = this;
        final Handler handler = new Handler();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mState != UIGestureRecognizerState.Failed) {
                            mState = UIGestureRecognizerState.Began;
                            markOtherGestureRecognizersFailed(self);
                            sendActions();
                        }
                    }
                });
            }
        }, (long)(minimumPressDuration * 1000));
    }

    @Override
    public void touchesMoved(UITouch[] touches, UIEvent event) {
        super.touchesMoved(touches, event);
        if (mState == UIGestureRecognizerState.Possible && moveOutOfBounds(touches)) {
            mState = UIGestureRecognizerState.Failed;
        }
        else if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            mState = UIGestureRecognizerState.Changed;
            sendActions();
        }
    }

    @Override
    public void touchesEnded(UITouch[] touches, UIEvent event) {
        super.touchesEnded(touches, event);
        if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            mState = UIGestureRecognizerState.Ended;
            sendActions();
        }
    }

    private boolean moveOutOfBounds(UITouch[] touches) {
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
