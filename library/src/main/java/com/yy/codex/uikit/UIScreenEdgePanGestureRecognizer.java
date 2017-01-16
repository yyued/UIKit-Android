package com.yy.codex.uikit;

import android.support.annotation.NonNull;

/**
 * Created by cuiminghui on 2017/1/13.
 */

public class UIScreenEdgePanGestureRecognizer extends UIPanGestureRecognizer {

    public enum Edge {
        Top,
        Left,
        Bottom,
        Right,
    }

    public Edge edge = Edge.Left;
    public double edgeLength = 22.0;

    public UIScreenEdgePanGestureRecognizer(@NonNull Object target, @NonNull String selector) {
        super(target, selector);
    }

    public UIScreenEdgePanGestureRecognizer(@NonNull Runnable triggerBlock) {
        super(triggerBlock);
    }

    @Override
    public void touchesBegan(@NonNull UITouch[] touches, @NonNull UIEvent event) {
        super.touchesBegan(touches, event);
        if (!checkEdge(touches)) {
            mState = UIGestureRecognizerState.Failed;
        }
    }

    private boolean checkEdge(UITouch[] touches) {
        if (startTouches == null) {
            return false;
        }
        UIView view = getView();
        if (view == null) {
            return false;
        }
        for (int i = 0; i < touches.length; i++) {
            CGPoint firstPoint = touches[i].getAbsolutePoint();
            if (edge == Edge.Left && firstPoint.getX() < edgeLength) {
                return true;
            }
            else if (edge == Edge.Right && UIScreen.mainScreen.bounds().getWidth() - firstPoint.getX() < edgeLength) {
                return true;
            }
            else if (edge == Edge.Top && firstPoint.getY() < edgeLength) {
                return true;
            }
            else if (edge == Edge.Bottom && UIScreen.mainScreen.bounds().getHeight() - firstPoint.getY() < edgeLength) {
                return true;
            }
        }
        return false;
    }

}
