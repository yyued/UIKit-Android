package com.yy.codex.uikit;

/**
 * Created by cuiminghui on 2017/1/11.
 */

public class UITapGestureRecognizer extends UIGestureRecognizer {

    private UITouch[] startTouches;

    public int numberOfTapsRequired = 1;
    public int numberOfTouchesRequired = 1;

    public UITapGestureRecognizer(Object target, String selector) {
        super(target, selector);
    }

    @Override
    public void touchesBegan(UITouch[] touches, UIEvent event) {
        super.touchesBegan(touches, event);
        int acceptedTouches = 0;
        for (int i = 0; i < touches.length; i++) {
            if (touches[i].getTapCount() == numberOfTapsRequired) {
                acceptedTouches++;
            }
        }
        if (acceptedTouches >= numberOfTouchesRequired) {
            startTouches = touches;
        }
        else {
            mState = UIGestureRecognizerState.Failed;
        }
    }

    @Override
    public void touchesMoved(UITouch[] touches, UIEvent event) {
        super.touchesMoved(touches, event);
        if (moveOutOfBounds(touches)) {
            mState = UIGestureRecognizerState.Failed;
        }
    }

    @Override
    public void touchesEnded(UITouch[] touches, UIEvent event) {
        super.touchesEnded(touches, event);
        if (!moveOutOfBounds(touches)) {
            mState = UIGestureRecognizerState.Ended;
            sendActions();
        }
    }

    private boolean moveOutOfBounds(UITouch[] touches) {
        if (startTouches == null) {
            return false;
        }
        double allowMovement = 8.0;
        int accepted = 0;
        for (int i = 0; i < touches.length; i++) {
            CGPoint p0 = touches[i].locationInView(this.mView);
            for (int j = 0; j < startTouches.length; j++) {
                CGPoint p1 = startTouches[j].locationInView(this.mView);
                if (p0.inRange(allowMovement, allowMovement, p1)) {
                    accepted++;
                    break;
                }
            }
        }
        return accepted >= startTouches.length;
    }

}
