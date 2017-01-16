package com.yy.codex.uikit;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;

/**
 * Created by saiakirahui on 2017/1/14.
 */

public class UIViewTouchHandler {

    WeakReference<UIView> viewWeakReference = null;
    UIView mHitTestedView;
    long mEventID;
    double[] mHash;

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
                    mHash = null;
                    mHitTestedView.touchesBegan(requestTouches(event), new UIEvent());
                    break;
                case MotionEvent.ACTION_UP:
                    mHitTestedView.touchesEnded(requestTouches(event), new UIEvent());
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_POINTER_DOWN:
                    double[] cHash = requestHash(event);
                    if (mHash != null && isHashSame(mHash, cHash)) {
                        break;
                    }
                    mHash = cHash;
                    mHitTestedView.touchesMoved(requestTouches(event), new UIEvent());
                    break;
            }
        }
    }

    UITouch[] requestTouches(MotionEvent event) {
        UIView view = viewWeakReference.get();
        if (view == null) {
            return new UITouch[0];
        }
        int pointerCount = event.getPointerCount();
        UITouch[] touches = new UITouch[pointerCount];
        double offsetX = (event.getRawX() - event.getX(0)) / UIScreen.mainScreen.scale();
        double offsetY = (event.getRawY() - event.getY(0)) / UIScreen.mainScreen.scale();
        for (int i = 0; i < pointerCount; i++) {
            double x = event.getX(i) / UIScreen.mainScreen.scale();
            double y = event.getY(i) / UIScreen.mainScreen.scale();
            CGPoint convertedPoint = view.convertPoint(new CGPoint(x, y), mHitTestedView);
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

    double[] requestHash(MotionEvent event) {
        int count = event.getPointerCount();
        double[] hash = new double[count * 2];
        for (int i = 0; i < count; i++) {
            hash[i] = (double)event.getX(i);
            hash[i + 1] = (double)event.getY(i);
        }
        return hash;
    }

    boolean isHashSame(double[] hashA, double[] hashB) {
        if (hashA.length == hashB.length) {
            for (int i = 0; i < hashA.length; i++) {
                if (Double.compare(hashA[i], hashB[i]) != 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
