package com.yy.codex.uikit;

/**
 * Created by PonyCui_Home on 2017/1/11.
 */

public class UIPanGestureRecognizer extends UIGestureRecognizer {

    UITouch[] startTouches;
    CGPoint translatePoint = new CGPoint(0, 0);
    CGPoint velocityPoint = new CGPoint(0, 0);

    public UIPanGestureRecognizer(Object target, String selector) {
        super(target, selector);
    }

    @Override
    public void touchesBegan(UITouch[] touches, UIEvent event) {
        super.touchesBegan(touches, event);
        if (touches.length > 1) {
            mState = UIGestureRecognizerState.Failed;
            return;
        }
        startTouches = touches;
    }

    @Override
    public void touchesMoved(UITouch[] touches, UIEvent event) {
        if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            resetVelocity(touches);
        }
        super.touchesMoved(touches, event);
        if (startTouches == null) {
            mState = UIGestureRecognizerState.Failed;
            return;
        }
        if (mState == UIGestureRecognizerState.Possible && moveOutOfBounds(touches)) {
            setTranslation(new CGPoint(0, 0));
            mState = UIGestureRecognizerState.Began;
            markOtherGestureRecognizersFailed(this);
            sendActions();
        }
        else if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            mState = UIGestureRecognizerState.Changed;
            sendActions();
        }
    }

    @Override
    public void touchesEnded(UITouch[] touches, UIEvent event) {
        if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            resetVelocity(touches);
        }
        super.touchesEnded(touches, event);
        if (mState == UIGestureRecognizerState.Began || mState == UIGestureRecognizerState.Changed) {
            mState = UIGestureRecognizerState.Ended;
            sendActions();
        }
    }

    public CGPoint translation() {
        if (lastPoints.length > 0 && translatePoint != null) {
            return new CGPoint(
                    lastPoints[0].getRelativePoint().getX() - translatePoint.getX(),
                    lastPoints[0].getRelativePoint().getY() - translatePoint.getY()
            );
        }
        return new CGPoint(0, 0);
    }

    public void setTranslation(CGPoint point) {
        if (lastPoints.length > 0) {
            translatePoint = new CGPoint(
                    lastPoints[0].getRelativePoint().getX() + point.getX(),
                    lastPoints[0].getRelativePoint().getY() + point.getY()
            );
        }
    }

    public CGPoint velocity() {
        return velocityPoint;
    }

    private void resetVelocity(UITouch[] nextTouches) {
        if (lastPoints.length > 0 && nextTouches.length > 0) {
            double vx = (nextTouches[0].getRelativePoint().getX() - lastPoints[0].getRelativePoint().getX()) / ((nextTouches[0].getTimestamp() - lastPoints[0].getTimestamp()) / 1000);
            double vy = (nextTouches[0].getRelativePoint().getY() - lastPoints[0].getRelativePoint().getY()) / ((nextTouches[0].getTimestamp() - lastPoints[0].getTimestamp()) / 1000);
            velocityPoint = new CGPoint(vx, vy);
        }
    }

    private boolean moveOutOfBounds(UITouch[] touches) {
        if (startTouches == null) {
            return true;
        }
        int accepted = 0;
        double allowableMovement = 22.0;
        for (int i = 0; i < touches.length; i++) {
            CGPoint p0 = touches[i].locationInView(this.mView);
            for (int j = 0; j < startTouches.length; j++) {
                CGPoint p1 = startTouches[j].locationInView(this.mView);
                if (!p0.inRange(allowableMovement, allowableMovement, p1)) {
                    accepted++;
                    break;
                }
            }
        }
        return accepted > 0;
    }

}
