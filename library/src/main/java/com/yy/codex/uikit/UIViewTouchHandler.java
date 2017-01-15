package com.yy.codex.uikit;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;
import java.util.Set;

/**
 * Created by saiakirahui on 2017/1/14.
 */

public class UIViewTouchHandler {

    WeakReference<UIView> viewWeakReference = null;
    UIView mHitTestedView;
    long mEventID;
    int mTouchCount;

    UIViewTouchHandler(UIView view) {
        viewWeakReference = new WeakReference<UIView>(view);
    }

    void onTouchEvent(@NonNull MotionEvent event) {
        UIView view = viewWeakReference.get();
        if (view == null) {
            return;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            CGPoint touchPoint = new CGPoint(event.getX() / UIScreen.mainScreen.scale(), event.getY() / UIScreen.mainScreen.scale());
            mHitTestedView = view.hitTest(touchPoint, event);
            mEventID = System.currentTimeMillis();
        }
        if (mHitTestedView != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mHitTestedView.touchesBegan(requestTouches(event), new UIEvent());
                    break;
                case MotionEvent.ACTION_UP:
                    mHitTestedView.touchesEnded(requestTouches(event), new UIEvent());
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mHitTestedView.touchesEnded(requestTouches(event), new UIEvent());
                    break;

                case MotionEvent.ACTION_MOVE:
                    mHitTestedView.touchesMoved(requestTouches(event), new UIEvent());
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    mHitTestedView.touchesBegan(requestTouches(event), new UIEvent());
                    break;
            }
        }
    }

    UITouch[] requestTouches(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        UITouch[] touches = new UITouch[pointerCount];
        double offsetX = (event.getRawX() - event.getX(0)) / UIScreen.mainScreen.scale();
        double offsetY = (event.getRawY() - event.getY(0)) / UIScreen.mainScreen.scale();
        for (int i = 0; i < pointerCount; i++) {
            double x = event.getX(i) / UIScreen.mainScreen.scale();
            double y = event.getY(i) / UIScreen.mainScreen.scale();
            CGPoint convertedPoint = mHitTestedView.convertPoint(new CGPoint(x, y), mHitTestedView);
            CGPoint rawPoint = new CGPoint(x + offsetX, y + offsetY);
            touches[i] = new UITouch(mHitTestedView, convertedPoint, rawPoint, requestPhase(event), mEventID);
        }
        return touches;
    }

    UITouch.Phase requestPhase(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return UITouch.Phase.Began;
            case MotionEvent.ACTION_UP:
                return UITouch.Phase.Ended;
            case MotionEvent.ACTION_POINTER_UP:
                return UITouch.Phase.Ended;
            case MotionEvent.ACTION_MOVE:
                return UITouch.Phase.Moved;
            case MotionEvent.ACTION_POINTER_DOWN:
                return UITouch.Phase.Began;
            default:
                return UITouch.Phase.Ended;
        }
    }

}
